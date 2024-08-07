package project.springboot.template.util.specification;

import org.springframework.data.jpa.domain.Specification;
import project.springboot.template.entity.Job;
import project.springboot.template.entity.Job_;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JobJPASpecificationBuilder {

    public static JobJPASpecificationBuilder specifications() {
        return new JobJPASpecificationBuilder();
    }

    private final List<Specification<Job>> specifications = new ArrayList<>();

    public JobJPASpecificationBuilder byTitleContains(String q) {
        if (q == null) {
            return this;
        }
        specifications.add((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(Job_.TITLE), "%" + q + "%")
        );
        return this;
    }

    public JobJPASpecificationBuilder byTagContains(String q) {
        if (q == null) {
            return this;
        }
        specifications.add((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(Job_.KEYWORDS), "%" + q + "%")
        );
        return this;
    }

    public JobJPASpecificationBuilder byDescriptionContains(String q) {
        if (q == null) {
            return this;
        }
        specifications.add((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(Job_.DESCRIPTION), "%" + q + "%")
        );
        return this;
    }

    public JobJPASpecificationBuilder bySearchingQuery(String q) {
        if (q == null) {
            return this;
        }
        specifications.add((root, query, criteriaBuilder) ->
                {
                    Predicate byDescription = criteriaBuilder.like(root.get(Job_.DESCRIPTION), "%" + q + "%");
                    Predicate byTitle = criteriaBuilder.like(root.get(Job_.TITLE), "%" + q + "%");
                    Predicate byKeyword = criteriaBuilder.like(root.get(Job_.KEYWORDS), "%" + q + "%");
                    return criteriaBuilder.or(byDescription, byTitle, byKeyword);
                }
        );
        return this;
    }

    public JobJPASpecificationBuilder byDepartment(String department) {
        if (department == null || department.isEmpty()) {
            return this;
        }
        specifications.add((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(Job_.DEPARTMENT), department)
        );
        return this;
    }

    public JobJPASpecificationBuilder byStatus(Boolean status) {
        if (status == null) {
            return this;
        }
        specifications.add((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(Job_.STATUS), status)
        );
        return this;
    }


    public Specification<Job> build() {
        return specifications.stream()
                .filter(Objects::nonNull)
                .reduce(all(), Specification::and);
    }

    private Specification<Job> all() {
        return Specification.where(null);
    }

}
