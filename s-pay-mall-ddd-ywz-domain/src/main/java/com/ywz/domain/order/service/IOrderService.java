package com.ywz.domain.order.service;


import com.ywz.domain.order.model.entity.PayOrderEntity;
import com.ywz.domain.order.model.entity.ShopCartEntity;

public interface IOrderService {

    PayOrderEntity createOrder(ShopCartEntity shopCartEntity) throws Exception;

}
