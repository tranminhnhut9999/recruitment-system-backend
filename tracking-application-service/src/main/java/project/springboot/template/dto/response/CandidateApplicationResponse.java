package project.springboot.template.dto.response;

import lombok.Data;
import project.springboot.template.constant.EApplyStatus;
import project.springboot.template.entity.StatusLog;
import project.springboot.template.entity.common.BaseEntity;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
public class CandidateApplicationResponse extends BaseEntity {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private Instant dateOfBirth;
    private String address;
    private String cvUrl;
    private Long jobId;
    private Instant applyDate;
    private String interviewer;
    private EApplyStatus status;
    private List<StatusLogResponse> statusLogs = new ArrayList<>();
}
