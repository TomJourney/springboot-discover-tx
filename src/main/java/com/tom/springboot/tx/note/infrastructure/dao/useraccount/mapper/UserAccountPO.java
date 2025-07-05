package com.tom.springboot.tx.note.infrastructure.dao.useraccount.mapper;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Tom
 * @version 1.0.0
 * @ClassName UserAccountPO.java
 * @Description TODO
 * @createTime 2025年03月06日 08:31:00
 */

@Data
@Builder
public class UserAccountPO {
    private int id;
    private int userId;
    private BigDecimal balance;
}
