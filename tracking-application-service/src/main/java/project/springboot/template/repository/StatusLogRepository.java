package project.springboot.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import project.springboot.template.constant.EApplyStatus;
import project.springboot.template.entity.CandidateApplication;
import project.springboot.template.entity.StatusLog;

import java.util.List;

@Repository
public interface StatusLogRepository extends JpaRepository<StatusLog, Long>, JpaSpecificationExecutor<StatusLog> {
    List<StatusLog> findByCandidateApplication(CandidateApplication candidateApplication);
}
