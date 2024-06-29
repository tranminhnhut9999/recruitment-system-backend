package project.springboot.template.entity;

import lombok.Data;
import project.springboot.template.constant.EApplyStatus;
import project.springboot.template.entity.common.BaseEntity;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "_candidate_application")
public class CandidateApplication extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @OneToMany(mappedBy = "candidateApplication", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<StatusLog> statusLogs = new ArrayList<>();
}
