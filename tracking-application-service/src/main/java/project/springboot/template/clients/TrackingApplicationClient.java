package project.springboot.template.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import project.springboot.template.constant.EApplyStatus;
import project.springboot.template.dto.response.CandidateApplicationResponse;
import project.springboot.template.entity.common.ApiResponse;

import java.util.List;

@FeignClient("tracking-service")
public interface TrackingApplicationClient {
    @GetMapping("/api/applications")
    ResponseEntity<ApiResponse<List<CandidateApplicationResponse>>> getApplications(@RequestHeader("Authorization") String token,
                                                                                    @RequestParam("status") EApplyStatus status,
                                                                                    @RequestParam("jobId") Long jobId,
                                                                                    @RequestParam("interviewEmail") String email);

    @GetMapping("/api/applications/{id}")
    ResponseEntity<ApiResponse<List<CandidateApplicationResponse>>> getApplicationById(@RequestHeader("Authorization") String token,
                                                                                       @PathVariable("id") Long candidateId);

}
