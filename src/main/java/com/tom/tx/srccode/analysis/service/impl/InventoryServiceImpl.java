package com.tom.tx.srccode.analysis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tom.tx.srccode.analysis.entity.InventoryEntity;
import com.tom.tx.srccode.analysis.entity.OrderEntity;
import com.tom.tx.srccode.analysis.mapper.inventory.InventoryMapper;
import com.tom.tx.srccode.analysis.service.InventoryService;
import com.tom.tx.srccode.analysis.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 库存服务实现
 * @version 1.0.0
 * @Description TODO
 * @createTime 2026年04月19日 11:43:00
 */
@Service("inventoryService")
@Slf4j
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, InventoryEntity> implements InventoryService {

    @Autowired
    OrderService orderService;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean save(InventoryEntity inventoryEntity) {
        super.save(inventoryEntity);
        OrderEntity orderEntity = OrderEntity.builder().id("1").skuCode("1001").orderNo("2001").build();
        try {
            orderService.save(orderEntity);
            return true;
        } catch (Exception e) {
            log.error("保存订单异常", e);
        }
        return false;
    }
}
