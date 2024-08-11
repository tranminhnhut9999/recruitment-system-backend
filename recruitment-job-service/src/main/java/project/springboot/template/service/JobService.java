package project.springboot.template.service;

import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import project.springboot.template.clients.TrackingApplicationClient;
import project.springboot.template.clients.models.CandidateApplicationResponse;
import project.springboot.template.clients.models.EApplyStatus;
import project.springboot.template.config.security.TokenHolder;
import project.springboot.template.config.security.jwt.JwtUtil;
import project.springboot.template.dto.request.CreateJobRequest;
import project.springboot.template.dto.request.UpdateJobRequest;
import project.springboot.template.dto.response.JobDetailResponse;
import project.springboot.template.dto.response.JobResponse;
import project.springboot.template.entity.Job;
import project.springboot.template.entity.common.ApiException;
import project.springboot.template.entity.common.ApiResponse;
import project.springboot.template.repository.JobRepository;
import project.springboot.template.util.ObjectUtil;
import project.springboot.template.util.SecurityUtil;
import project.springboot.template.util.specification.JobJPASpecificationBuilder;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class JobService {
    private static final Logger log = LoggerFactory.getLogger(JobService.class);
    private final JobRepository jobRepository;
    private final HttpService httpService;
    private final TrackingApplicationClient trackingApplicationClient;
    private final JwtUtil jwtUtil;

    public JobService(JobRepository jobRepository, HttpService request, TrackingApplicationClient trackingApplicationClient, JwtUtil jwtUtil, JwtUtil jwtUtil1) {
        this.jobRepository = jobRepository;
        this.httpService = request;
        this.trackingApplicationClient = trackingApplicationClient;
        this.jwtUtil = jwtUtil1;
    }

    public JobDetailResponse createJob(CreateJobRequest createJobRequest) {
        Job.JobBuilder jobBuilder = Job.builder()
                .department(createJobRequest.getDepartment())
                .endDate(createJobRequest.getEndDate())
                .startDate(createJobRequest.getStartDate())
                .description(createJobRequest.getDescription())
                .title(createJobRequest.getTitle())
                .targetNumber(createJobRequest.getTargetNumber())
                .salaryRangeTo(createJobRequest.getSalaryRangeTo())
                .salaryRangeFrom(createJobRequest.getSalaryRangeFrom())
                .status(createJobRequest.isStatus())
                .requiredExperience(createJobRequest.getRequiredExperience())
                .jobType(createJobRequest.getJobType())
                .workingPlace(createJobRequest.getWorkingPlace());

        if (!createJobRequest.getKeywords().isEmpty()) {
            String keyworkAsString = String.join("-", createJobRequest.getKeywords());
            jobBuilder.keywords(keyworkAsString);
        }
        if (!createJobRequest.getRecruiters().isEmpty()) {
            String recruiterAsString = String.join("-", createJobRequest.getRecruiters());
            jobBuilder.recruiters(recruiterAsString);
        }
        Job job = jobBuilder.build();
        Job savedJob = jobRepository.save(job);
        return ObjectUtil.copyProperties(savedJob, new JobDetailResponse(), JobDetailResponse.class, true);
    }

    public List<JobResponse> getHiringJob(String query, String department) {

        JobJPASpecificationBuilder jobJPASpecificationBuilder = JobJPASpecificationBuilder.specifications();
        Specification<Job> jobSpecification = jobJPASpecificationBuilder
                .byStatus(true)
                .bySearchingQuery(query)
                .byDepartment(department).build();
        List<Job> hiringJobs = jobRepository.findAll(jobSpecification);

        return hiringJobs.stream()
                .map(job -> {
                    JobResponse jobResponse = ObjectUtil.copyProperties(job, new JobResponse(), JobResponse.class, true);
                    jobResponse.setKeywords(this.extractKeyword(job.getKeywords()));
                    jobResponse.setRecruiters(this.extractKeyword(job.getRecruiters()));
                    return jobResponse;
                }).collect(Collectors.toList());
    }

    public List<JobResponse> getAllJob() {
        List<Job> hiringJobs;
        String userEmail = SecurityUtil.getCurrentUserEmail().orElseThrow(() -> ApiException.create(HttpStatus.FORBIDDEN).withMessage("Không tìm thấy email"));
        if (SecurityUtil.isCurrentUserHasAuthority("HR_MANAGER")) {
            hiringJobs = jobRepository.findAll();
        } else if (SecurityUtil.isCurrentUserHasAuthority("HR_STAFF")) {
            // TODO: get EMAIL
            hiringJobs = jobRepository.findJobByRecruitersLike(userEmail);
        } else {
            throw ApiException.create(HttpStatus.FORBIDDEN).withMessage("Bạn không có thẩm quyền để thực hiện chức năng này");
        }

        return hiringJobs.stream()
                .map(job -> {
                    JobResponse jobResponse = ObjectUtil.copyProperties(job, new JobResponse(), JobResponse.class, true);
                    jobResponse.setKeywords(this.extractKeyword(job.getKeywords()));
                    return jobResponse;
                }).collect(Collectors.toList());
    }

    public JobDetailResponse getHiringDetailJob(Long id) {
        Job hiringJob = jobRepository.findJobByIdAndStatus(id, true)
                .orElseThrow(() -> ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Công việc đã đóng:" + id));
        if (!hiringJob.isStatus()) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Công việc không khả dụng");
        }
        JobDetailResponse jobResponse = ObjectUtil.copyProperties(hiringJob, new JobDetailResponse(), JobDetailResponse.class, true);
        jobResponse.setKeywords(this.extractKeyword(hiringJob.getKeywords()));
        return jobResponse;
    }

    public JobDetailResponse getDetailJob(Long id) {
        Job hiringJob = jobRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Không tìm thấy job:" + id));
        JobDetailResponse jobResponse = ObjectUtil.copyProperties(hiringJob, new JobDetailResponse(), JobDetailResponse.class, true);
        jobResponse.setKeywords(this.extractKeyword(hiringJob.getKeywords()));
        jobResponse.setRecruiters(this.extractKeyword(hiringJob.getRecruiters()));
        return jobResponse;
    }


    private Set<String> extractKeyword(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return new HashSet<>();
        }
        return Set.of(keyword.split("-"));
    }

    public JobResponse changeJobStatus(Long id, boolean status) {
        Job job = this.jobRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Không tìm thấy công việc:" + id));
        job.setStatus(status);
        this.jobRepository.save(job);
        return ObjectUtil.copyProperties(job, new JobResponse(), JobResponse.class, true);
    }

    public JobDetailResponse updateJob(Long id, UpdateJobRequest updateJobRequest) {
        Job updatedJob = this.jobRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Không tìm thấy công việc:" + id));

        updatedJob.setDepartment(updateJobRequest.getDepartment());
        updatedJob.setStartDate(updateJobRequest.getStartDate());
        updatedJob.setEndDate(updateJobRequest.getEndDate());
        updatedJob.setDescription(updateJobRequest.getDescription());
        updatedJob.setTitle(updateJobRequest.getTitle());
        updatedJob.setTargetNumber(updateJobRequest.getTargetNumber());
        updatedJob.setSalaryRangeTo(updateJobRequest.getSalaryRangeTo());
        updatedJob.setSalaryRangeFrom(updateJobRequest.getSalaryRangeFrom());
        updatedJob.setStatus(updateJobRequest.isStatus());
        updatedJob.setRequiredExperience(updateJobRequest.getRequiredExperience());
        updatedJob.setJobType(updateJobRequest.getJobType());

        if (!updateJobRequest.getKeywords().isEmpty()) {
            String keyworkAsString = String.join("-", updateJobRequest.getKeywords());
            updatedJob.setKeywords(keyworkAsString);
        }

        if (!updateJobRequest.getRecruiters().isEmpty()) {
            StringBuilder recruiterAsStringBuffer = new StringBuilder();
            List<String> recruiters = updateJobRequest.getRecruiters();
            for (int i = 0; i < recruiters.size(); i++) {
                recruiterAsStringBuffer.append(recruiters.get(i));
                if (i < recruiters.size() - 1) {
                    recruiterAsStringBuffer.append(",");
                }
            }
            updatedJob.setRecruiters(recruiterAsStringBuffer.toString());
        }

        Job savedJob = jobRepository.save(updatedJob);
        return ObjectUtil.copyProperties(savedJob, new JobDetailResponse(), JobDetailResponse.class, true);
    }

    public Boolean deleteJob(Long id) {
        try {
            Job job = this.jobRepository.findById(id)
                    .orElseThrow(() -> ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Không tìm thấy công việc:" + id));
            this.jobRepository.delete(job);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void checkJobsStartingToday() {
        List<Job> jobsStartingToday = jobRepository.findAllJobsWithStartDateToday();

        if (!jobsStartingToday.isEmpty()) {
            // Process the jobs as needed
            jobsStartingToday.forEach(job -> {
                job.setStatus(true);
                System.out.println("Job starting today: " + job.getTitle());
            });
            this.jobRepository.saveAll(jobsStartingToday);
        }
    }
}
