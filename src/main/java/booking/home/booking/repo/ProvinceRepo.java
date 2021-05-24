package booking.home.booking.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import booking.home.booking.enity.Province;;


@Repository
public interface ProvinceRepo extends JpaRepository<Province, Long> {
	
	Optional<Province> findByProvinceid(String provinceid);
}
