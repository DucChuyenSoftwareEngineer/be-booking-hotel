package booking.home.booking.vo.account;

import lombok.Data;

@Data
public class Register {
	private String username;
	private String password;
	private String fullName;
	private String email;
	private String phone;
	private String legalNo;
	private String role; 
	private String address;
}
