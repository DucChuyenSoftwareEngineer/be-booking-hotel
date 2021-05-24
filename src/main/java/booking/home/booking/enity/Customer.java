package booking.home.booking.enity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "customer")
public class Customer implements Serializable  {

	@Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
	
	@Column(name = "fullName", nullable = true)
	private String fullname;
	
	@Column(name = "idCard", nullable = true)
	private String idCard;
	
	@Column(name = "address",columnDefinition="TEXT",nullable = true)
	private String address;
	
	@Column(name = "email", nullable = true)
	private String email;
	
	@Column(name = "phone", nullable = true)
	private String phone;
	
	@Column(name = "map",columnDefinition="TEXT",nullable = true)
	private String map;
	
	@Column(name = "status",nullable = true)
	private Boolean status;
	
	@Column(name = "totalBillSuccess", nullable = true)
    private BigDecimal totalBillSuccess;
	
	@Column(name = "totalOrderSuccess", nullable = true)
	private Integer totalOrderSuccess;
	
	@Column(name = "userID", nullable = true)
	private Long userID;
	
	@OneToMany
	@JoinColumn(name="hostID")
	private List<OrderDeal> orderDeals;
	
	
}
