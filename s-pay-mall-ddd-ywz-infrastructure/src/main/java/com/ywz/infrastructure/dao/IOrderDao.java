package com.ywz.infrastructure.dao;


import com.ywz.infrastructure.dao.po.PayOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IOrderDao {

    void insert(PayOrder payOrder);

    PayOrder queryUnPayOrder(PayOrder payOrder);

}
