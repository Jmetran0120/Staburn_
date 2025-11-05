package com.gabriel.controller;

import com.gabriel.model.Customer;
import com.gabriel.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CustomerController
 * Provides customer CRUD endpoints
 */
@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * GET /api/customer - Get all customers
     */
    @GetMapping("/api/customer")
    public ResponseEntity<?> getAllCustomers() {
        try {
            List<Customer> customers = customerService.getAll();
            return ResponseEntity.ok(customers);
        } catch (Exception ex) {
            log.error("Failed to retrieve customers: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    /**
     * GET /api/customer/{id} - Get customer by ID
     */
    @GetMapping("/api/customer/{id}")
    public ResponseEntity<?> getCustomer(@PathVariable Integer id) {
        try {
            Customer customer = customerService.get(id);
            if (customer != null) {
                return ResponseEntity.ok(customer);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
            }
        } catch (Exception ex) {
            log.error("Failed to retrieve customer {}: {}", id, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    /**
     * PUT /api/customer - Add customer (BaseHttpService.add() uses PUT)
     */
    @PutMapping("/api/customer")
    public ResponseEntity<?> addCustomer(@RequestBody Customer customer) {
        try {
            log.info("Adding customer: {}", customer.toString());
            Customer newCustomer = customerService.create(customer);
            log.info("Created customer: {}", newCustomer.toString());
            return ResponseEntity.ok(newCustomer);
        } catch (Exception ex) {
            log.error("Failed to add customer: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    /**
     * POST /api/customer - Update customer (BaseHttpService.update() uses POST)
     */
    @PostMapping("/api/customer")
    public ResponseEntity<?> updateCustomer(@RequestBody Customer customer) {
        try {
            log.info("Updating customer: {}", customer.toString());
            Customer updatedCustomer = customerService.update(customer);
            if (updatedCustomer != null) {
                return ResponseEntity.ok(updatedCustomer);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
            }
        } catch (Exception ex) {
            log.error("Failed to update customer: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    /**
     * DELETE /api/customer/{id} - Delete customer
     */
    @DeleteMapping("/api/customer/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Integer id) {
        try {
            log.info("Deleting customer with id: {}", id);
            customerService.delete(id);
            return ResponseEntity.ok("Customer deleted successfully");
        } catch (Exception ex) {
            log.error("Failed to delete customer {}: {}", id, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
}
