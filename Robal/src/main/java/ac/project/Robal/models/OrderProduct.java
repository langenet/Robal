package ac.project.Robal.models;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
public class OrderProduct {
	
	@Id
	@SequenceGenerator(name = "order_product_seq", sequenceName = "order_product_seq", initialValue = 1, allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="order_product_seq")
	private Long orderProductId;

	@ManyToOne
	@JoinColumn(name="store_product_id_fk")
	private StoreProduct storeProduct;
	
	private int quantity;
	private double price;
	
}
