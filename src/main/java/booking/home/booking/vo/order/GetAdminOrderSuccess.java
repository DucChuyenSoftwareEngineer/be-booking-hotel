package booking.home.booking.vo.order;

import java.math.BigDecimal;

import lombok.Data;


@Data
public class GetAdminOrderSuccess {
	private String hotelName;
	private String customerName;
	private BigDecimal price;
	private String timeOrder;
	private String fullName;
	private String legalNo;
	private String address;
	private String email;
	private String phone;
}
