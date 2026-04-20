package com.tom.tx.srccode.analysis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tom.tx.srccode.analysis.entity.InventoryEntity;
import com.tom.tx.srccode.analysis.entity.OrderEntity;
import com.tom.tx.srccode.analysis.mapper.order.OrderMapper;
import com.tom.tx.srccode.analysis.service.InventoryService;

/**
 * @author tom
 * @version 1.0.0
 * @Description TODO
 * @createTime 2026年04月20日 21:47:00
 */
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderEntity> implements InventoryService {
    @Override
    public boolean save(InventoryEntity entity) {
        return false;
    }
}
