package com.ywz.domain.order.adapter.repository;


import com.ywz.domain.order.model.aggregate.CreateOrderAggregate;
import com.ywz.domain.order.model.entity.OrderEntity;
import com.ywz.domain.order.model.entity.ShopCartEntity;

public interface IOrderRepository {
    void doSaveOrder(CreateOrderAggregate orderAggregate);

    OrderEntity queryUnPayOrder(ShopCartEntity shopCartEntity);

}
