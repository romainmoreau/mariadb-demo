package demo.mariadb;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DemoRepository extends JpaRepository<Demo, Long> {
	@Query("select max(demo.id) from Demo demo")
	Optional<Long> findMaxId();
}
