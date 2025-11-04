package com.gabriel.controller;

import com.gabriel.entity.OrderData;
import com.gabriel.repository.OrderDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class OrderController {

    @Autowired
    private OrderDataRepository orderDataRepository;

    // GET all orders
    @GetMapping("/api/order")
    public ResponseEntity<?> getAllOrders() {
        try {
            List<OrderData> orders = (List<OrderData>) orderDataRepository.findAll();
            return ResponseEntity.ok(orders);
        } catch (Exception ex) {
            log.error("Failed to retrieve orders: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // GET orders by customer ID - uses SQL query from repository
    @GetMapping("/api/order/customer/{customerId}")
    public ResponseEntity<?> getOrdersByCustomer(@PathVariable Integer customerId) {
        try {
            List<OrderData> orders = orderDataRepository.findByCustomerId(customerId);
            return ResponseEntity.ok(orders);
        } catch (Exception ex) {
            log.error("Failed to retrieve orders for customer {}: {}", customerId, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // GET orders by status - uses SQL query from repository
    @GetMapping("/api/order/status/{status}")
    public ResponseEntity<?> getOrdersByStatus(@PathVariable String status) {
        try {
            List<OrderData> orders = orderDataRepository.findByStatus(status);
            return ResponseEntity.ok(orders);
        } catch (Exception ex) {
            log.error("Failed to retrieve orders with status {}: {}", status, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // GET orders with filters - uses custom SQL query with @Query
    @GetMapping("/api/order/filter")
    public ResponseEntity<?> getOrdersWithFilters(
            @RequestParam(required = false) Integer customerId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String customerName) {
        try {
            List<OrderData> orders = orderDataRepository.findOrdersWithFilters(customerId, status, customerName);
            return ResponseEntity.ok(orders);
        } catch (Exception ex) {
            log.error("Failed to retrieve filtered orders: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // GET total amount by customer - uses native SQL query
    @GetMapping("/api/order/customer/{customerId}/total")
    public ResponseEntity<?> getTotalAmountByCustomer(@PathVariable Integer customerId) {
        try {
            Double total = orderDataRepository.getTotalAmountByCustomer(customerId);
            return ResponseEntity.ok(total != null ? total : 0.0);
        } catch (Exception ex) {
            log.error("Failed to get total amount for customer {}: {}", customerId, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // GET order count by status - uses native SQL query
    @GetMapping("/api/order/count/{status}")
    public ResponseEntity<?> getOrderCountByStatus(@PathVariable String status) {
        try {
            Long count = orderDataRepository.getOrderCountByStatus(status);
            return ResponseEntity.ok(count);
        } catch (Exception ex) {
            log.error("Failed to get order count for status {}: {}", status, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // POST create order
    @PutMapping("/api/order")
    public ResponseEntity<?> createOrder(@RequestBody OrderData orderData) {
        try {
            OrderData savedOrder = orderDataRepository.save(orderData);
            log.info("Created order: {}", savedOrder.getId());
            return ResponseEntity.ok(savedOrder);
        } catch (Exception ex) {
            log.error("Failed to create order: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // GET order by ID
    @GetMapping("/api/order/{id}")
    public ResponseEntity<?> getOrder(@PathVariable Integer id) {
        try {
            OrderData order = orderDataRepository.findById(id).orElse(null);
            if (order != null) {
                return ResponseEntity.ok(order);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
            }
        } catch (Exception ex) {
            log.error("Failed to retrieve order {}: {}", id, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // DELETE order
    @DeleteMapping("/api/order/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Integer id) {
        try {
            orderDataRepository.deleteById(id);
            return ResponseEntity.ok("Order deleted successfully");
        } catch (Exception ex) {
            log.error("Failed to delete order {}: {}", id, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
}

