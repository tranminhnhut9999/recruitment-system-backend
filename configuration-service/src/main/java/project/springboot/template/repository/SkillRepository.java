package project.springboot.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.springboot.template.entity.JobType;
import project.springboot.template.entity.Skill;


@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
}
