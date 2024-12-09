package hcmute.nhom.kltn.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import hcmute.nhom.kltn.model.UserProfile;

/**
 * Class UserProfileRepository.
 *
 * @author: ThanhTrong
 **/
public interface UserProfileRepository extends AbstractRepository<UserProfile, String> {

    @Query(value = "SELECT usrp.* FROM t_user_profile usrp INNER JOIN t_user u ON "
            + " u.profile_id = usrp.id WHERE u.email = :email", nativeQuery = true)
    Optional<UserProfile> findByEmail(@Param("email") String email);
}
