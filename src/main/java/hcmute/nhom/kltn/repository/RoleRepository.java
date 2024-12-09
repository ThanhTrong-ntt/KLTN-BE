package hcmute.nhom.kltn.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import hcmute.nhom.kltn.model.Role;

/**
 * Class RoleRepository.
 *
 * @author: ThanhTrong
 **/
public interface RoleRepository extends AbstractRepository<Role, String> {

    @Query(value = "SELECT * FROM t_role WHERE role_name = :name AND removal_flag = 0", nativeQuery = true)
    Optional<Role> findByName(@Param("name") String name);
}
