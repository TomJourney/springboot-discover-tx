package com.tom.springboot.tx.note.infrastructure.converter;

import com.tom.springboot.tx.note.domain.user.model.UserEntity;
import com.tom.springboot.tx.note.infrastructure.dao.user.mapper.UserPO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * @author Tom
 * @version 1.0.0
 * @ClassName UserConverter.java
 * @Description TODO
 * @createTime 2025年03月04日 22:33:00
 */

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserConverter {

    UserPO toPO(UserEntity userEntity);

}
