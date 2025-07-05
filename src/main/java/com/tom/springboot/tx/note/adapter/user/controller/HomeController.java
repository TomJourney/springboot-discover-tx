package com.tom.springboot.tx.note.adapter.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Tom
 * @version 1.0.0
 * @ClassName HomeController.java
 * @Description TODO
 * @createTime 2025年04月05日 21:28:00
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "forward:index.html";
    }
}
