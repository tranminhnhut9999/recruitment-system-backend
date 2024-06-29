package project.springboot.template.dto.response;

import lombok.Data;
import project.springboot.template.constant.EApplyStatus;
import project.springboot.template.entity.CandidateApplication;

import javax.persistence.*;
import java.time.Instant;

@Data
public class StatusLogResponse {
    private Long id;
    private EApplyStatus status;
    private Instant createTime;
    private String note;
}
