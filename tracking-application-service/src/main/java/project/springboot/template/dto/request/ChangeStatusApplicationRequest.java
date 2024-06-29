package project.springboot.template.dto.request;

import lombok.Data;
import project.springboot.template.constant.EApplyStatus;

@Data
public class ChangeStatusApplicationRequest {
    private EApplyStatus status;
    private String note;
}
