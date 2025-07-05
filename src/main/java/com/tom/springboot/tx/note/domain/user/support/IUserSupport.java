package com.tom.springboot.tx.note.domain.user.support;

import com.tom.springboot.tx.note.domain.user.model.UserEntity;

/**
 * @author Tom
 * @version 1.0.0
 * @ClassName IUserSupport.java
 * @Description TODO
 * @createTime 2025年03月04日 22:31:00
 */
public interface IUserSupport {

    void saveNewUser(UserEntity userEntity);
}
