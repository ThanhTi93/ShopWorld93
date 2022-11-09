package com.thanhti.shop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thanhti.shop.domain.Order;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{

	
	Page<Order> findByNameContaining(String name, Pageable pageable);
}
