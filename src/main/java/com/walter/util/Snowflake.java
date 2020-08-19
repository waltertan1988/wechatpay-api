//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.walter.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Snowflake {
    public static final Logger LOGGER = LoggerFactory.getLogger(Snowflake.class);
    private final long workerId;
    private static final long SNSEPOCH = 1330328109047L;
    private static long sequence = 0L;
    private static final long WORKERIDBITS = 10L;
    private static final long maxWorkerId = 1023L;
    private static final long SEQUENCEBITS = 12L;
    private static final long WORKERIDSHIFT = 12L;
    private static final long TIMESTAMPLEFTSHIFT = 24L;
    private static final long sequenceMask = 4095L;
    private long lastTimestamp = -1L;

    public Snowflake() {
        long $workerId = getWorkerId();
        if ($workerId <= 1023L && $workerId >= 0L) {
            this.workerId = $workerId;
        } else {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", 1023L));
        }
    }

    private static long getWorkerId() {
        try {
            return reverseIpToLong(InetAddress.getLocalHost().getHostAddress()) & 1023L;
        } catch (UnknownHostException e) {
            return -1;
        }
    }

    public static long reverseIpToLong(String strIp) {
        long[] ip = new long[4];
        int position1 = strIp.indexOf(".");
        int position2 = strIp.indexOf(".", position1 + 1);
        int position3 = strIp.indexOf(".", position2 + 1);
        ip[0] = Long.parseLong(strIp.substring(0, position1));
        ip[1] = Long.parseLong(strIp.substring(position1 + 1, position2));
        ip[2] = Long.parseLong(strIp.substring(position2 + 1, position3));
        ip[3] = Long.parseLong(strIp.substring(position3 + 1));
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    public synchronized long nextId() {
        long timestamp = this.timeGen();
        if (this.lastTimestamp == timestamp) {
            sequence = sequence + 1L & 4095L;
            if (sequence == 0L) {
                timestamp = this.tilNextMillis(this.lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        if (timestamp < this.lastTimestamp) {
            LOGGER.error(String.format("Clock moved backwards.Refusing to generate id for %d milliseconds", this.lastTimestamp - timestamp));
            throw new RuntimeException(String.format("Clock moved backwards.Refusing to generate id for %d milliseconds", this.lastTimestamp - timestamp));
        } else {
            this.lastTimestamp = timestamp;
            return timestamp - 1330328109047L << 24 | this.workerId << 12 | sequence;
        }
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp;
        for(timestamp = this.timeGen(); timestamp <= lastTimestamp; timestamp = this.timeGen()) {
        }

        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    public static Date parseDate(String id) {
        return parseDate(id, 2);
    }

    public static Date parseDate(String id, int size) {
        String _id = id;
        if (size > 0) {
            _id = id.substring(size);
        }

        _id = Long.toBinaryString(Long.parseLong(_id));
        int len = _id.length();
        Long sequenceStart = (long)len < 12L ? 0L : (long)len - 12L;
        Long timeStart = (long)len < 24L ? 0L : (long)len - 24L;
        String time = timeStart == 0L ? "0" : _id.substring(0, timeStart.intValue());
        long diffTime = Long.parseLong(time, 2);
        long timeLong = diffTime + 1330328109047L;
        return new Date(timeLong);
    }

    public static int parseWorkId(String id) {
        id = Long.toBinaryString(Long.parseLong(id));
        int len = id.length();
        Long sequenceStart = (long)len < 12L ? 0L : (long)len - 12L;
        Long timeStart = (long)len < 24L ? 0L : (long)len - 24L;
        String workerId = sequenceStart == 0L ? "0" : id.substring(timeStart.intValue(), sequenceStart.intValue());
        int workerIdInt = Integer.valueOf(workerId, 2);
        return workerIdInt;
    }
}
