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

package com.netcracker.edu.kulich.logging.annotation;

import org.springframework.boot.logging.LogLevel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that an annotated method will log some information before and after method invoking.
 *
 * <p>Class which has method, annotated with {@link Logging @Component}, must be annotated with {@link DefaultLogging @Component}.
 *
 * @author Kulich Svyatoslav, Vorobei Artsiom
 * @see DefaultLogging
 * @since 5.1.6
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Logging {

    /**
     * Returns information, that must be logged before method invoking.
     *
     * @return information, that must be logged before method invoking
     */
    String startMessage();

    /**
     * Returns information, that must be logged after method invoking.
     *
     * @return information, that must be logged after method invoking
     */
    String endMessage();

    /**
     * (Optional) Returns logging level, with which information will be logged.
     *
     * @return logging level, with which information will be logged
     */
    LogLevel level() default LogLevel.INFO;

    /**
     * (Optional) Whether logger will log from new line, or continue.
     */
    boolean startFromNewLine() default false;
}
