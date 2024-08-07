package project.springboot.template.dto.response;

import lombok.Data;

@Data
public class JobStatisticsResponse {
    private Long jobId;
    private Integer totalApplied;
}
