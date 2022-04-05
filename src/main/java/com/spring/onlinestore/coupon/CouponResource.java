package com.spring.onlinestore.coupon;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.spring.onlinestore.exception.NotFoundException;

@RestController
public class CouponResource {

	@Autowired
	CouponRepository couponRepository;
	
	@GetMapping("/coupons")
	public List<Coupon> retrieveAllCoupons() {
		return couponRepository.findAll();
	}
	
	@PostMapping("/coupons")
	public ResponseEntity<Object> createCoupon(@RequestBody Coupon coupon) {
		Coupon savedCoupon = couponRepository.save(coupon);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedCoupon.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/coupons/{id}")
	public Coupon editCoupon(@PathVariable int id, @RequestBody Coupon coupon) {
		Optional<Coupon> optional = couponRepository.findById(id);
		if(optional.isEmpty()) throw new NotFoundException("Coupon id - " + id);
		coupon.setId(id);
		return couponRepository.save(coupon);
	}
	
	@DeleteMapping("/coupons/{id}")
	public ResponseEntity<String> deleteCoupon(@PathVariable int id) {
		Optional<Coupon> optional = couponRepository.findById(id);
		if(optional.isEmpty()) throw new NotFoundException("Coupon id - " + id);
		couponRepository.deleteById(id);
		return ResponseEntity.ok().body("Coupon deleted");
	}
}
