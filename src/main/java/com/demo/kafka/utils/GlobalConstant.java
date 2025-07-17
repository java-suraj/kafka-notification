package com.demo.kafka.utils;

import java.util.concurrent.atomic.AtomicBoolean;

public final class GlobalConstant {

    private GlobalConstant() {
        // Prevent instantiation
    }

    public static final AtomicBoolean KAFKA_ENABLED = new AtomicBoolean(true);
    public static final String TOPIC_NAME = "notifications";
    private static final String NOTIFICATION_WINDOW_DAYS = "7";
    private static final String NOTIFICATION_GROUP = "notifications";
    private static final String NOTIFICATION_BOOTSTRAP_SERVER = "localhost:9092";

    public static boolean isKafkaEnabled() {
        return KAFKA_ENABLED.get();
    }

    public static String getBootstrapServer() {
        return NOTIFICATION_BOOTSTRAP_SERVER;
    }

    public static String getGroup() {
        return NOTIFICATION_GROUP;
    }

    public static String getWindowDays() {
        return NOTIFICATION_WINDOW_DAYS;
    }

}
