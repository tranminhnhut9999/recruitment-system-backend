package project.springboot.template.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.minio.ObjectWriteResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.springboot.template.config.security.TokenHolder;
import project.springboot.template.constant.EApplyStatus;
import project.springboot.template.dto.request.ChangeStatusApplicationRequest;
import project.springboot.template.dto.request.CreateCandidateApplicationRequest;
import project.springboot.template.dto.request.GetApplicationRequest;
import project.springboot.template.dto.response.CandidateApplicationResponse;
import project.springboot.template.dto.response.JobResponse;
import project.springboot.template.dto.response.StatusLogResponse;
import project.springboot.template.entity.CandidateApplication;
import project.springboot.template.entity.StatusLog;
import project.springboot.template.entity.common.ApiException;
import project.springboot.template.entity.common.ApiResponse;
import project.springboot.template.repository.CandidateApplicationRepository;
import project.springboot.template.repository.StatusLogRepository;
import project.springboot.template.util.ObjectUtil;
import project.springboot.template.util.SecurityUtil;
import project.springboot.template.util.UrlUtil;
import project.springboot.template.util.specification.CandidateApplicationSpecificationBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

@Service
@Transactional
public class CandidateApplicationService {
    private static final Logger log = LoggerFactory.getLogger(CandidateApplicationService.class);
    private final CandidateApplicationRepository candidateApplicationRepository;
    private final StatusLogRepository statusLogRepository;

    private final HttpService httpService;
    private final MinioService minioService;
    @Value("${minio.endpoint}")
    String minioUrl;
    private final ObjectMapper objectMapper;

    public CandidateApplicationService(CandidateApplicationRepository candidateApplicationRepository, StatusLogRepository statusLogRepository, HttpService httpService, MinioService minioService, ObjectMapper objectMapper) {
        this.candidateApplicationRepository = candidateApplicationRepository;
        this.statusLogRepository = statusLogRepository;
        this.httpService = httpService;
        this.minioService = minioService;
        this.objectMapper = objectMapper;
    }

    public List<CandidateApplicationResponse> getAllByQuery(GetApplicationRequest query) {
        /*Build query with Specification*/
        CandidateApplicationSpecificationBuilder specifications = CandidateApplicationSpecificationBuilder.specifications();
        Specification<CandidateApplication> criteriaSpecification = specifications
                .byStatus(query.getStatus())
                .byInterviewEmail(query.getInterviewEmail())
                .byJobId(query.getJobId()).build();

        List<CandidateApplication> candidateApplications = candidateApplicationRepository.findAll(criteriaSpecification);

        return candidateApplications.stream().map(candidateApplication -> {
            CandidateApplicationResponse response = ObjectUtil.copyProperties(candidateApplication, new CandidateApplicationResponse(), CandidateApplicationResponse.class, true);
            List<StatusLogResponse> statusLogResponses = candidateApplication.getStatusLogs().stream().map(statusLog -> ObjectUtil.copyProperties(statusLog, new StatusLogResponse(), StatusLogResponse.class, true)).collect(Collectors.toList());
            response.setStatusLogs(statusLogResponses);
            return response;
        }).collect(Collectors.toList());
    }

    public CandidateApplicationResponse getApplicationById(Long id) {
        CandidateApplication candidateApplication = candidateApplicationRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy đơn tuyển dụng:" + id));
        CandidateApplicationResponse response = ObjectUtil.copyProperties(candidateApplication, new CandidateApplicationResponse(), CandidateApplicationResponse.class, true);
        List<StatusLogResponse> statusLogResponses = candidateApplication.getStatusLogs().stream().map(statusLog -> ObjectUtil.copyProperties(statusLog, new StatusLogResponse(), StatusLogResponse.class, true)).collect(Collectors.toList());
        response.setStatusLogs(statusLogResponses);
        return response;
    }

    public CandidateApplicationResponse createApplication(CreateCandidateApplicationRequest request) {
        Long jobId = request.getJobId();
        if (jobId == null) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không thể để trống công việc cần nộp");
        }
        JobResponse appliedJob = null;
        ApiResponse<JobResponse> response = this.httpService.<JobResponse>get("http://localhost:8082/api/jobs/hiring/" + jobId, new HashMap<>(), TokenHolder.getToken());
        if (Objects.equals(response.getStatusCode(), "OK")) {
            appliedJob = objectMapper.convertValue(response.getData(), new TypeReference<JobResponse>() {
            });
        }
        if (appliedJob == null) {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy công việc đang tuyển");
        }

        CandidateApplication candidateApplication = new CandidateApplication();
        candidateApplication.setName(request.getName());
        candidateApplication.setEmail(request.getEmail());
        candidateApplication.setPhoneNumber(request.getPhoneNumber());
        candidateApplication.setDateOfBirth(request.getDateOfBirth());
        candidateApplication.setAddress(request.getAddress());
        candidateApplication.setJobId(jobId);
        candidateApplication.setStatus(EApplyStatus.APPLYING);


        // Upload CV and then set the URL
        MultipartFile cvFile = request.getCvFile();
        if (cvFile != null) {
            try {
                ObjectWriteResponse objectResponse = this.minioService.uploadFile(cvFile.getName(), cvFile.getContentType(), cvFile.getInputStream(), cvFile.getSize());
                String accessResourceURL = UrlUtil.buildUrl(minioUrl, objectResponse);
                candidateApplication.setCvUrl(accessResourceURL);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }

        this.candidateApplicationRepository.save(candidateApplication);
        return ObjectUtil.copyProperties(candidateApplication, new CandidateApplicationResponse(), CandidateApplicationResponse.class, true);
    }

    public StatusLogResponse updateStatusOfApplication(Long id, ChangeStatusApplicationRequest request) {
        CandidateApplication candidateApplication = candidateApplicationRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy đơn tuyển dụng:" + id));
        String emailOptional = SecurityUtil.getCurrentUserEmail()
                .orElseThrow(() -> ApiException.create(HttpStatus.FORBIDDEN).withMessage("Người dùng không hợp lệ"));

        if (candidateApplication.getStatus() != EApplyStatus.APPLYING && !Objects.equals(emailOptional, candidateApplication.getInterviewer())) {
            throw ApiException.create(HttpStatus.CONFLICT).withMessage("Hồ sơ này đã được tiếp nhận!");
        }

        StatusLog statusLog = new StatusLog();
        statusLog.setStatus(request.getStatus());
        statusLog.setCandidateApplication(candidateApplication);
        statusLog.setNote(request.getNote());

        candidateApplication.getStatusLogs().add(statusLog);

        this.candidateApplicationRepository.save(candidateApplication);
        this.statusLogRepository.save(statusLog);
        return ObjectUtil.copyProperties(statusLog, new StatusLogResponse(), StatusLogResponse.class, true);
    }
}
