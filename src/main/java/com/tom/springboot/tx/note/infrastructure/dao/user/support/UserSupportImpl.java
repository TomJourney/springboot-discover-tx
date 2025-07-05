package com.tom.springboot.tx.note.infrastructure.dao.user.support;

import com.tom.springboot.tx.note.domain.user.model.UserEntity;
import com.tom.springboot.tx.note.domain.user.support.IUserSupport;
import com.tom.springboot.tx.note.infrastructure.converter.UserConverter;
import com.tom.springboot.tx.note.infrastructure.dao.user.mapper.UserMapper;
import com.tom.springboot.tx.note.infrastructure.dao.useraccount.mapper.UserAccountMapper;
import com.tom.springboot.tx.note.infrastructure.dao.useraccount.mapper.UserAccountPO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Random;

/**
 * @author Tom
 * @version 1.0.0
 * @ClassName UserSupportImpl.java
 * @Description TODO
 * @createTime 2025年03月04日 22:32:00
 */
@Component
@Slf4j
public class UserSupportImpl implements IUserSupport {

    private Random random = new Random();

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserConverter userConverter;

    @Autowired
    UserAccountMapper userAccountMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveNewUser(UserEntity userEntity) {
        userMapper.insertUser(userConverter.toPO(userEntity));
        UserAccountPO userAccountPO = UserAccountPO.builder().userId(userEntity.getId()).balance(new BigDecimal(random.nextInt(10000))).build();
        userAccountMapper.insertUserAccount(userAccountPO);
        log.info("保存用户信息及账户成功");
    }
}
