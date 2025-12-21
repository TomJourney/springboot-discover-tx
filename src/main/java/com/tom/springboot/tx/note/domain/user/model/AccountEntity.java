package com.tom.springboot.tx.note.domain.user.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * @author Tom
 * @version 1.0.0
 * @ClassName UserEntity.java
 * @Description TODO
 * @createTime 2025年03月04日 22:13:00
 */
@Builder
@Getter
public class AccountEntity {
    private String accountId;
    private String userId;
    private BigDecimal balance;
}
