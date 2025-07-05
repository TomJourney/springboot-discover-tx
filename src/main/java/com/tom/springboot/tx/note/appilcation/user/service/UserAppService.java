package com.tom.springboot.tx.note.appilcation.user.service;

import com.tom.springboot.tx.note.domain.user.model.UserEntity;
import com.tom.springboot.tx.note.domain.user.support.IUserSupport;
import com.tom.springboot.tx.note.infrastructure.dao.user.mapper.UserPO;
import com.tom.springboot.tx.note.infrastructure.dao.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Tom
 * @version 1.0.0
 * @ClassName UserAppService.java
 * @Description TODO
 * @createTime 2025年03月04日 08:32:00
 */
@Service
public class UserAppService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    IUserSupport userSupport;

    public UserPO findUserById(String id) {
        return userMapper.qryUserById(id);
    }

    public void saveNewUser(UserEntity userEntity) {
        // saveNewUser标注有@Transaction
        userSupport.saveNewUser(userEntity);
    }
}
