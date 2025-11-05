package com.gabriel.serviceimpl;

import com.gabriel.entity.CustomerData;
import com.gabriel.model.Customer;
import com.gabriel.repository.CustomerDataRepository;
import com.gabriel.service.CustomerService;
import com.gabriel.util.Transform;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerDataRepository customerDataRepository;

    Transform<CustomerData, Customer> transformCustomerData = new Transform<>(Customer.class);
    Transform<Customer, CustomerData> transformCustomer = new Transform<>(CustomerData.class);

    @Override
    public List<Customer> getAll() {
        List<CustomerData> customerDataRecords = new ArrayList<>();
        List<Customer> customers = new ArrayList<>();

        customerDataRepository.findAll().forEach(customerDataRecords::add);
        Iterator<CustomerData> it = customerDataRecords.iterator();

        while(it.hasNext()) {
            CustomerData customerData = it.next();
            Customer customer = transformCustomerData.transform(customerData);
            customers.add(customer);
        }
        return customers;
    }

    @Override
    public Customer get(Integer id) {
        log.info("Input id >> " + Integer.toString(id));
        Customer customer = null;
        Optional<CustomerData> optional = customerDataRepository.findById(id);
        if(optional.isPresent()) {
            log.info("Is present >> ");
            customer = transformCustomerData.transform(optional.get());
        }
        else {
            log.info("Failed >> unable to locate id: " + Integer.toString(id));
        }
        return customer;
    }

    @Override
    public Customer create(Customer customer) {
        log.info("add:Input {}", customer.toString());
        CustomerData customerData = transformCustomer.transform(customer);
        CustomerData updatedCustomerData = customerDataRepository.save(customerData);
        log.info("add:Output {}", updatedCustomerData.toString());
        return transformCustomerData.transform(updatedCustomerData);
    }

    @Override
    public Customer update(Customer customer) {
        Optional<CustomerData> optional = customerDataRepository.findById(customer.getId());
        if(optional.isPresent()){
            CustomerData customerData = transformCustomer.transform(customer);
            CustomerData updatedCustomerData = customerDataRepository.save(customerData);
            return transformCustomerData.transform(updatedCustomerData);
        }
        else {
            log.error("Customer record with id: {} does not exist", customer.getId());
        }
        return null;
    }

    @Override
    public void delete(Integer id) {
        Optional<CustomerData> optional = customerDataRepository.findById(id);
        if(optional.isPresent()) {
            CustomerData customerData = optional.get();
            customerDataRepository.delete(customerData);
            log.info("Success >> deleted customer: {}", id);
        }
        else {
            log.info("Failed >> unable to locate customer id: {}", id);
        }
    }
}

