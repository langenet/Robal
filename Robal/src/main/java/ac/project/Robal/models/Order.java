package ac.project.Robal.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name="orders")
public class Order {

	@Id
	@SequenceGenerator(name = "order_seq", sequenceName = "order_seq", initialValue = 1, allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="order_seq")
	private Long orderId;	
	
	private Long invoiceNumber;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate purchaseDate;
	
	private Double subTotal;
	private Double total;

	@OneToMany(cascade = CascadeType.ALL)
//	@JoinColumn(name="order_id_fk")
	private List<OrderProduct> orderProducts;

	
}
