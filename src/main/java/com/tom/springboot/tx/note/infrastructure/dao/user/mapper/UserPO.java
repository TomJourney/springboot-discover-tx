package com.tom.springboot.tx.note.infrastructure.dao.user.mapper;

import lombok.Data;

/**
 * @author Tom
 * @version 1.0.0
 * @ClassName UserPO.java
 * @Description TODO
 * @createTime 2025年03月04日 06:45:00
 */
@Data
public class UserPO {
    private Integer id;
    private String name;
    private String mobilePhone;
    private String address;
}