package com.spring.onlinestore.category;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class CategoryService {
	
	private static List<Category> cats = new ArrayList<>();
	private static int catsCount = 2;
	
	public List<Category> listAllCategories(){
		return cats;
	}
	
	public Category findOneCategory(int id) {
		for(Category cat:cats) {
			if(cat.getId() == id) {
				return cat;
			}
		}
		return null;
	}
	
	public Category save(Category cat) { 
		if(cat.getId()==null) {
			cat.setId(++catsCount);
		}
		cats.add(cat);
		return cat;
	}
	
	public Category deleteById(int id) {
		Iterator<Category> iterator = cats.iterator();
		while(iterator.hasNext()) {
			Category cat = iterator.next();
			if(cat.getId() == id) {
				iterator.remove();
				return cat;
			}
		}
		return null;
	}
}
