package com.tom.springboot.tx.note.infrastructure.dao.user.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Tom
 * @version 1.0.0
 * @ClassName UserMapper.java
 * @Description TODO
 * @createTime 2025年03月04日 06:46:00
 */
public interface UserMapper {
    UserPO qryUserById(@Param("id") String id);
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    void insertUser(UserPO userPO);
}
