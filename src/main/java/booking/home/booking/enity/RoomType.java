package booking.home.booking.enity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Data
@Entity
@Table(name = "room_type")
public class RoomType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date_create", nullable = false)
    private Date dateCreate;

    @Column(name = "date_modify")
    private Date dateModify;

    @Column(name = "account_create", nullable = false)
    private Integer accountCreate;

    @Column(name = "account_modify")
    private Integer accountModify;

    @Column(name = "status", nullable = false)
    private Integer status;
    


}
