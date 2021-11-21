package ru.kkrukhmalev.profiler;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class ProfileAspect {
    @Around("execution(* *(..)) && !within(ru.kkrukhmalev.profiler.*)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Profiler profiler = Profiler.getInstance();
        long start = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            profiler.process((MethodSignature) joinPoint.getSignature(), System.currentTimeMillis() - start);
        }
    }
}
