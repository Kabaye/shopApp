package com.netcracker.edu.kulich.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

@Component
public class DefaultLoggingAnnotationHandlerBeanPostProcessor implements BeanPostProcessor {
    private final static Logger logger = LoggerFactory.getLogger(DefaultLoggingAnnotationHandlerBeanPostProcessor.class);
    private Map<String, Class> classMap = new HashMap<>();
    private Map<String, Map<String, Method>> methodsMap = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(DefaultLogging.class)) {
            classMap.put(beanName, beanClass);
            Method[] methods = beanClass.getMethods();
            Map<String, Method> smMap = new HashMap<>();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Logging.class)) {
                    smMap.put(method.getName(), method);
                }
            }
            methodsMap.put(beanName, smMap);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = classMap.get(beanName);
        if (beanClass != null) {
            if (beanClass.getInterfaces().length != 0) {
                logger.info("JDK Dynamic Proxy used to replace bean: \"" + beanClass.getName() + "\".");
                return Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(), (proxy, method, args) -> {
                    Method m = methodsMap.get(beanName).get(method.getName());
                    if (m != null) {
                        Logging annotation = m.getAnnotation(Logging.class);
                        logMessage(beanClass.getSimpleName() + ": \"" + annotation.startMessage() + "\"", annotation.level(),
                                annotation.startFromNewLine());
                        Object retVal;
                        try {
                            retVal = method.invoke(bean, args);
                        } catch (InvocationTargetException ite) {
                            throw ite.getCause();
                        }
                        logMessage(beanClass.getSimpleName() + ": \"" + annotation.endMessage() + "\"", annotation.level(),
                                false);
                        return retVal;
                    }
                    return method.invoke(bean, args);
                });
            } else {
                logger.info("CGLib (Subclass) used to replace bean: \"" + beanClass.getName() + "\".");
                Enhancer enhancer = new Enhancer();
                enhancer.setSuperclass(beanClass);
                enhancer.setInterfaces(beanClass.getInterfaces());
                enhancer.setCallback((MethodInterceptor) (o, method, objects, methodProxy) -> {
                    Method m = methodsMap.get(beanName).get(method.getName());
                    if (m != null) {
                        Logging annotation = m.getAnnotation(Logging.class);
                        logMessage(beanClass.getSimpleName() + ": \"" + annotation.startMessage() + "\"", annotation.level(),
                                annotation.startFromNewLine());
                        Object retVal;
                        try {
                            retVal = methodProxy.invokeSuper(o, objects);
                        } catch (InvocationTargetException ite) {
                            logMessage(beanClass.getSimpleName() + ": \"" + annotation.endMessage() + "\"", annotation.level(),
                                    false);
                            return ite.getCause();
                        }
                        logMessage(beanClass.getSimpleName() + ": \"" + annotation.endMessage() + "\"", annotation.level(),
                                false);
                        return retVal;
                    }
                    return methodProxy.invokeSuper(o, objects);
                });
                Object proxy = enhancer.create();
                Field[] fields = beanClass.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    Object objectToCopy = ReflectionUtils.getField(field, bean);
                    ReflectionUtils.setField(field, proxy, objectToCopy);
                }
                return proxy;
            }
        }
        return bean;
    }

    private void logMessage(String message, LogLevel level, boolean startFromNewLine) {
        if (startFromNewLine) {
            logger.info("");
        }
        switch (level) {
            case WARN:
                logger.warn(message);
                break;
            case DEBUG:
                logger.debug(message);
                break;
            case ERROR:
                logger.error(message);
                break;
            case FATAL:
                logger.error("FATAL ERROR! message: " + message);
                break;
            default:
                logger.info(message);
                break;
        }
    }
}
