package project.springboot.template.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.minio.ObjectWriteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.springboot.template.config.RabbitMQConfig;
import project.springboot.template.constant.EApplyStatus;
import project.springboot.template.dto.request.ChangeStatusApplicationRequest;
import project.springboot.template.dto.request.CreateCandidateApplicationRequest;
import project.springboot.template.dto.request.GetApplicationRequest;
import project.springboot.template.dto.response.CandidateApplicationResponse;
import project.springboot.template.dto.response.StatusLogResponse;
import project.springboot.template.entity.common.ApiResponse;
import project.springboot.template.service.CandidateApplicationService;
import project.springboot.template.service.EventPublisher;
import project.springboot.template.service.MinioService;
import project.springboot.template.util.UrlUtil;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class CandidateApplicationController {
    private final CandidateApplicationService candidateApplicationService;
    private final MinioService minioService;
    @Value("${minio.endpoint}")
    String minioUrl;

    public CandidateApplicationController(CandidateApplicationService candidateApplicationService, MinioService minioService) {
        this.candidateApplicationService = candidateApplicationService;
        this.minioService = minioService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CandidateApplicationResponse>>> getCandidateApplication(@RequestParam("status") EApplyStatus status,
                                                                                                   @RequestParam("jobId") Long jobId,
                                                                                                   @RequestParam(value = "interviewEmail", required = false) String interviewEmail) {
        GetApplicationRequest getApplicationRequest = new GetApplicationRequest(status, jobId, interviewEmail);
        // Your logic here
        return ResponseEntity.ok(ApiResponse.success(this.candidateApplicationService.getAllByQuery(getApplicationRequest)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CandidateApplicationResponse>> getCandidateApplicationById(@PathVariable Long id) {
        // Your logic here
        return ResponseEntity.ok(ApiResponse.success(this.candidateApplicationService.getApplicationById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CandidateApplicationResponse>> createCandidateApplication(@ModelAttribute CreateCandidateApplicationRequest request) throws JsonProcessingException {
        // Your logic here
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(this.candidateApplicationService.createApplication(request)));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<StatusLogResponse>> changeApplicationStatus(@PathVariable Long id, @RequestBody ChangeStatusApplicationRequest request) {
        // Your logic here
        return ResponseEntity.ok(ApiResponse.success(this.candidateApplicationService.updateStatusOfApplication(id, request)));
    }

    @PostMapping("/test")
    public ResponseEntity<ApiResponse<String>> testUploadFile(@RequestParam MultipartFile file) throws IOException {
        ObjectWriteResponse response = minioService.uploadFile(file.getOriginalFilename(), file.getContentType(), file.getInputStream(), file.getSize());
        String s = UrlUtil.buildUrl(minioUrl, response);
        // Your logic here
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(s));
    }

    @Autowired
    private EventPublisher eventPublisher;

    @GetMapping("/test-queue")
    public ResponseEntity<ApiResponse<Void>> testQueue() throws IOException {
        eventPublisher.publishEvent(RabbitMQConfig.NEW_APPLICATION_RK, "HELLO WORLD");
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success());
    }
}
