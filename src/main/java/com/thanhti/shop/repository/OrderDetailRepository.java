package com.thanhti.shop.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.thanhti.shop.domain.OrderDetail;


@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long>{

	@Query("Select od From OrderDetail od where od.order.orderId = ?1")
	List<OrderDetail> findByOrderId(Long orderId);
}
