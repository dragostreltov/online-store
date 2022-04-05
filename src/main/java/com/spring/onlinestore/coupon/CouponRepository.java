package com.spring.onlinestore.coupon;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer>{
	
	Optional<Coupon> findByCode(String code);
}
