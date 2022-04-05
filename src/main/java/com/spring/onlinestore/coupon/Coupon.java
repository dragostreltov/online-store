package com.spring.onlinestore.coupon;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.stereotype.Component;

@Component
@Entity
public class Coupon {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
    
    @Column(unique = true)
    private String code;
    
    private double percentage;

    public Coupon () {
    	super();
    }
    
	public Coupon(Integer id, String code, double percentage) {
		super();
		this.id = id;
		this.code = code;
		this.percentage = percentage;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}
    
}
