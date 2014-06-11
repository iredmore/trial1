package com.ecow.core.helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * methods for sorting out dates
 *
 * @author Toby Mottram
 */
public class DateProcess {

    /**
     * 14/07/2010 , 11:10:20 , 6.35 , 34.6 , 3.6 , 203 14/07/2010 , 11:15:20 ,
     * 6.38 , 38.1 , 3.6 , 203 14/07/2010 , 11:20:20 , 6.38 , 38.9 , 3.6 , 203
     */
    public static final String DATE_FORMAT_NOW = "yyyyMMdd";
    public static final String TIME_FORMAT_NOW = "HHmmss";
    public static final int MINIMUM_LINE_LENGTH = 4;  //used to set the valid line length
    public static String DATE_FORMAT_STN;

    public static String convertDateToString(Date inDate) {
        
        SimpleDateFormat pHFormat = new SimpleDateFormat(" dd/MM/yyyy , HH:mm:ss ");
        StringBuilder datepHFormat = new StringBuilder(pHFormat.format(inDate));
        
        return datepHFormat.toString();
    }

    public static String convertLongtoString(long inDate, String dFormat) {
        
        SimpleDateFormat pHFormat = new SimpleDateFormat(dFormat);
        StringBuilder datepHFormat = new StringBuilder(pHFormat.format(inDate));
        
        return datepHFormat.toString();
    }

    public static String convertDateToShortString(Date inDate) {
        
        SimpleDateFormat pHFormat = new SimpleDateFormat(" dd/MM/yyyy ");
        StringBuilder datepHFormat = new StringBuilder(pHFormat.format(inDate));
        
        return datepHFormat.toString();
    }

    public static String extractTimefromDate(Date inDate) {
        
        SimpleDateFormat pHFormat = new SimpleDateFormat("HH:mm:ss");
        StringBuilder timepHFormat = new StringBuilder(pHFormat.format(inDate));
        
        return timepHFormat.toString();
    }

