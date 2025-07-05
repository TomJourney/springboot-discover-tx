package com.tom.springboot.tx.note.domain.user.assembler;

import com.tom.springboot.tx.note.appilcation.user.dto.UserDTO;
import com.tom.springboot.tx.note.domain.user.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * @author Tom
 * @version 1.0.0
 * @ClassName UserAssembler.java
 * @Description TODO
 * @createTime 2025年03月04日 22:14:00
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserAssembler {

    UserEntity toEntity(UserDTO userDTO);

    List<UserEntity> toEntityList(List<UserDTO> userDTOList);
}
