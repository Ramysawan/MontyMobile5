package com.bezkoder.spring.jpa.postgresql.service;

import java.util.List;
import java.util.Optional;

import com.bezkoder.spring.jpa.postgresql.model.Customer;
import com.google.i18n.phonenumbers.NumberParseException;

import org.springframework.stereotype.Component;

@Component
public interface CustomerService {

	public void getAll(String title,List<Customer> customers);

	public void deleteAllCust();

	public void deleteCust(long id);

	public void saveCust(Customer customer);

	public Optional<Customer> getCustById(long id);

	public Customer addCustomer(Customer customer) throws NumberParseException;

	public void update(Customer customer, Customer repCustomer) throws NumberParseException;

	public List<Customer> publishedCust();

}
