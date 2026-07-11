package com.tom.springboot.tx.note.appilcation.user.service;

import com.tom.springboot.tx.note.domain.account.model.AccountEntity;
import com.tom.springboot.tx.note.domain.account.support.IAccountSupport;
import com.tom.springboot.tx.note.domain.user.model.UserEntity;
import com.tom.springboot.tx.note.domain.user.support.IUserSupport;
import com.tom.springboot.tx.note.infrastructure.dao.user.mapper.UserPO;
import com.tom.springboot.tx.note.infrastructure.dao.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Tom
 * @version 1.0.0
 * @ClassName UserAppService.java
 * @Description TODO
 * @createTime 2025年03月04日 08:32:00
 */
@Service
@Slf4j
public class UserAppService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    IUserSupport userSupport;

    @Autowired
    private IAccountSupport accountSupport;

    public UserPO findUserById(String id) {
        return userMapper.qryUserById(id);
    }

    public void saveNewUser(UserEntity userEntity) {
        // saveNewUser标注有@Transaction
        userSupport.saveNewUser(userEntity.assembleUserId(Long.hashCode(System.currentTimeMillis())));
        log.info("========== userSupport.saveNewUser()执行完成");
        // 创建账户, 账户余额默认100元
        accountSupport.saveAccount(
                AccountEntity.builder().userId(userEntity.getUserId()).balance(new BigDecimal("100")).build());
        log.info("========== accountSupport.saveAccount()执行完成");
    }
}
