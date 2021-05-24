package booking.home.booking.repo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import booking.home.booking.enity.Host;
import booking.home.booking.enity.OrderDeal;


@Repository
public interface OrderDealRepo  extends JpaRepository<OrderDeal, Long> {

	
	public List<OrderDeal> findOrderDealBytimeRoom(String timeRoom);
	
	public List<OrderDeal> findOrderDealByStatus(String status);
	
	public List<OrderDeal> findOrderDealByHost(Host host);
}
