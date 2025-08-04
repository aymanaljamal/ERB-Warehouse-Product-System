package com.erb.demo.companyProject;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;

public class TimeIntervalHelper {

    public static Timestamp parseUtcTimestamp(String utcDateTime) {
        ZonedDateTime zdt = ZonedDateTime.parse(utcDateTime);
        return Timestamp.from(zdt.toInstant());
    }
}
