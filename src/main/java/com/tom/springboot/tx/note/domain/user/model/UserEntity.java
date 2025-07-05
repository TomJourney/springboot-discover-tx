package com.tom.springboot.tx.note.domain.user.model;

import lombok.Builder;
import lombok.Getter;

/**
 * @author Tom
 * @version 1.0.0
 * @ClassName UserEntity.java
 * @Description TODO
 * @createTime 2025年03月04日 22:13:00
 */
@Builder
@Getter
public class UserEntity {

    private Integer id;
    private String name;
    private String mobilePhone;
    private String address;
}
