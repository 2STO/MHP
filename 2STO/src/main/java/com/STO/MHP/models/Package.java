package com.STO.MHP.models;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "package")
public class Package {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private long id;
	
	private String amount;
        
        private String packages;
        
        private String payment;
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
        
        public String getPackages() {
		return packages;
	}

	public void setPackages(String packages) {
		this.packages = packages;
	}
        
        public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}        

        
	@Override
	public String toString() {
		return "Package [id=" + id + ",amount="+ amount+",packages="+ packages +",payment="+ payment + "]";
	}





	
}
