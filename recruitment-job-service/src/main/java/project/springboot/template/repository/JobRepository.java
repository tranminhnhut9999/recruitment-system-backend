package project.springboot.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.springboot.template.entity.Job;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {
    List<Job> findAllByStatusIs(boolean status);

    Optional<Job> findJobByIdAndStatus(Long id, boolean status);

    @Query("SELECT j FROM Job j WHERE j.recruiters LIKE CONCAT('%',:email,'%')")
    List<Job> findJobByRecruitersLike(@Param("email") String email);
}
