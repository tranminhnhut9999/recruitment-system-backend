package project.springboot.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.springboot.template.entity.Email;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {
}
