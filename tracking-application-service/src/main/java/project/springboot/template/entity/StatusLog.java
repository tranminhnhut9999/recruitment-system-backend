package project.springboot.template.entity;

import lombok.Data;
import project.springboot.template.constant.EApplyStatus;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "_status_log")
public class StatusLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private EApplyStatus status;
    private Instant createTime;
    private String note;
    @ManyToOne
    @JoinColumn(name = "application_id")
    private CandidateApplication candidateApplication;
}
