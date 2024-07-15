package project.springboot.template.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.springboot.template.clients.models.SendEmailRequest;
import project.springboot.template.constant.EApplyStatus;
import project.springboot.template.dto.response.CandidateApplicationResponse;
import project.springboot.template.entity.common.ApiResponse;

import java.util.List;

@FeignClient("email-service")
public interface EmailClient {
    @PostMapping("/api/email")
    ResponseEntity<ApiResponse<List<CandidateApplicationResponse>>> sendEmail(@RequestHeader("Authorization") String token,
                                                                              @RequestBody SendEmailRequest request);
}
