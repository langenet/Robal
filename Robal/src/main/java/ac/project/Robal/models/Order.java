package ac.project.Robal.models;

import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
@Table(name="orders")
public class Order {

	@Id
	@SequenceGenerator(name = "order_seq", sequenceName = "order_seq", initialValue = 1, allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="order_seq")
	private Long orderId;	
	
	private Long invoiceNumber;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date purchaseDate;
	
	private Double subTotal;
	private Double total;

	@ManyToOne(targetEntity = Customer.class, cascade = CascadeType.ALL)
	@JoinColumn(name="account_id_fk")
	private Customer customer;

	@OneToMany(targetEntity = OrderProduct.class, cascade = CascadeType.ALL)
	@JoinColumn(name="order_id_fk")
	private List<OrderProduct> orderProducts;
	
	
}
