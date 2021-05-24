package booking.home.booking.repo;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import booking.home.booking.enity.Host;


@Repository
public interface HostRepo extends JpaRepository<Host, Long> {

	public Optional<Host> findHostByuserID(long userID);
	
	@Transactional
	@Modifying
	@Query("update Host h set h.status = ?1 where h.userID = ?2")
	void setHostStatusById(Boolean status,long id);
}
