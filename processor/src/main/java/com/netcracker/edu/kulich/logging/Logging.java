/*
 * Copyright (c) 2019, Kabaye INC. and/or its affiliates. All rights reserved.
 * KABAYE INC. PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package com.netcracker.edu.kulich.logging;

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
 * @author Kulich Svyatoslav
 * @see DefaultLogging
 * @since 1.8.0_201
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
