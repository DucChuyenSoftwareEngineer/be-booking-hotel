package booking.home.booking.repo;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import booking.home.booking.enity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

	public Optional<User> findUserByUsername(String username);
	
	public Optional<User> findUserRoleByToken(String token);
	
	public Optional<User> findUserByUsernameAndPassword(String username,String password);
	
	public List<User> findUserByStatus(boolean status);
	
	@Transactional
	@Modifying
	@Query("update User u set u.status = ?1 where u.id = ?2")
	void setHostStatusById(Boolean status,long userID);
}
