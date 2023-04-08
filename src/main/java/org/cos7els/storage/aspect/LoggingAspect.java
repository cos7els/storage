package org.cos7els.storage.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Around("execution(* org.cos7els.storage.*.*.*(..)) && !@annotation(org.cos7els.storage.aspect.NoLogging)")
    public Object logsAroundMethodsExecution(@NotNull ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().toString();
        logger.info(String.format("Start execution method: %s, with parameters: %s", methodName, Arrays.toString(args)));
        Object proceed = joinPoint.proceed();
        logger.info(String.format("End execution method: %s, with parameters: %s", methodName, Arrays.toString(args)));
        return proceed;
    }
}