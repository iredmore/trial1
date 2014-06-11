/*
 * (c) eCow Ltd 2013.  This software and the intellectual property in it is
 * proprietary to eCow Ltd and/or its licensors.  Your use of it is subject
 * to the terms and restrictions set out in the contract under which it was
 * supplied. You must not use it for any other purpose without eCow's prior
 * written permission and in particular, but without limitation, you must not
 * copy, reverse engineer or decompile this software nor permit or purport to
 * permit any third party to do (other than and to the extent the same cannot
 * be prohibited by law).
 */
package com.ecow.core.helper;

/**
 * A set of <code>Constants</code> for use in the utility and in Hathor
 *
 * @author Toby Mottram December 2012
 */
public class Constants {

    public static final int METADATA_LINES = 26;   // lines at the head of a file
    public static final int MAXIMUM_DAYS = 200;   // the life of a bolus
    public static final double MINIMUM_pH = 4.80;  // to assist in graphing
    public static final double PREDICTED_LOW_pH = 5.50;
    public static final double MAX_pH = 7.00;
    public static final double MAX_REALpH = 7.50;
    public static final int pH_UNIT = 100; //to define the vertical grid multiple of 0.1 pH
    public static final int BORDER_WIDTH = 60;
    public static final int charWidth = 10; //to set width of showdata
    public static final String FILE_DATE_FORMAT_NOW = "yyyyMMdd";  //for creating dates
    public static final String FILE_TIME_FORMAT_NOW = "kkmmss";
    public static final int MAXPOINTS = 100000;
    public static final String path1 = "C:\\bolus\\data\\";
    public static final int MAX_pH_DAYS = 10000; // two years = 96x365 x 2

    /**
     * the height of the X axis can be scaled from the values expected
     */
    public double getRange() {
        
        return MAX_pH - PREDICTED_LOW_pH;
    }
    
    /**
     * files for icons
     */
    public final static String SOUND_PATH = "sounds/button.wav";
    public final static String IMAGE_PATH_ABOUT = "icons/about ecow b.jpg";
    public final static String IMAGE_PATH_LOGO = "icons/GreenIcon.jpg";
    public final static String IMAGE_PATH_CLEAN = "icons/clean and cat b.jpg";
    public final static String IMAGE_PATH_META = "icons/editmetadatab.jpg";
    public final static String IMAGE_PATH_CONFIG = "icons/configa.gif";
    public final static String IMAGE_PATH_CALIBRATE = "icons/calibrate.jpg";
    public final static String IMAGE_PATH_GRAPH = "icons/show graph b.jpg";
    public final static String IMAGE_PATH_FWD = "icons/show graph b.jpg";
    public final static String IMAGE_PATH_TEST = "icons/Tests.png";
    
    /**
     * columns in an ecow data csv file parameters
     */
    public static final int MINIMUM_LINE_LENGTH = 4;  //used to set the valid line length 
    public static final String SEPARATOR = ",";
    public static final int PH_COLUMN = 2;
    public static final int VOLT_COLUMN = 5;
    public final static int _DATE = 0;
    public final static int _TIME = 1;
    public final static int _PH = 2;
    public final static int REDOX = 3;
    public final static int TEMP = 4;
    public final static int BATTERY = 5;
    public final static int CODE = 6;
    
    /**
     * columns in an ecow data file name
     */
    public final static String fNAME_SEP = "_";
    public final static int farm = 0;
    public final static int type = 1;
    public final static int ID = 2;
    public final static int date = 3;
}
