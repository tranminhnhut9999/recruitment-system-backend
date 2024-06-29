package project.springboot.template.dto.response;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class JobResponse {
    private Long id;
    private String department;
    private String title;
    private String description;
    private Integer targetNumber = 1;
    private BigDecimal salaryRangeFrom = BigDecimal.ZERO;
    private BigDecimal salaryRangeTo = BigDecimal.ZERO;
    private Set<String> keywords = new HashSet<>();
    private Instant endDate;
    private Instant startDate;
    private String position;
    private boolean status = false;
    private Float requiredExperience = 0.0F;
    private Set<String> recruiters = new HashSet<>();
    private String jobType = "";
    private String workingPlace = "";
}