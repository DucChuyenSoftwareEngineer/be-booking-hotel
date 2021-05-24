package booking.home.booking.vo.order;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class GetCustomerOrder {
	
		private Long idCustomer;
		private Long idRoom;
		private String hotelName;
		private String customerName;
		private String phone;
		private String email;
		private String timeOrder;
		private String Note;
		private BigDecimal price;
}
