package com.tom.tx.srccode.analysis.entity;

import lombok.Builder;
import lombok.Getter;

/**
 * @author tom
 * @version 1.0.0
 * @Description TODO
 * @createTime 2026年04月19日 11:46:00
 */
@Builder
@Getter
public class OrderEntity {
    private String id;
    private String orderNo;
    private String orderName;
}
