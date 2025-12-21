package com.tom.springboot.tx.note.appilcation.user.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Tom
 * @version 1.0.0
 * @ClassName UserDTO.java
 * @Description TODO
 * @createTime 2025年03月04日 22:11:00
 */
@Data
public class AccountDTO {

    private String accountId;
    private String userId;
    private BigDecimal balance;


}
