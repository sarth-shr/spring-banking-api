package com.example.foneproject.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("within(com.example.foneproject.scheduling..*)")
    public void interestPaidScheduler() {
    }

    @After(value = "interestPaidScheduler()")
    public void aroundScheduling() {
        LOGGER.info("Interest paid");
    }

}
