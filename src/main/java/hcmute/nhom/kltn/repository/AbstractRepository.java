package hcmute.nhom.kltn.repository;

import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Class AbstractRepository.
 *
 * @author: ThanhTrong
 **/
@NoRepositoryBean
public interface AbstractRepository<E, I extends Serializable> extends JpaRepository<E, I> {
}
