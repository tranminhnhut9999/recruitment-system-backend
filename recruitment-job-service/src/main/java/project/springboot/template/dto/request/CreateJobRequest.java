package project.springboot.template.dto.request;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class CreateJobRequest {
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
    private Set<String> keywords = new HashSet<>();
    private Instant endDate;
    private Instant startDate;
    @NotNull
    @NotEmpty
    private String position;
    private boolean status = false;
    private Float requiredExperience = 0.0F;
    @NotNull
    private Set<String> recruiters = new HashSet<>();
    private String jobType = "";
    private String workingPlace = "";
    private String requiredGender = "Không yêu cầu";
}