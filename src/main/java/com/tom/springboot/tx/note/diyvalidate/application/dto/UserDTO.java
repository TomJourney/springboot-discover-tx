package com.tom.springboot.tx.note.diyvalidate.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author tom
 * @version 1.0.0
 * @Description 用户DTO
 * @createTime 2025年12月28日 20:58:00
 */
@Data
public class UserDTO {

    @NotBlank(message = "用户名不能为空")
    @Size(min=6, max = 10, message="用户名最小长度为6，最大长度为10")
    private String userName;

    @NotBlank(message = "密码不能为空")
    @Size(min = 8, message = "密码长度不能小于8个字符")
    private String password;

    @Override
    public String toString() {
        return "UserDTO{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
