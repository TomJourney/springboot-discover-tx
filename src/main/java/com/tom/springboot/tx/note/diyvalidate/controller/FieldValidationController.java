package com.tom.springboot.tx.note.diyvalidate.controller;

import com.tom.springboot.tx.note.diyvalidate.application.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tom
 * @version 1.0.0
 * @Description TODO
 * @createTime 2025年12月28日 21:04:00
 */
@RestController
@RequestMapping("/field-valid")
@Slf4j
public class FieldValidationController {

    @PostMapping(path = "/register", consumes = "application/json;charset=UTF-8")
    public void register(@RequestBody UserDTO userDTO) {
        log.info("userDTO = {}", userDTO);
    }
}
