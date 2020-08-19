package com.walter.util;

public class SequenceGenerator {
    private static final Snowflake snowflake = new Snowflake();

    private SequenceGenerator() {
    }

    public static Long nextSequence() {
        return snowflake.nextId();
    }

    public static String nextSequence(String prefix) {
        StringBuilder sb = new StringBuilder(prefix);
        sb.append(String.valueOf(snowflake.nextId()));
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(nextSequence());
    }
}
