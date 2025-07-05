package com.tom.springboot.tx.note.adapter.user.controller;

import com.tom.springboot.tx.note.appilcation.user.dto.UserDTO;
import com.tom.springboot.tx.note.appilcation.user.service.UserAppService;
import com.tom.springboot.tx.note.domain.user.assembler.UserAssembler;
import com.tom.springboot.tx.note.infrastructure.dao.user.mapper.UserPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Tom
 * @version 1.0.0
 * @ClassName SpringbootRedisController.java
 * @Description TODO
 * @createTime 2024年12月01日 16:18:00
 */
@RestController
@RequestMapping("/springboot-tx")
public class SpringbootTxNoteController {

    @Autowired
    private UserAppService userAppService;

    @Autowired
    private UserAssembler userAssembler;

    @GetMapping("/user/{id}")
    public UserPO findUserById(@PathVariable("id") String id) {
        return userAppService.findUserById(id);
    }

    @PostMapping(path = "/add-user", consumes = "application/json")
    public void addUser(@RequestBody UserDTO userDTO) {
        userAppService.saveNewUser(userAssembler.toEntity(userDTO));
    }
}
