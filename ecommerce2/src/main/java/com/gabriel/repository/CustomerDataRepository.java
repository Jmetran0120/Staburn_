package com.gabriel.repository;

import com.gabriel.entity.CustomerData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerDataRepository extends CrudRepository<CustomerData, Integer> {
    
    // Find customer by firstname
    List<CustomerData> findByFirstname(String firstname);
    
    // Find customer by lastname
    List<CustomerData> findByLastname(String lastname);
    
    // Find customer by email
    CustomerData findByEmail(String email);
    
    // Find customers by name (firstname or lastname)
    @Query("SELECT c FROM CustomerData c WHERE " +
           "(:firstname IS NULL OR c.firstname LIKE %:firstname%) AND " +
           "(:lastname IS NULL OR c.lastname LIKE %:lastname%)")
    List<CustomerData> findCustomersByName(
        @Param("firstname") String firstname,
        @Param("lastname") String lastname
    );
    
    // Find all customers
    List<CustomerData> findAll();
}

