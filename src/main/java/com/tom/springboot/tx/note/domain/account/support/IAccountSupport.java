package com.tom.springboot.tx.note.domain.account.support;

import com.tom.springboot.tx.note.domain.account.model.AccountEntity;

/**
 * @author tom
 * @version 1.0.0
 * @Description TODO
 * @createTime 2026年07月11日 16:53:00
 */
public interface IAccountSupport {

    void saveAccount(AccountEntity accountEntity);
}
