package booking.home.booking.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import booking.home.booking.enity.User;

@Repository
public interface AccountRepo  extends JpaRepository<User, Long>   {

}
