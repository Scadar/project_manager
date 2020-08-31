package ru.scadarnull.project_manager.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.scadarnull.project_manager.entity.User;

import java.util.Arrays;

@Aspect
@Component
public class Logging {

    Logger logger = LoggerFactory.getLogger(Logging.class);

    @Around("execution(* ru.scadarnull.project_manager.service.UserService.addUser(..))")
    public Object logAddUser(ProceedingJoinPoint jp) throws Throwable {
        Object o = jp.proceed(jp.getArgs());
        if((boolean) o){
            logger.info("====================================================================");
            logger.info("Метод: " + jp.getSignature().getName());
            User user = (User) jp.getArgs()[0];
            logger.info("Пользователь " + user.getName() + " успешно создан");
        }
        return o;
    }

    @Pointcut("execution(* ru.scadarnull.project_manager.service.ProjectService.*(..))")
    public void logProjectService(){}

    @Before("logProjectService()")
    public void beforeProjectMethods(JoinPoint joinPoint){
        logger.info("====================================================================");
        logger.info("Вызван метод " + joinPoint.getSignature().getName());
        logger.info("С параметрами " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(value = "logProjectService()", returning = "returnValue")
    public void afterProjectMethods(JoinPoint joinPoint, Object returnValue){
        logger.info("====================================================================");
        logger.info("Метод " + joinPoint.getSignature().getName() + " отработал");
        if(returnValue != null){
            logger.info("И вернул " + returnValue.toString());
        }else{
            logger.info("И ничего не вернул");
        }
    }
}
