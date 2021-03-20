package com.bezkoder.spring.jpa.postgresql.service;


import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.bezkoder.spring.jpa.postgresql.model.Customer;
import com.bezkoder.spring.jpa.postgresql.repository.CustomerRepository;
import com.google.i18n.phonenumbers.NumberParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;


@Service
@CacheConfig(cacheNames = "customerCache")
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Transactional
	@Cacheable(cacheNames = "customers")
	@Override
	public void getAll(String title, List<Customer> customers) {
		
		if (title == null)
			customerRepository.findAll().forEach(customers::add);
		else
			customerRepository.findByTitleContaining(title).forEach(customers::add);
	}

	@Transactional
	@Override
	public Customer addCustomer(Customer customer) throws NumberParseException {
		return customerRepository.save(new Customer(customer.getTitle(), customer.getDescription(), customer.getPhoneNumber(), customer.getCountryCode(), customer.getCountryName(), customer.getOperatorName(),false));
	}


	@Transactional
	@CacheEvict(cacheNames = "customers", allEntries = true)
	@Override
	public void saveCust(Customer customer) {
		customerRepository.save(customer);
		
	}

	@Transactional
	@CacheEvict(cacheNames = "customers", allEntries = true)
	@Override
	public void update(Customer customer, Customer repCustomer) throws NumberParseException {
		
		repCustomer.setTitle(customer.getTitle());
		repCustomer.setDescription(customer.getDescription());
        repCustomer.setPhoneNumber(customer.getPhoneNumber());
		repCustomer.setOperatorName(customer.getOperatorName());
		repCustomer.setCountryCode(customer.getCountryCode());
		repCustomer.setCountryName(customer.getCountryName());
        repCustomer.setPublished(customer.isPublished());
        
		this.customerRepository.save(repCustomer);
	}

	@Transactional
	@Caching(evict = { @CacheEvict(cacheNames = "customer", key = "#id"),
	@CacheEvict(cacheNames = "customers", allEntries = true) })
	@Override
	public void deleteCust(long id) {
		this.customerRepository.deleteById(id);
	}

	@Transactional
	@Caching(evict = { @CacheEvict(cacheNames = "customer", key = "#id"),
	@CacheEvict(cacheNames = "customers", allEntries = true) })
	@Override
	public void deleteAllCust() {
		this.customerRepository.deleteAll();
	}

	@Transactional
	@Cacheable(cacheNames = "customer", key = "#id", unless = "#result == null")
	@Override
	public Optional<Customer> getCustById(long id) {
		return this.customerRepository.findById(id);
	}

	@Transactional
	@CacheEvict(cacheNames = "customers", allEntries = true)
	@Override
	public List<Customer> publishedCust() {
		return customerRepository.findByPublished(true);
		
	}


}