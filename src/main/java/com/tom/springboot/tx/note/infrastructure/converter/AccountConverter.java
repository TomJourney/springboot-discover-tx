package com.tom.springboot.tx.note.infrastructure.converter;

import com.tom.springboot.tx.note.domain.account.model.AccountEntity;
import com.tom.springboot.tx.note.domain.user.model.UserEntity;
import com.tom.springboot.tx.note.infrastructure.dao.user.mapper.UserPO;
import com.tom.springboot.tx.note.infrastructure.dao.useraccount.mapper.UserAccountPO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * @author tom
 * @version 1.0.0
 * @Description TODO
 * @createTime 2026年07月11日 16:57:00
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountConverter {

    UserAccountPO toPO(AccountEntity accountEntity);
}
