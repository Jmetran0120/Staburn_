package com.gabriel.controller;

import com.gabriel.model.OrderItem;
import com.gabriel.service.OrderItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CartController
 * Maps cart operations to OrderItem operations with status=Created (cart items)
 * Frontend CartService expects endpoints at /api/cart
 */
@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class CartController {

    @Autowired
    private OrderItemService orderItemService;

    /**
     * GET /api/cart - Get all cart items for a customer
     * Expected: ?customerId=1
     */
    @GetMapping("/api/cart")
    public ResponseEntity<?> getCartItems(@RequestParam(required = false) Integer customerId) {
        try {
            if (customerId == null) {
                // If no customerId, return empty or all - adjust based on requirements
                return ResponseEntity.ok(List.of());
            }
            List<OrderItem> cartItems = orderItemService.getCartItems(customerId);
            return ResponseEntity.ok(cartItems);
        } catch (Exception ex) {
            log.error("Failed to retrieve cart items for customer {}: {}", customerId, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    /**
     * GET /api/cart/{customerId} - Get cart items for specific customer
     */
    @GetMapping("/api/cart/{customerId}")
    public ResponseEntity<?> getCartItemsByCustomer(@PathVariable Integer customerId) {
        try {
            List<OrderItem> cartItems = orderItemService.getCartItems(customerId);
            return ResponseEntity.ok(cartItems);
        } catch (Exception ex) {
            log.error("Failed to retrieve cart items for customer {}: {}", customerId, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    /**
     * PUT /api/cart - Add item to cart (BaseHttpService.add() uses PUT)
     */
    @PutMapping("/api/cart")
    public ResponseEntity<?> addToCart(@RequestBody OrderItem orderItem) {
        try {
            log.info("Adding item to cart: {}", orderItem.toString());
            OrderItem newOrderItem = orderItemService.create(orderItem);
            log.info("Created cart item: {}", newOrderItem.toString());
            return ResponseEntity.ok(newOrderItem);
        } catch (Exception ex) {
            log.error("Failed to add item to cart: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    /**
     * POST /api/cart - Update cart item (BaseHttpService.update() uses POST)
     */
    @PostMapping("/api/cart")
    public ResponseEntity<?> updateCartItem(@RequestBody OrderItem orderItem) {
        try {
            log.info("Updating cart item: {}", orderItem.toString());
            OrderItem updatedOrderItem = orderItemService.update(orderItem);
            return ResponseEntity.ok(updatedOrderItem);
        } catch (Exception ex) {
            log.error("Failed to update cart item: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    /**
     * DELETE /api/cart/{id} - Remove item from cart
     */
    @DeleteMapping("/api/cart/{id}")
    public ResponseEntity<?> removeFromCart(@PathVariable Integer id) {
        try {
            log.info("Removing cart item with id: {}", id);
            orderItemService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            log.error("Failed to remove cart item {}: {}", id, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
}

