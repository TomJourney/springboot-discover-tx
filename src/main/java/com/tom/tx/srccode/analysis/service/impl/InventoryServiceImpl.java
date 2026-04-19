package com.tom.tx.srccode.analysis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tom.tx.srccode.analysis.entity.InventoryEntity;
import com.tom.tx.srccode.analysis.entity.OrderEntity;
import com.tom.tx.srccode.analysis.mapper.InventoryMapper;
import org.springframework.stereotype.Component;
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
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, InventoryEntity> {

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean save(InventoryEntity inventoryEntity) {
        super.save(inventoryEntity);
        OrderEntity.builder().id("1").orderNo("20260419-01").orderName()
    }
}
