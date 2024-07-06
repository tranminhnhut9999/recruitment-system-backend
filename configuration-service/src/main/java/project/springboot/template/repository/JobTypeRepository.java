package project.springboot.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.springboot.template.entity.JobType;


@Repository
public interface JobTypeRepository extends JpaRepository<JobType, Long> {
}
