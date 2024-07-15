package project.springboot.template.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import project.springboot.template.clients.models.CandidateApplicationResponse;
import project.springboot.template.clients.models.EApplyStatus;
import project.springboot.template.entity.common.ApiResponse;

import java.util.List;

public interface TrackingApplicationClient {

}
