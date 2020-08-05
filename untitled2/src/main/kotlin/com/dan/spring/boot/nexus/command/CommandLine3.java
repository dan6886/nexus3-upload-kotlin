package com.dan.spring.boot.nexus.command;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 在系统启动的时候，同时运行run方法里面的内容 order 越小越先执行
 */
@Component
@Order(4)
class CommandLine3 implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("run three");
    }
}

@Component
@Order(2)
class CommandLine2 implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("run two");
    }
}


@Component
@Order(1)
class CommandLine1 implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("run one");
    }
}