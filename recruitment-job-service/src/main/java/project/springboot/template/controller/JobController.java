package project.springboot.template.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.springboot.template.dto.request.ChangeJobStatusRequest;
import project.springboot.template.dto.request.CreateJobRequest;
import project.springboot.template.dto.request.UpdateJobRequest;
import project.springboot.template.dto.response.JobDetailResponse;
import project.springboot.template.dto.response.JobResponse;
import project.springboot.template.entity.Job;
import project.springboot.template.entity.common.ApiResponse;
import project.springboot.template.service.JobService;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;

    // Get Hiring Jobs
    @GetMapping("/hiring")
    public ResponseEntity<ApiResponse<List<JobResponse>>> getHiringJobs() {
        return ResponseEntity.ok(ApiResponse.success(this.jobService.getHiringJob()));
    }

    // Get Hiring Detail Job
    @GetMapping("/hiring/{id}")
    public ResponseEntity<ApiResponse<JobDetailResponse>> getHiringJobDetail(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ApiResponse.success(this.jobService.getHiringDetailJob(id)));
    }

    // Get All Jobs
    @GetMapping
    public ResponseEntity<ApiResponse<List<JobResponse>>> getAllJobs() {
        return ResponseEntity.ok(ApiResponse.success(this.jobService.getAllJob()));
    }

    // Get Detail Jobs
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobDetailResponse>> getJobDetail(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ApiResponse.success(this.jobService.getDetailJob(id)));
    }

    // Create Job
    @PostMapping
    public ResponseEntity<ApiResponse<JobDetailResponse>> createJob(@RequestBody CreateJobRequest request) {
        return ResponseEntity.ok(ApiResponse.success(this.jobService.createJob(request)));
    }

    // Update Job
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<JobDetailResponse>> updateJob(@PathVariable("id") Long id, @RequestBody UpdateJobRequest request) {
        return ResponseEntity.ok(ApiResponse.success(this.jobService.updateJob(id, request)));
    }

    // Change Job Status
    @PutMapping("/{id}/change-status")
    public ResponseEntity<ApiResponse<JobResponse>> updateJobStatus(@PathVariable("id") Long id, @RequestBody ChangeJobStatusRequest request) {
        return ResponseEntity.ok(ApiResponse.success(this.jobService.changeJobStatus(id, request.isStatus())));
    }

    // Delete Job
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteJob(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ApiResponse.success(this.jobService.deleteJob(id)));
    }
}
