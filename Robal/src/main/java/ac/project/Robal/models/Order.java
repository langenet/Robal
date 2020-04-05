package ac.project.Robal.models;

import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name="orders")

public class Order {

	@Id
	@GeneratedValue
	private Long orderId;	
	private Long invoiceNumber;
	private Long storeId;
	private Long customerId;
	private Date purchaseDate;
	private Double subTotal;
	private Double total;

	
	@ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
        name = "Order_Products", 
        joinColumns = { @JoinColumn(name = "order_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "product_id") }
    )
	private List<Product> products;
	
}
