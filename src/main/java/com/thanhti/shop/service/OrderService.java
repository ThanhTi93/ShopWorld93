package com.thanhti.shop.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

import com.thanhti.shop.domain.Order;

public interface OrderService {

	<S extends Order> List<S> findAll(Example<S> example, Sort sort);

	<S extends Order> List<S> findAll(Example<S> example);

	void deleteAll();

	Order getReferenceById(Long id);

	void deleteAll(Iterable<? extends Order> entities);

	void deleteAllById(Iterable<? extends Long> ids);

	Order getById(Long id);

	void delete(Order entity);

	Order getOne(Long id);

	void deleteById(Long id);

	void deleteAllInBatch();

	long count();

	<S extends Order, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction);

	void deleteAllByIdInBatch(Iterable<Long> ids);

	<S extends Order> boolean exists(Example<S> example);

	void deleteAllInBatch(Iterable<Order> entities);

	<S extends Order> long count(Example<S> example);

	boolean existsById(Long id);

	void deleteInBatch(Iterable<Order> entities);

	Optional<Order> findById(Long id);

	<S extends Order> Page<S> findAll(Example<S> example, Pageable pageable);

	<S extends Order> List<S> saveAllAndFlush(Iterable<S> entities);

	<S extends Order> S saveAndFlush(S entity);

	void flush();

	<S extends Order> List<S> saveAll(Iterable<S> entities);

	List<Order> findAllById(Iterable<Long> ids);

	List<Order> findAll(Sort sort);

	Page<Order> findAll(Pageable pageable);

	List<Order> findAll();

	<S extends Order> Optional<S> findOne(Example<S> example);

	<S extends Order> S save(S entity);

	Page<Order> findByNameContaining(String name, Pageable pageable);

}
