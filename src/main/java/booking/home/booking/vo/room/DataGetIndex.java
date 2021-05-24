package booking.home.booking.vo.room;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class DataGetIndex {

	private Long id;
	
	private String name;
	
	private BigDecimal price;
	
	private String roomProfileImage;
	
	private String streetDetail;
}
