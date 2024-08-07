package project.springboot.template.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JobStatisticsResponse {
    private Long jobId;
    private Long totalApplied;
}
