package project.springboot.template.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.springboot.template.constant.EApplyStatus;

@Data
@AllArgsConstructor
public class GetApplicationRequest {
    private EApplyStatus status;
    private Long jobId;
    private String interviewEmail;
}
