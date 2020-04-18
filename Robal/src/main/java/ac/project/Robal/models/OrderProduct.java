package ac.project.Robal.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
public class OrderProduct {
	
	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(name="order_id_fk")
	private Order order;
	
	@ManyToOne
	@JoinColumn(name="store_id_fk")
	private Store store;
		
	@ManyToOne
	@JoinColumn(name="product_id_fk")
	private Product product;
	
	private int inventory;
	private double price;
	
	
}
