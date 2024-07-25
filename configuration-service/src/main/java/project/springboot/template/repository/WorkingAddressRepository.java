package project.springboot.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.springboot.template.entity.WorkingAddress;
@Repository
public interface WorkingAddressRepository extends JpaRepository<WorkingAddress, Long> {
}
