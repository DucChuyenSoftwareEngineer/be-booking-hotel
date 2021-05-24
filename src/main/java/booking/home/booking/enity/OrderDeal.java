package booking.home.booking.enity;

import java.io.Serializable;
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

@Data
@Entity
@Table(name = "orderDeal")
public class OrderDeal implements Serializable  {
	@Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
	
	
	@ManyToOne 
    @JoinColumn(name="customerID")
    @JsonIgnore
    private Customer customer;
	
	
	@ManyToOne 
    @JoinColumn(name="roomID")
    @JsonIgnore
    private Room room;
	
	@ManyToOne 
    @JoinColumn(name="hostID")
    @JsonIgnore
    private Host host;
	
	@Column(name = "status", nullable = true)
	private String status;
	
	@Column(name = "timeRoom", nullable = true)
	private String timeRoom;
	
	@Column(name = "note",columnDefinition="TEXT",nullable = true)
	private String note;
	
	@Column(name = "totalPay", nullable = true)
	private BigDecimal totalPay;
	
	@Column(name = "date_create", nullable = true)
    private Date dateCreate;
	
}