    public static Calendar parseDateStamp(String timestamp) throws Exception {
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH);
        Date d = sdf.parse(timestamp);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        
        return cal;
    }

    public static Calendar parseTimestamp(String timestamp) throws Exception {
        
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        Date d = sdf.parse(timestamp);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        
        return cal;
    }

    /**
     * uses the date format found in the bolus .csv files
     */
    public static Date parsepHTimestamp(String timestamp) throws Exception {
        
        SimpleDateFormat sdf = new SimpleDateFormat(" dd/MM/yyyy , HH:mm:ss ", Locale.ENGLISH);
        Date d = sdf.parse(timestamp);
        //Calendar cal = Calendar.getInstance();
        //cal.setTime(d);
        
        return d;
    }

    /**
     * use this to add the extra days
     */
    public static Date addDays(String str_date, int days) {
        
        Date date = null;
        
        try {
            DateFormat formatter;
            formatter = new SimpleDateFormat("dd/MM/yyyy");
            date = (Date) formatter.parse(str_date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, days);  // number of days to add
            date = cal.getTime();  // date is now the new date
            //System.out.println("Today is " +date );
        } 
        catch (ParseException e) {
            System.out.println("Exception :" + e);
        }
        
        return date;
    }

    /**
     * use this to add extra minutes to a time line, use this to fill gaps in
     * files
     */
    public static Date addMinutes(String str_date, int mins) {
        
        Date date = null;
        
        try {
            DateFormat formatter;
            formatter = new SimpleDateFormat("HH:mm:ss");
            date = (Date) formatter.parse(str_date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MINUTE, mins);  // number of days to add
            date = cal.getTime();  // date is now the new date
            //System.out.println("Today is " +date );
        } 
        catch (ParseException e) {
            System.out.println("Exception :" + e);
        }
        
        return date;
    }

    /**
     * use this to add the extra secs
     */
    public static Date addSecs(String str_date, int secs) {
        
        Date date = null;
        
        try {
            DateFormat formatter;
            formatter = new SimpleDateFormat(" hh:mm:SS ");
            date = (Date) formatter.parse(str_date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MILLISECOND, secs * 1000);  // number of mSecs to add
            date = cal.getTime();  // date is now the new date
            //System.out.println("Today is " +date );
        } 
        catch (ParseException e) {
            System.out.println("Exception :" + e);
        }
        
        return date;
    }

    /**
     * returns the instant time now
     */
    public static String now(String dateFt) {
        
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFt);
        
        return sdf.format(cal.getTime());

    }

    /**
     * returns the long relating to the inputDate
     */
    public static long getLongofDate(String str_date, String dFormat) {
        
        long lDate = 0;
        
        try {
            DateFormat formatter;
            Date date;
            formatter = new SimpleDateFormat(dFormat);
            date = (Date) formatter.parse(str_date.trim());
            lDate = date.getTime();
            // System.out.println(str_date+" is " +lDate );
        } 
        catch (ParseException e) {
            System.out.println("  " + str_date + " Exception :" + e);
            lDate = 0;
        }
        
        return lDate;
    }

    /**
     * to get the date sent by the bolus
     */
    public static long getDaysSince(String str_date, String dFormat) {
        
        long now = Calendar.getInstance().getTimeInMillis();
        
        return (now - getLongofDate(str_date, dFormat)) / 86400000;
    }

    /**
     * returns the time in seconds since the input date
     */
    public static long getSecsSince(String str_date, String dFormat) {
        
        long now = Calendar.getInstance().getTimeInMillis();
        
        return (now - getLongofDate(str_date, dFormat)) / 1000;
    }

    /**
     * returns the time in minutes since the input date
     */
    public static long getMinutes(String str_date, String dFormat) {
        
        long now = Calendar.getInstance().getTimeInMillis();
        
        return (now - getLongofDate(str_date, dFormat)) / (60 * 1000);
    }

    /**
     * returns the time in minutes since the input date
     */
    public static long getMinutesBetween(String str_date1, String str_date2, String dFormat) {
        
        return ((getLongofDate(str_date2, dFormat)) - getLongofDate(str_date1, dFormat)) / (60 * 1000);
    }

    /**
     * returns the number of quarters since the input date time link this to
     * midnight of each day
     */
    public static long getQuarter(String str_date, String dFormat) {
        
        long now = Calendar.getInstance().getTimeInMillis();
        
        return (now - getLongofDate(str_date, dFormat)) / (15 * 60 * 1000);
    }

    /**
     * tidies up white space to a standardised format
     */
    public static String tidyDates(String inputDate) {
        
        // System.out.println(inputDate);
        inputDate = inputDate.trim();
        inputDate = inputDate.substring(0, 10) + " " + inputDate.substring(inputDate.length() - 8, inputDate.length());
        //  System.out.println(inputDate);
        
        return inputDate;
    }

    /**
     * returns the number of quarters since the input date time str_date
     * formatted to time to a quarter hour public static String
     * getDaysQuarter(String str_midnight,String midNghtFormat, String
     * str_date,String dFormat) {
     *
     * // this should return a number in      *
     * long quarters = (getLongofDate(tidyDates(
     * str_date),dFormat)-getLongofDate(tidyDates(
     * str_midnight),dFormat))/(15*60*1000); //System.out.println("quarters =
     * "+quarters); int hours = (int) quarters/4; int mins = (int)
     * ((quarters%4)*15); //if (hours == 24){hours = 0;} String hrString =
     * String.format("%02d", hours); String minString = String.format("%02d",
     * mins); str_midnight = str_midnight.substring(0, 13); //selects the date
     * return str_midnight+" , "+hrString+":"+minString+":00 "; }
     *
     * /** returns the next quarter hour after the input date time str_date
     * formatted to time to a quarter hour public static String
     * getDaysQuarter2(String str_midnight,String midNghtFormat, String
     * str_date,String dFormat) {
     *
     * long testNum = (getLongofDate(tidyDates(
     * str_date),dFormat)-getLongofDate(tidyDates( str_midnight),dFormat)); if
     * (testNum>=0) { System.out.println(str_date + " = diff in mS from midnight
     * "+testNum);} long quarters = testNum/(15*60*1000);
     * System.out.println("quarters = "+quarters); int hours = (int) quarters/4;
     * int mins = (int) ((quarters%4)*15); //if (hours == 24){hours = 0;} String
     * hrString = String.format("%02d", hours); String minString =
     * String.format("%02d", mins); str_midnight = str_midnight.substring(0,
     * 13); //selects the date return str_midnight+" ,
     * "+hrString+":"+minString+":00 "; }
     *
     *
     * /** a generic way of finding the nearest quarter of an hour
     */
    public static String getNextQuarter(String str_midnight, String midNghtFormat, String str_date, String dFormat) {
        
        long testNum = (getLongofDate(tidyDates(str_date), dFormat) - getLongofDate(tidyDates(str_midnight), dFormat));  // time in mS 
        long quarters = testNum / (15 * 60 * 1000);   // number of quarter hours since midnight 
        testNum = (testNum) / 1000;  // get rid of mS for the maths

        //  Now determine the time to the nearest quarter 
        long diff = (testNum % (900));
        // System.out.println(testNum+"   remainder = "+diff+ " ");
        int remainderSeeker = 1;
        if (diff >= (450)) {
            remainderSeeker = 1;
            quarters = quarters + 1;
        } //advance the quarter hour 
        else {
            remainderSeeker = -1;
        }
        while ((testNum % (900)) != 0) {
            testNum = testNum + remainderSeeker;
            // System.out.println(testNum + " remainder "+(testNum % (900)));  
        }
        //System.out.println("quarters = "+quarters);
        int hours = (int) quarters / 4;
        int mins = (int) ((quarters % 4) * 15);
        if (hours == 24) // rollover the day
        {
            hours = 0;
            str_midnight = convertDateToString(addDays(str_midnight, 1)).substring(0, 12) + "  ";
        }
        String hrString = String.format("%02d", hours);
        String minString = String.format("%02d", mins);
        str_midnight = str_midnight.trim();          //tidy up space
        str_midnight = str_midnight.substring(0, 10);  //selcts the date element
        
        return str_midnight + " , " + hrString + ":" + minString + ":00 ";  // str_date + " = "+
    }

    /**
     * returns the time in hours since the input date
     */
    public static long getHours(String str_date, String dFormat) {
        
        long now = Calendar.getInstance().getTimeInMillis();
        
        return (now - getLongofDate(str_date, dFormat)) / (60 * 1000);
    }

    /**
     * 
     * @param date
     * @param DATE_FORMAT_STN
     * @param daysLeft
     * @return 
     */
    public static Date addDays(String date, String DATE_FORMAT_STN, int daysLeft) {
        
        throw new UnsupportedOperationException("Not yet implemented");
    }

    
    public static void main(String[] args) throws Exception {

        //System.out.println(getLongofDate("06/05/2013 , 22:01:10", "dd/MM/yyyy , hh:mm:ss"));
        //System.out.println(getLongofDate("22:01:10", "hh:mm:ss"));
        //System.out.println(getMinutesBetween("06/05/2013 , 22:01:10", "07/05/2013 , 22:02:10","dd/MM/yyyy , hh:mm:ss"));
        System.out.println(convertLongtoString(123400000, " dd/MM/yyyy HH:mm:ss "));
        System.out.println(getNextQuarter("   05/12/2012  " + " 00:00:00 ", tidyDates(" dd/MM/yyyy HH:mm:SS "), "05/12/2012    11:33:02  ", tidyDates(" dd/MM/yyyy HH:mm:SS ")));
    }
}
/**
 * //tidyDates(" 05/12/2012 , 00:00:00 "); //tidyDates(" 05/12/2012 00:00:00 ");
 * //tidyDates(" dd/MM/yyyy HH:mm:SS");
 *
 * // System.out.println(getDaysQuarter2(" 05/12/2012 "+" 00:00:00 ",tidyDates("
 * dd/MM/yyyy HH:mm:SS "), "05/12/2012 01:25:00 ",tidyDates(" dd/MM/yyyy
 * HH:mm:SS " ))); // System.out.println(getDaysQuarter2(" 05/12/2012 "+"
 * 00:00:00 ",tidyDates(" dd/MM/yyyy HH:mm:SS "), "05/12/2012 01:35:00
 * ",tidyDates(" dd/MM/yyyy HH:mm:SS " ))); /*
 * System.out.println(getLongofDate(" 07/12/2012 00:00:00 "," dd/MM/yyyy
 * HH:mm:SS ")); System.out.println(getDaysQuarter(" 05/12/2012 00:00:00 ","
 * dd/MM/yyyy HH:mm:SS "," 05/12/2012 00:00:15 "," dd/MM/yyyy HH:mm:SS "));
 * System.out.println(getDaysQuarter(" 05/12/2012 00:00:00 "," dd/MM/yyyy
 * HH:mm:SS "," 05/12/2012 07:18:15 "," dd/MM/yyyy HH:mm:SS "));
 * System.out.println(getDaysQuarter(" 05/12/2012 00:00:00 "," dd/MM/yyyy
 * HH:mm:SS "," 05/12/2012 11:18:15 "," dd/MM/yyyy HH:mm:SS "));
 * System.out.println(getDaysQuarter(" 05/12/2012 00:00:00 "," dd/MM/yyyy
 * HH:mm:SS "," 05/12/2012 23:47:15 "," dd/MM/yyyy HH:mm:SS "));
 * System.out.println(getDaysQuarter(" 05/12/2012 00:00:00 "," dd/MM/yyyy
 * HH:mm:SS "," 05/12/2012 07:57:15 "," dd/MM/yyyy HH:mm:SS "));
 */
//System.out.println("minutes since midnight "+ getMinutes("25/08/2013 00:00:00 ","dd/MM/yyyy hh:mm:SS"));
//System.out.println("Quarters since midnight "+ getMinutes("25/08/2013 10:00:00 ","dd/MM/yyyy hh:mm:SS")/15);
//System.out.println("Seconds = "+getSecsSince("01/01/2010 11:20:20 ","dd/MM/yyyy hh:mm:SS"));
//System.out.println("Midnight = "+now("dd/MM/yyy 11:20:20 "));
 // System.out.println("Seconds since midnight = "+getSecsSince(now("dd/MM/yyy, hh:mm:SS"),"dd/MM/yyyy, hh:mm:SS"));  */
