package project.springboot.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.springboot.template.entity.Account;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(String email);

    List<Account> findByRole_Code(String roleCode);

    @Query("SELECT ac FROM Account ac WHERE ac.role.code IN :roleCode")
    List<Account> findAllIsContainInRoleCode(@Param("roleCode") List<String> roleCode);
}
