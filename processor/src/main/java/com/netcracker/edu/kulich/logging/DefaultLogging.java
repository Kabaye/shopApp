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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that an annotated class has default logging system.
 * This means, that each method of class, annotated with {@link Logging @Logging},
 * will log some information before and after method invoking.
 *
 * @author Kulich Svyatoslav
 * @see Logging
 * @since 5.1.6
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DefaultLogging {
}
