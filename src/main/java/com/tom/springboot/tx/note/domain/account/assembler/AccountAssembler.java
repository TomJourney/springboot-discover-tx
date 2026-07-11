package com.tom.springboot.tx.note.domain.account.assembler;

import com.tom.springboot.tx.note.appilcation.user.dto.AccountDTO;
import com.tom.springboot.tx.note.domain.account.model.AccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * @author Tom
 * @version 1.0.0
 * @ClassName UserAssembler.java
 * @Description TODO
 * @createTime 2025年03月04日 22:14:00
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountAssembler {

    AccountEntity toEntity(AccountDTO accountDTO);
}
