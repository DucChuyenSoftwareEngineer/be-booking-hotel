package booking.home.booking.repo;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import booking.home.booking.enity.Customer;


@Repository
public interface CustomerRepo  extends JpaRepository<Customer, Long>  {

	public Optional<Customer> findCustomerByuserID(long userID);
	
	@Transactional
	@Modifying
	@Query("update Customer cus set cus.status = ?1 where cus.userID = ?2")
	void setCustomerStatusById(Boolean status,long id);
}
