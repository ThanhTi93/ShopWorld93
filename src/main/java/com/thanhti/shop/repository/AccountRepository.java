package com.thanhti.shop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thanhti.shop.domain.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, String>{

	Page<Account> findByNameContaining(String name, Pageable pageable);
}
