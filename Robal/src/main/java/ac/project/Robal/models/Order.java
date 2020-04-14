package ac.project.Robal.models;

import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
	@GeneratedValue
	private Long orderId;	
	
	private Long invoiceNumber;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date purchaseDate;
	
	private Double subTotal;
	private Double total;

	@ManyToOne(targetEntity = Order.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "account_id", referencedColumnName="orderId")
	private Customer customer;
	
	@ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
        name = "Order_Products", 
        joinColumns = { @JoinColumn(name = "order_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "product_id") }
    )
	private List<Product> products;
	
}
