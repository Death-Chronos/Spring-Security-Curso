package com.security.couponservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.couponservice.models.Coupon;
import com.security.couponservice.repositories.CouponRespository;


@RestController
@RequestMapping("/couponapi")
public class CouponRestController {

    @Autowired
    CouponRespository repo;

    @PostMapping("/coupons")
    public Coupon create ( @RequestBody Coupon coupon){
        return repo.save(coupon);
    }

    @GetMapping("/coupons/{code}")
    public Coupon getCoupon(@PathVariable String code) {
        return repo.findByCode(code);
    }
    
}
