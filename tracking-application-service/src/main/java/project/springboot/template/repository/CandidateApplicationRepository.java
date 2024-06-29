package project.springboot.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import project.springboot.template.constant.EApplyStatus;
import project.springboot.template.entity.CandidateApplication;

import java.util.List;

@Repository
public interface CandidateApplicationRepository extends JpaRepository<CandidateApplication, Long>, JpaSpecificationExecutor<CandidateApplication> {
    List<CandidateApplication> findAllByStatus(EApplyStatus status);
}
