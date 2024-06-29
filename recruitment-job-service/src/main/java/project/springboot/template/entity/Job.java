package project.springboot.template.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.*;
import project.springboot.template.entity.common.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "_job")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Job extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String department;
    private String title;
    @Lob
    private String description;
    private Integer targetNumber = 1;
    private BigDecimal salaryRangeFrom = BigDecimal.ZERO;
    private BigDecimal salaryRangeTo = BigDecimal.ZERO;
    private String keywords = "";
    private Instant endDate;
    private Instant startDate;
    private boolean status = false;
    private Float requiredExperience = 0.0F;
    private String recruiters;
    private String jobType = "";
    private String workingPlace = "";
}