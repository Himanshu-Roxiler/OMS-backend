package com.roxiler.erp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication()
@EnableJpaRepositories("com.roxiler.erp.repository")
@EntityScan("com.roxiler.erp.model")
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@EnableAspectJAutoProxy
public class ErpApplication {

    public static void main(String[] args) {
        System.out.println("Hello");
        SpringApplication.run(ErpApplication.class, args);
    }

}
