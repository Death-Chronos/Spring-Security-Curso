package com.security.couponservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.couponservice.models.Coupon;

public interface CouponRespository extends JpaRepository<Coupon, Long> {

    Coupon findByCode(String code);
    
}
