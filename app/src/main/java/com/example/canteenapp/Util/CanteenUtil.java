package com.example.canteenapp.Util;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class CanteenUtil {


  public static String ConvertMilliSecondsToFormattedDate(long milliSeconds) {
    String dateFormat = "dd-MM-yyyy hh:mm";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(milliSeconds);
    return simpleDateFormat.format(calendar.getTime());
  }


  public static int getYearFromMilliSecond(long milliSeconds) {
    Calendar c = Calendar.getInstance();
    c.setTimeInMillis(milliSeconds);
    return c.get(Calendar.YEAR);
  }

  public static String getYearAndDayFromMilliSecond(long milliSeconds) {
    Calendar c = Calendar.getInstance();
    c.setTimeInMillis(milliSeconds);
    return c.get(Calendar.YEAR) + " " + c.get(Calendar.DAY_OF_YEAR);
  }


  public static String ConvertMilliSecondsToPrettyTime(long milliSeconds) {
    PrettyTime p = new PrettyTime();
    return p.format(new Date(milliSeconds));
  }


//  public static void sendMail(String message, String TO, String subject) {
//    try {
//      Email email = new SimpleEmail();
//      email.setHostName(GOOGLE_SMTP_URL);
//      email.setSmtpPort(GOOGLE_SMTP_PORT);
//      email.setAuthenticator(new DefaultAuthenticator(USERNAME, PASSWORD));
//      email.setSSLOnConnect(true);
//      email.setFrom(FROM);
//      email.setSubject(subject);
//      email.setMsg(message);
//      email.addTo(TO);
//      email.send();
//    } catch (Exception e) {
//    }
//
//  }

  public static boolean isInBetweenTwoDate(long startDate, long endDate, long dateToCompare) {
    if (dateToCompare >= startDate && dateToCompare <= endDate) {
      return true;
    }
    if (getYearAndDayFromMilliSecond(dateToCompare).equals(getYearAndDayFromMilliSecond(System.currentTimeMillis()))) {
      return true;
    }
    return false;
  }


  /**
   * This is the new section of util for admin algorithm here date is check for certain condition
   */
  public static boolean isThisDay(long time) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(time);
    Calendar calendarToday = Calendar.getInstance();
    calendarToday.setTimeInMillis(System.currentTimeMillis());
    if ((calendarToday.get(Calendar.YEAR) + " " + calendarToday.get(Calendar.DAY_OF_YEAR)).equals(calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.DAY_OF_YEAR))) {
      return true;
    }
    return false;
  }

  public static boolean isThisWeek(long time) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    calendar.setFirstDayOfWeek(Calendar.SUNDAY);
    int current_year = calendar.get(Calendar.YEAR);
    int current_week_number = calendar.get(Calendar.WEEK_OF_YEAR);
    calendar.setTimeInMillis(time);
    int year = calendar.get(Calendar.YEAR);
    int week_number = calendar.get(Calendar.WEEK_OF_YEAR);
    if (current_year == year && current_week_number == week_number) {
      return true;
    } else {
      return false;
    }
  }

  public static boolean isThisMonth(long time) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    int current_year = calendar.get(Calendar.YEAR);
    int current_month_number = calendar.get(Calendar.MONTH);
    calendar.setTimeInMillis(time);
    int year = calendar.get(Calendar.YEAR);
    int month_number = calendar.get(Calendar.MONTH);
    if (month_number == current_month_number && year == current_year) {
      return true;
    }
    return false;
  }

  public static boolean isThisYear(long time) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    int current_year = calendar.get(Calendar.YEAR);
    calendar.setTimeInMillis(time);
    int year = calendar.get(Calendar.YEAR);
    if (year == current_year) {
      return true;
    }
    return false;
  }

  public static boolean isThisSpring(long time) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    int current_year = calendar.get(Calendar.YEAR);
    calendar.setTimeInMillis(time);
    int year = calendar.get(Calendar.YEAR);
    int month_number = calendar.get(Calendar.MONTH);
    if (year == current_year && (month_number == 2 || month_number == 3 || month_number == 4)) {
      return true;
    }
    return false;
  }

  public static boolean isThisSummer(long time) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    int current_year = calendar.get(Calendar.YEAR);
    calendar.setTimeInMillis(time);
    int year = calendar.get(Calendar.YEAR);
    int month_number = calendar.get(Calendar.MONTH);
    if (year == current_year && (month_number == 5 || month_number == 6 || month_number == 7)) {
      return true;
    }
    return false;
  }

  public static boolean isThisFall(long time) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    int current_year = calendar.get(Calendar.YEAR);
    calendar.setTimeInMillis(time);
    int year = calendar.get(Calendar.YEAR);
    int month_number = calendar.get(Calendar.MONTH);
    if (year == current_year && (month_number == 8 || month_number == 9 || month_number == 10)) {
      return true;
    }
    return false;
  }

  public static boolean isThisWinter(long time) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    int current_year = calendar.get(Calendar.YEAR);
    calendar.setTimeInMillis(time);
    int year = calendar.get(Calendar.YEAR);
    int month_number = calendar.get(Calendar.MONTH);
    if (year == current_year && (month_number == 0 || month_number == 1 || month_number == 11)) {
      return true;
    }
    return false;
  }


}