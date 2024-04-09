package com.alan.user.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

@Component
public class UserUtils {

    private static final Logger logger = LoggerFactory.getLogger(UserUtils.class.getName());

    public String base64Encode(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    public String base64Decode(String text) {
        return new String(Base64.getDecoder().decode(text));
    }

    public long getDateAsLong() {
        return getDateAsLong(new Date());
    }

    public long getDateAsLong(Date date) {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        return Long.parseLong(df.format(date));
    }

    public long stringDateToLong(String date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return getDateAsLong(df.parse(date));
        } catch (ParseException pe) {
            logger.error("Error occurred while ");
            return 0;
        }
    }

}
