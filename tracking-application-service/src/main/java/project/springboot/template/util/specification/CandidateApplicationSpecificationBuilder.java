package project.springboot.template.util.specification;

import org.springframework.data.jpa.domain.Specification;
import project.springboot.template.constant.EApplyStatus;
import project.springboot.template.dto.request.GetApplicationRequest;
import project.springboot.template.entity.CandidateApplication;
import project.springboot.template.entity.CandidateApplication_;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CandidateApplicationSpecificationBuilder {

    public static CandidateApplicationSpecificationBuilder specifications() {
        return new CandidateApplicationSpecificationBuilder();
    }

    private final List<Specification<CandidateApplication>> specifications = new ArrayList<>();

    public CandidateApplicationSpecificationBuilder byStatus(EApplyStatus status) {
        if (status == null || status == EApplyStatus.ALL) {
            return this;
        }
        specifications.add((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(CandidateApplication_.STATUS), status)
        );
        return this;
    }

    public CandidateApplicationSpecificationBuilder byJobId(Long jobId) {
        if (jobId == null) {
            return this;
        }
        specifications.add((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(CandidateApplication_.JOB_ID), jobId)
        );
        return this;
    }

    public CandidateApplicationSpecificationBuilder byInterviewEmail(String interviewEmail) {
        if (interviewEmail == null || interviewEmail.isEmpty()) {
            return this;
        }
        specifications.add((root, query, criteriaBuilder) ->
                {
                    Predicate byInterviewEmailPredicate = criteriaBuilder.equal(root.get(CandidateApplication_.INTERVIEWER), interviewEmail);
                    Predicate byApplyingStatusPredicate = criteriaBuilder.equal(root.get(CandidateApplication_.STATUS), EApplyStatus.APPLYING);
                    return criteriaBuilder.or(byInterviewEmailPredicate, byApplyingStatusPredicate);
                }
        );
        return this;
    }

    public Specification<CandidateApplication> build() {
        return specifications.stream()
                .filter(Objects::nonNull)
                .reduce(all(), Specification::and);
    }

    private Specification<CandidateApplication> all() {
        return Specification.where(null);
    }

}
