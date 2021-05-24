package booking.home.booking.repo;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import booking.home.booking.enity.Room;

@Repository
public interface RoomRepo extends JpaRepository<Room, Long> {

	@Transactional
	@Modifying
	@Query("update Room r set r.status = ?1 where r.id = ?2")
	void setRoomStatusById(String status,long id);
}
