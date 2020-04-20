package ac.project.Robal.models;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

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

	@ManyToOne
	@JoinColumn(name="account_id_fk")
	private Customer customer;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
//	@JoinColumn(name="order_id_fk")
	private List<OrderProduct> orderProducts;

	
}
