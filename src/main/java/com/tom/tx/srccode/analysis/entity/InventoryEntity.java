package com.tom.tx.srccode.analysis.entity;

import lombok.Builder;
import lombok.Getter;

/**
 * @author tom
 * @version 1.0.0
 * @Description TODO
 * @createTime 2026年04月19日 11:45:00
 */
@Builder
@Getter
public class InventoryEntity {

    private String id;
    private int quantity;
    private String skuCode;
}
