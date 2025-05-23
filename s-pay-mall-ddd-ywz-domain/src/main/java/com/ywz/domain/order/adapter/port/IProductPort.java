package com.ywz.domain.order.adapter.port;


import com.ywz.domain.order.model.entity.ProductEntity;

public interface IProductPort {
    ProductEntity queryProductByProductId(String productId);

}
