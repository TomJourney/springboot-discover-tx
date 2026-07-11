package com.tom.springboot.tx.note.infrastructure.dao.useraccount.mapper;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Tom
 * @version 1.0.0
 * @ClassName UserMapper.java
 * @Description TODO
 * @createTime 2025年03月04日 06:46:00
 */
public interface UserAccountMapper {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    void insertUserAccount(UserAccountPO userAccountPO);

    @Transactional
    List<String> selectUserId();
}
