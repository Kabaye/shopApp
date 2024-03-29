/*
 * Copyright 2019-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netcracker.edu.kulich.logging.bpp;

import com.netcracker.edu.kulich.logging.annotation.DefaultLogging;
import com.netcracker.edu.kulich.logging.annotation.Logging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

@Component
public class DefaultLoggingAnnotationHandlerBeanPostProcessor implements BeanPostProcessor {
    private static final Logger logger = LoggerFactory.getLogger(Logging.class);
    private Map<String, Class> classMap = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(DefaultLogging.class)) {
            classMap.put(beanName, beanClass);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = classMap.get(beanName);
        if (beanClass != null) {
            if (beanClass.getInterfaces().length != 0) {

                logger.info("JDK Dynamic Proxy used to enhance bean: \"" + beanClass.getName() + "\".");
                return Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(), (proxy /*my proxy*/, method /*origin method*/, args) -> {
                    Method m = beanClass.getMethod(method.getName(), method.getParameterTypes());
                    if (m.isAnnotationPresent(Logging.class)) {
                        Logging annotation = m.getAnnotation(Logging.class);
                        logMessage(beanClass.getSimpleName() + ": " + annotation.startMessage(), annotation.level(),
                                annotation.startFromNewLine());
                        Object retVal;
                        try {
                            retVal = method.invoke(bean, args);
                        } catch (InvocationTargetException ite) {
                            logger.error("Forwarding of exception, thrown by " + method.getName() + "...");
                            throw ite.getCause();
                        }
                        logMessage(beanClass.getSimpleName() + ": " + annotation.endMessage(), annotation.level(),
                                false);
                        return retVal;
                    }
                    return method.invoke(bean, args);
                });
            } else {

                logger.info("SpringCGLib (Subclass) used to enhance bean: \"" + beanClass.getName() + "\".");
                Enhancer enhancer = new Enhancer();
                enhancer.setSuperclass(beanClass);
                enhancer.setInterfaces(beanClass.getInterfaces());
                enhancer.setCallback((MethodInterceptor) (o /*Subclass*/, method /*origin method*/, objects /*args*/, methodProxy /*Subclass method*/) -> {
                    if (method.isAnnotationPresent(Logging.class)) {
                        Logging annotation = method.getAnnotation(Logging.class);
                        logMessage(beanClass.getSimpleName() + ": " + annotation.startMessage(), annotation.level(),
                                annotation.startFromNewLine());
                        Object retVal = methodProxy.invokeSuper(o, objects);
                        logMessage(beanClass.getSimpleName() + ": " + annotation.endMessage(), annotation.level(),
                                false);
                        return retVal;
                    }
                    return methodProxy.invokeSuper(o, objects);
                });
                Object proxy;

                Constructor[] constructors = beanClass.getConstructors();
                Class[] parameterTypes = constructors[0].getParameterTypes();
                if (parameterTypes.length == 0) {
                    proxy = enhancer.create();
                } else {
                    proxy = enhancer.create(parameterTypes, new Object[parameterTypes.length]);
                }
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
