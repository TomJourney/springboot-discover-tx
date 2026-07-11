package com.tom.springboot.tx.note;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Tom
 * @version 1.0.0
 * @ClassName SpringbootRedisApplication.java
 * @Description TODO
 * @createTime 2024年12月01日 16:17:00
 */
//@SpringBootApplication(scanBasePackages = {"com.tom.springboot.tx.note", "com.tom.tx.srccode.analysis"})
@SpringBootApplication(scanBasePackages = {"com.tom.springboot.tx.note"})
@EnableTransactionManagement
@MapperScan("com.tom.springboot.tx.note.infrastructure.dao")
public class SpringbootTxNoteApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootTxNoteApplication.class, args);
    }
}
