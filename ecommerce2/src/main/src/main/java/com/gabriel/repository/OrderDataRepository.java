package com.gabriel.repository;

import com.gabriel.entity.OrderData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderDataRepository extends CrudRepository<OrderData, Integer> {
    
    // Find orders by customer ID
    List<OrderData> findByCustomerId(Integer customerId);
    
    // Find orders by status
    List<OrderData> findByStatus(String status);
    
    // Find orders by customer ID and status
    List<OrderData> findByCustomerIdAndStatus(Integer customerId, String status);
    
    // Custom SQL query to find orders with filters
    @Query("SELECT o FROM OrderData o WHERE " +
           "(:customerId IS NULL OR o.customerId = :customerId) AND " +
           "(:status IS NULL OR o.status = :status) AND " +
           "(:customerName IS NULL OR o.customerName LIKE %:customerName%)")
    List<OrderData> findOrdersWithFilters(
        @Param("customerId") Integer customerId,
        @Param("status") String status,
        @Param("customerName") String customerName
    );
    
    // Native SQL query example - get total amount by customer
    @Query(value = "SELECT SUM(total_amount) FROM order_data WHERE customer_id = :customerId", nativeQuery = true)
    Double getTotalAmountByCustomer(@Param("customerId") Integer customerId);
    
    // Native SQL query - get order count by status
    @Query(value = "SELECT COUNT(*) FROM order_data WHERE status = :status", nativeQuery = true)
    Long getOrderCountByStatus(@Param("status") String status);
    
    // Custom JPQL query - get orders with total amount greater than
    @Query("SELECT o FROM OrderData o WHERE o.totalAmount >= :minAmount ORDER BY o.created DESC")
    List<OrderData> findOrdersWithMinAmount(@Param("minAmount") Double minAmount);
}

