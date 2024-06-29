package project.springboot.template.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import project.springboot.template.constant.EApplyStatus;

import java.time.Instant;

@Data
public class CreateCandidateApplicationRequest {
    private String name;
    private String email;
    private String phoneNumber;
    private Instant dateOfBirth;
    private String address;
    private String cvUrl;
    private Long jobId;
    private Instant applyDate;
    private String interviewer;
    private MultipartFile cvFile;
}
