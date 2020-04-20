package ac.project.Robal.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

@Entity
public class OrderProduct {
	
	@Id
	@SequenceGenerator(name = "order_product_seq", sequenceName = "order_product_seq", initialValue = 1, allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="order_product_seq")
	private Long orderProductId;

	@ManyToOne
	@JoinColumn(name="order_id_fk")
	private Order order;
	
	@ManyToOne
	@JoinColumn(name="store_id_fk")
	private Store store;
		
	@ManyToOne
	@JoinColumn(name="product_id_fk")
	private Product product;
	
	private int quantity;
	private double price;
	
}
