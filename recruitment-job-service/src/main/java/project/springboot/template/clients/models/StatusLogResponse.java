package project.springboot.template.clients.models;

import lombok.Data;

import java.time.Instant;

@Data
public class StatusLogResponse {
    private Long id;
    private EApplyStatus status;
    private Instant createTime;
    private String note;
}
