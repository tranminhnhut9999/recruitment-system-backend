package project.springboot.template.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
public class UpdateJobRequest {
    @NotNull
    @NotEmpty
    private String department;
    @NotNull
    @NotEmpty
    private String title;
    @NotNull
    @NotEmpty
    private String description;
    @NotNull
    @NotEmpty
    private Integer targetNumber = 1;
    @NotNull
    @NotEmpty
    private BigDecimal salaryRangeFrom = BigDecimal.ZERO;
    @NotNull
    @NotEmpty
    private BigDecimal salaryRangeTo = BigDecimal.ZERO;
    private List<String> keywords = new ArrayList<>();
    private Instant endDate;
    @NotNull
    @NotEmpty
    private String position;
    private boolean status = false;
    private Float requiredExperience = 0.0F;
}