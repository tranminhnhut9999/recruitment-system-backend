package project.springboot.template.clients.models;

import lombok.Data;
import project.springboot.template.entity.common.BaseEntity;

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
