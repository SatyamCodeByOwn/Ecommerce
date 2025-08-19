package app.ecom.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Yeh Aspect application ke alag-alag layers (Controller, Service, Repository)
 * par method execution ko log karne ke liye hai.
 * Yeh method ke entry, exit, arguments, return value, aur execution time ko log karta hai.
 */
@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    // ## Pointcuts Definition ##

    /**
     * Ek Pointcut jo Controller layer ke sabhi public methods ko match karta hai.
     */
    @Pointcut("execution(* app.ecom.controller.*.*(..))")
    public void controllerLayerPointcut() {}

    /**
     * Ek Pointcut jo Service layer ke sabhi public methods ko match karta hai.
     */
    @Pointcut("execution(* app.ecom.services.*.*(..))")
    public void serviceLayerPointcut() {}

    /**
     * Ek Pointcut jo Repository layer ke sabhi public methods ko match karta hai.
     */
    @Pointcut("execution(* app.ecom.repositories.*.*(..))")
    public void repositoryLayerPointcut() {}


    // ## Advice Definition ##

    /**
     * Yeh ek Around advice hai jo upar diye gaye sabhi pointcuts par apply hota hai.
     * Yeh method execution ko "wrap" karta hai taaki pehle aur baad mein logging ki ja sake.
     *
     * @param joinPoint method execution ka reference, jisse method ki details milti hain.
     * @return method dwara return kiya gaya original result.
     * @throws Throwable agar original method koi exception throw karta hai.
     */
    @Around("controllerLayerPointcut() || serviceLayerPointcut() || repositoryLayerPointcut()")
    public Object logAroundAllLayers(ProceedingJoinPoint joinPoint) throws Throwable {

        // Method ki jaankari nikalna
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        // 1. Method shuru hone se pehle log karna
        log.info("ENTERING -> {}.{}() | ARGS: {}", className, methodName, Arrays.toString(joinPoint.getArgs()));

        // Execution time track karne ke liye start time record karna
        long startTime = System.currentTimeMillis();

        Object result;
        try {
            // 2. Asli method ko execute karna
            result = joinPoint.proceed();
        } catch (Throwable e) {
            // 3. Agar koi exception aaye to use log karna
            log.error("EXCEPTION in {}.{}() -> {}", className, methodName, e.getMessage());
            throw e; // Exception ko aage pass kar dena
        }

        // Execution time calculate karna
        long executionTime = System.currentTimeMillis() - startTime;

        // 4. Method safalta se khatam hone ke baad log karna
        log.info("EXITING  -> {}.{}() | RESULT: {} | TIME: {} ms", className, methodName, result, executionTime);

        // Asli method ka result return karna
        return result;
    }
}