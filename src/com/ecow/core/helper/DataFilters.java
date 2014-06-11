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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * A suite of filters to stop dead bolus data being added and analysed
 *
 * @author Toby Mottram
 */
public class DataFilters {

    Boolean accept;  // to allow the file through the filter
    Date OKtoHere;  // last date with acceptable data
    String bolusID; // extracted from the file name

    /**
     * Examines the pH values and finds the date when the pH value exceeds the
     * physiological maximum returns the data up to that point
     * 
     * @param inputData pH values to be checked
     * @return array of data up to the point of any detected high
     */
    public static ArrayList<String> detectHigh(ArrayList<String> inputData) {
        
        ArrayList<String> cleanData = new ArrayList<>();
        Iterator<String> listIt = inputData.iterator();
        String tempStr;
        
        while (listIt.hasNext()) {
            tempStr = listIt.next();
            try {
                if (!tempStr.isEmpty()) {
                    double phCheck = Double.parseDouble(ArrayFilter.extractColumn(tempStr, Constants._PH, Constants.SEPARATOR));
                    if (phCheck <= Constants.MAX_REALpH) {
                        cleanData.add(tempStr);
                    }
                    else {
                        break;
                    }
                    /*double pH_Check = Constants.MINIMUM_pH;
                    while (pH_Check <= Constants.MAX_REALpH) {
                        // Get the double value
                        pH_Check = Double.parseDouble(ArrayFilter.extractColumn(tempStr, Constants._PH, Constants.SEPARATOR));
                        cleanData.add(tempStr);
                    }*/  // ends while    
                }
            }
            catch (Exception e) {
                System.out.println("Toby exception thrown at " + tempStr + "  " + e);
            }
        }  //ends while iterator
        
        return cleanData;
    }

    /**
     * Examines the pH values and finds the date when the pH value exceeds the
     * physiological maximum returns the data up to that point
     * 
     * @param inputData pH values to be checked
     * @return array of data up to the point of any detected low
     */
    public static ArrayList<String> detectLow(ArrayList<String> inputData) {
        
        ArrayList<String> cleanData = new ArrayList<>();
        Iterator<String> listIt = inputData.iterator();
        String tempStr;
        
        while (listIt.hasNext()) {
            tempStr = listIt.next();
            try {
                if (!tempStr.isEmpty()) {
                    double phCheck = Double.parseDouble(ArrayFilter.extractColumn(tempStr, Constants._PH, Constants.SEPARATOR));
                    if (phCheck >= Constants.PREDICTED_LOW_pH) {
                        cleanData.add(tempStr);
                    }
                    else {
                        break;
                    }
                    /*double pH_Check = Constants.MAX_REALpH;
                    while (pH_Check >= Constants.PREDICTED_LOW_pH) {
                        // Get the double value
                        pH_Check = Double.parseDouble(ArrayFilter.extractColumn(tempStr, Constants._PH, Constants.SEPARATOR));
                        cleanData.add(tempStr);
                    }*/  // ends while  
                }
            }
            catch (Exception e) {
                System.out.println("Toby exception thrown at " + tempStr + "  " + e);
            }
        }  //ends while iterator 
        
        return cleanData;
    }
    
    
    public static ArrayList<String> detectHighLow(ArrayList<String> inputData, Logger log) {
        
        ArrayList<String> cleanData = new ArrayList<>();
        Iterator<String> listIt = inputData.iterator();
        String tempStr;
        
        while (listIt.hasNext()) {
            tempStr = listIt.next();
            try {
                if (!tempStr.isEmpty()) {
                    double phCheck = Double.parseDouble(ArrayFilter.extractColumn(tempStr, Constants._PH, Constants.SEPARATOR));
                    if (phCheck >= Constants.MINIMUM_pH && phCheck <= Constants.MAX_REALpH) {
                        cleanData.add(tempStr);
                    }
                    else {
                        log.info("pH out of range, truncating data - tempStr="+tempStr);
                        break;
                    }
                }
            }
            catch (Exception ex) {
                log.log(Level.ERROR, "DataFilters.detectHighLow - " + ex.getMessage(), ex);
                log.error("DataFilters.detectHighLow - tempStr=" + tempStr);
            }
        }  //ends while iterator 
        
        return cleanData;
    }
    
    /**
     * Takes a date sorted array and determines the validity of the date range
     * assuming that if it spans more than 2 years it is invalid.
     * 
     * @param inputData 
     */
    public static boolean dateRangeValid(ArrayList<String> inputData, Logger log) {
        
        boolean retVal = true;
        String startDateStr = "";
        String endDateStr = "";
        
        try {
            startDateStr = ArrayFilter.extractColumn(inputData.get(0), 0, ",") + ","
                    + ArrayFilter.extractColumn(inputData.get(0), 1, ",");
            Date startDate = ArrayFilter.parsepHTimestamp(startDateStr);
            endDateStr = ArrayFilter.extractColumn(inputData.get(inputData.size()-1), 0, ",") + ","
                    + ArrayFilter.extractColumn(inputData.get(inputData.size()-1), 1, ",");
            Date endDate = ArrayFilter.parsepHTimestamp(endDateStr);
            Calendar calStart = Calendar.getInstance();
            calStart.setTime(startDate);
            Calendar calEnd = Calendar.getInstance();
            calEnd.setTime(endDate);
            calEnd.roll(Calendar.YEAR, -2);
            if (calEnd.after(calStart)) {
                retVal = false;
                log.info("Start="+calStart.getTime().toString()+" End="+calEnd.getTime().toString());
            }
        }
        catch (Exception ex) {
            log.log(Level.ERROR, "DataFilters.dateRangeValid - " + ex.getMessage(), ex);
            log.error("DataFilters.dateRangeValid - startDateStr=" + startDateStr);
            log.error("DataFilters.dateRangeValid - endDateStr=" + endDateStr);
        }
        
        return retVal;
    }
}
