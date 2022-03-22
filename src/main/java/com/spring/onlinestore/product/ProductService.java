package com.spring.onlinestore.product;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class ProductService {
	
	private static List<Product> products = new ArrayList<>();
	private static int prodCount = 3;
	
	static {
		products.add(new Product(1, "Iphone 13", "Telefon APPLE iPhone 13 Pro Max 5G, 1TB, Alpine Green", 9000.0));
		products.add(new Product(2, "Samsung 13", "Telefon Samsung iPhone 13 Pro Max 5G, 1TB, Blue", 7000.0));
		products.add(new Product(3, "Xiaomi 11", "Telefon Xiaomi 11 Pro Max 5G, 1TB, White", 4000.0));
	}
	
	public List<Product> listAllProducts() {
		return products;
	}
	
	public Product findOne(int id) {
		for(Product prod:products) {
			if(prod.getId() == id) {
				return prod;
			}
		}
		return null;
	}
	
	public Product save(Product prod) { 
		if(prod.getId()==null) {
			prod.setId(++prodCount);
		}
		products.add(prod);
		return prod;
	}
	
	public Product deleteById(int id) {
		Iterator<Product> iterator = products.iterator();
		while(iterator.hasNext()) {
			Product prod = iterator.next();
			if(prod.getId() == id) {
				iterator.remove();
				return prod;
			}
		}
		return null;
	}
}
