package com.deep.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class UniqueIdGenerator {

    private UniqueIdGenerator() {}

    private static final AtomicInteger increment = new AtomicInteger(0);

    public static Integer generateId() {
        return increment.getAndIncrement();
    }
}
