package booking.home.booking.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import booking.home.booking.enity.RoomType;

@Repository
public interface RoomTypeRepo  extends JpaRepository<RoomType, Long>  {

}
