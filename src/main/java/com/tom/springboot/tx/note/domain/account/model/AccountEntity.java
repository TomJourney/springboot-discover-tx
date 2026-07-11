package com.tom.springboot.tx.note.domain.account.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * @author Tom
 * @version 1.0.0
 * @ClassName UserAccountPO.java
 * @Description TODO
 * @createTime 2025年03月06日 08:31:00
 */

@Builder
@Getter
public class AccountEntity {
    private int id;
    private int userId;
    private BigDecimal balance;
}
