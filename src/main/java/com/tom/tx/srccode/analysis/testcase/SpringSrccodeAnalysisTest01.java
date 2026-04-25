package com.tom.tx.srccode.analysis.testcase;

import com.tom.tx.srccode.analysis.config.AppConfig;
import com.tom.tx.srccode.analysis.entity.InventoryEntity;
import com.tom.tx.srccode.analysis.service.InventoryService;
import org.springframework.aop.framework.autoproxy.InfrastructureAdvisorAutoProxyCreator;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author tom
 * @version 1.0.0
 * @Description TODO
 * @createTime 2026年04月20日 22:06:00
 */
public class SpringSrccodeAnalysisTest01 {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        //
        InventoryService inventoryService = context.getBean(InventoryService.class);
        InventoryEntity inventoryEntity = InventoryEntity.builder().id("kc001").quantity(10).skuCode("1001").build();
        inventoryService.save(inventoryEntity);

        InfrastructureAdvisorAutoProxyCreator creator;
    }
}
