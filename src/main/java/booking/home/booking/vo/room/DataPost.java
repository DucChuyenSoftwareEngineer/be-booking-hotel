package booking.home.booking.vo.room;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class DataPost {
	 
	
		private Long id;
	
	    private String name;

	   
	    private String description;

	   
	    private BigDecimal price;

	  
	    private Integer areaRoom;


	    /**
	     * hướng dẫn thủ tục  nhận phòng
	     */
	   
	    private String guidelineCheckin;

	   
	    private String guidelineUsingService;

	  
	    private Integer maxPeopleAmount;

	    /**
	     * Số ngày tối đa mà cho phép được đặt nhé
	     */
	   
	    private Integer maxDateAmount;


	    /**
	     * Cái này lấy mặc đinh từ personal service sum toàn bộ quantity room (1)
	     */
	    
	    private Integer roomTotal;

	    /**
	     * Cái này lấy mặc đinh từ personal service sum toàn bộ quantity bed (2)
	     */
	    
	    private Integer bedTotal;

	    
	    private String roomProfileImage;


	   
	    private String streetDetail;

	    /**
	     * cái này  là account_id
	     */

	    
	    private String status;


	    private List<String> urlImg;
	    
	    private String idProvince;
	    
	    private String idHost;
	    
	    private String idType; 
	   
}
