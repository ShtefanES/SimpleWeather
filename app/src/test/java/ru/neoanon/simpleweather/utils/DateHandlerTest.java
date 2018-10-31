package ru.neoanon.simpleweather.utils;

import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import ru.neoanon.simpleweather.model.DateInfo;

import static org.junit.Assert.*;

/**
 * Created by eshtefan on  24.10.2018.
 */

public class DateHandlerTest {

    private DateHandler dateHandler;
    private long currentTimestamp;
    private long timestamp = 1539165600000L;//(GMT): Wednesday, 10 October 2018 г., 10:00:00
    private long timestampToCheck;//timestamp +/- your offset
    private long timestampWeekend = 1540036800000L;//(GMT): Saturday, 20 October 2018 г., 12:00:00
    private long timestampWeekendToCheck;//timestamp +/- your offset

    @Before
    public void initDateHandler() {
        Locale locale = new Locale("en");
        dateHandler = new DateHandler(locale);
    }

    @Before
    public void initCurrentTimestamp() {
        currentTimestamp = Calendar.getInstance().getTimeInMillis();
    }

    @Before
    public void initTimestampToCheck() {
        TimeZone tz = TimeZone.getDefault();
        int offset = tz.getOffset(timestamp);
        timestampToCheck = timestamp - offset;
    }

    @Before
    public void initTimestampWeekendToCheck() {
        TimeZone tz = TimeZone.getDefault();
        int offset = tz.getOffset(timestampWeekend);
        timestampWeekendToCheck = timestampWeekend - offset;
    }

    @Test
    public void getCurrentHour() {
        Date currentDate = new Date(currentTimestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("H");
        String currentHourStr = sdf.format(currentDate);
        int expectedHour = Integer.valueOf(currentHourStr);
        int actualHour = dateHandler.getCurrentHour();
        assertEquals(expectedHour, actualHour);
    }

    @Test
    public void getLocalOffset() {
        TimeZone tz = TimeZone.getDefault();
        int expectedOffset = tz.getOffset(currentTimestamp) / 1000 / 3600;
        int actualOffset = dateHandler.getLocalOffset();
        assertEquals(expectedOffset, actualOffset);
    }

    @Test
    public void getHoursAndMinutes() {
        String expectedString = "10:00";
        String actualString = dateHandler.getHoursAndMinutes(timestampToCheck);
        assertEquals(expectedString, actualString);
    }

    @Test
    public void getDataInfo() {
        String expectedDayOfWeek = "Wednesday";
        String expectedDayOfMonth = "10 October";
        boolean expectedIsWeekend = false;
        DateInfo dateInfo = dateHandler.getDataInfo(timestampToCheck);
        String actualDayOfWeek = dateInfo.getDayOfWeek();
        String actualDayOfMonth = dateInfo.getDayOfMonth();
        boolean actualIsWeekend = dateInfo.isWeekend();
        assertEquals(expectedDayOfWeek, actualDayOfWeek);
        assertEquals(expectedDayOfMonth, actualDayOfMonth);
        assertEquals(expectedIsWeekend, actualIsWeekend);
    }

    @Test
    public void getDataInfoWithWeekend() {
        String expectedDayOfWeek = "Saturday";
        String expectedDayOfMonth = "20 October";
        boolean expectedIsWeekend = true;
        DateInfo dateInfo = dateHandler.getDataInfo(timestampWeekendToCheck);
        String actualDayOfWeek = dateInfo.getDayOfWeek();
        String actualDayOfMonth = dateInfo.getDayOfMonth();
        boolean actualIsWeekend = dateInfo.isWeekend();
        assertEquals(expectedDayOfWeek, actualDayOfWeek);
        assertEquals(expectedDayOfMonth, actualDayOfMonth);
        assertEquals(expectedIsWeekend, actualIsWeekend);
    }
}