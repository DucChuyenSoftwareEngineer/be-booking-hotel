package booking.home.booking.vo.host;

import java.math.BigDecimal;

import javax.persistence.Column;

import lombok.Data;

@Data
public class DataPost {

	
	private String fullname;
	
	
	private String idCard;
	
	
	private String address;
	
	
	private String email;
	
	
	private String phone;
	
	
	private String map;
	
	
	private Boolean status;
	
	
    private BigDecimal totalRevenue;
	
	
	private Long userID;
}
