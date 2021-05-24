package booking.home.booking.repo;

import booking.home.booking.enity.RoomImg;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("RoomImgRepo")
public interface RoomImgRepo extends JpaRepository<RoomImg, Long> {


	
	public List<RoomImg> findRoomImgByroomId(long room_id);
}
