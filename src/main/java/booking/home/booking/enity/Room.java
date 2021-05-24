package booking.home.booking.enity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * this is room information
 */
@Data
@Entity
@Table(name = "room")
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "description",columnDefinition="TEXT", nullable = true)
    private String description;

    @Column(name = "price", nullable = true)
    private BigDecimal price;

    @Column(name = "area_room", nullable = true)
    private Integer areaRoom;


    /**
     * hướng dẫn thủ tục  nhận phòng
     */
    @Column(name = "guideline_checkin", nullable = true)
    private String guidelineCheckin;

    @Column(name = "guideline_using_service", nullable = true)
    private String guidelineUsingService;

    @Column(name = "max_people_amount", nullable = true)
    private Integer maxPeopleAmount;

    /**
     * Số ngày tối đa mà cho phép được đặt nhé
     */
    @Column(name = "max_date_amount", nullable = true)
    private Integer maxDateAmount;


    /**
     * Cái này lấy mặc đinh từ personal service sum toàn bộ quantity room (1)
     */
    @Column(name = "room_total", nullable = true)
    private Integer roomTotal;

    /**
     * Cái này lấy mặc đinh từ personal service sum toàn bộ quantity bed (2)
     */
    @Column(name = "bed_total", nullable = true)
    private Integer bedTotal;

    @Column(name = "room_profile_image", nullable = true)
    private String roomProfileImage;


    @Column(name = "street_detail", nullable = true)
    private String streetDetail;
    
    @Column(name = "mapLink",columnDefinition="TEXT", nullable = true)
    private String Maps;

    /**
     * cái này  là account_id
     */
  

    
    @Column(name = "status", nullable = true)
    private String status;


    @Column(name = "date_create", nullable = true)
    private Date dateCreate;
    
    @Column(name = "numberShip", nullable = true)
    private int numberShip;

    @Column(name = "date_modify")
    private Date dateModify;

  
    
    @ManyToOne 
    @JoinColumn(name="provinceid")
    @JsonIgnore
    private Province province;
    
    @ManyToOne 
    @JoinColumn(name="hostId")
    @JsonIgnore
    private Host host;
    
    
    @ManyToOne 
    @JoinColumn(name="roomtypeId")
    @JsonIgnore
    private RoomType roomType;
    
    @OneToMany
    @JoinColumn(name="room_id")
    private List<RoomImg> roomImgs;
    

}
