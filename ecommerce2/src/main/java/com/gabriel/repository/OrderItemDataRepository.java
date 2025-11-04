package com.gabriel.repository;

import com.gabriel.entity.OrderItemData;
import com.gabriel.enums.OrderItemStatus;
import com.gabriel.model.OrderItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderItemDataRepository extends CrudRepository<OrderItemData, Integer> {
    List<OrderItemData> findAllByCustomerId(Integer customerId);
//    List<OrderItemData> saveAll(List<OrderItemData> orderItemData);
}
