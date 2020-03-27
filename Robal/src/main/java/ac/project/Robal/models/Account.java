package ac.project.Robal.models;

import java.util.List;

public class Account {

	private Long id;
	private List<String> s_owner;
	private List<String> customers;
	private String administrator;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<String> getS_owner() {
		return s_owner;
	}
	public void setS_owner(List<String> s_owner) {
		this.s_owner = s_owner;
	}
	public List<String> getCustomers() {
		return customers;
	}
	public void setCustomers(List<String> customers) {
		this.customers = customers;
	}
	public String getAdministrator() {
		return administrator;
	}
	public void setAdministrator(String administrator) {
		this.administrator = administrator;
	}
	
	
	
}
