package booking.home.booking.enity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;




@Data
@Entity
@Table(name = "province")
public class Province implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "provinceid", nullable = false)
    private String provinceid;

    @Column(name = "name", nullable = false)
    private String name;
    
    

}

