package com.tom.springboot.tx.note.infrastructure.dao.useraccount.support;

import com.tom.springboot.tx.note.domain.account.model.AccountEntity;
import com.tom.springboot.tx.note.domain.account.support.IAccountSupport;
import com.tom.springboot.tx.note.infrastructure.converter.AccountConverter;
import com.tom.springboot.tx.note.infrastructure.dao.useraccount.mapper.UserAccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author tom
 * @version 1.0.0
 * @Description TODO
 * @createTime 2026年07月11日 16:57:00
 */
@Component
public class AccountSupportImpl implements IAccountSupport {

    @Autowired
    private AccountConverter accountConverter;

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Override
    public void saveAccount(AccountEntity accountEntity) {
        userAccountMapper.insertUserAccount(accountConverter.toPO(accountEntity));
    }
}
