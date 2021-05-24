package booking.home.booking.enity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@Table(name = "room_img")
public class RoomImg {

	 private static final long serialVersionUID = 1L;

	    @Id
	    @Column(name = "id", nullable = false)
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private long id;
	   
	    @Column(name = "url", nullable = true)
	    private String url;
	    
	    @Column(name = "date_create", nullable = true)
	    private Date dateCreate;

	    @Column(name = "date_modify")
	    private Date dateModify;
	    
	    @ManyToOne 
	    @JoinColumn(name="room_id")
	    @JsonIgnore
	    private Room room;
}
