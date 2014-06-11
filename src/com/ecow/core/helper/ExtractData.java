package com.ecow.core.helper;

import com.ecow.core.battery.Battery;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * methods for breaking the raw data and extracting features this converts
 * ArrayList<String> data read from a file into an arrayList of double. This
 * extracts columns from files in the format set up by bolus software, where
 * column 3 is the pH and 4 the temperature 16/06/2010 , 06:37:20 , 6.45 , 38.9
 * , 3.6 , 75 extracts various parameters of the arrayList TODO make changes to
 * add redox to the file columns
 *
 * @author Toby Mottram
 */
public final class ExtractData extends DateProcess {

    /**
     * constructor
     */
    public ExtractData(ArrayList<String> pHdataList) {
        
        raw = pHdataList;
        raw = standardiseTimes(raw);
        pHData_AL = getPHOnly(pHdataList, Constants.PH_COLUMN, Constants.SEPARATOR);
        numPoints = pHdataList.size();
        pHData = convertToArray(getPHOnly(pHdataList, Constants.PH_COLUMN,
                Constants.SEPARATOR));
        days = getDays(pHdataList, actualDayReadings);
        numDays = days.size();
        Expiry = predictExpiryDate(this.raw);

        StaticMethods.showDataArray(days);
        //daypoints=  convertToStrArray( days);
        if (numDays == 0) {
            readingsDay = 0;
        } else {
            readingsDay = numPoints / numDays;
        }
        //ArrayList<oneDayData> splitDataToDays = this.splitDataToDays(raw);
    }

    /**
     * to create an arrayList of ArrayLists selected by date
     */
    public static class oneDayData {

        ArrayList<String> todaysData = new ArrayList<>();
        String DATE = "";

        /**
         * creates a list of lines with the same date
         */
        public oneDayData(ArrayList<String> inputData, String dayToSelect) {
            todaysData = this.getThisDay(inputData, dayToSelect);
            DATE = dayToSelect;
        }

        /**
         * gets passed the full array and returns an arraylist for the day
         *
         * @param dayToSelect
         * @return
         */
        private ArrayList<String> getThisDay(ArrayList<String> dataToParse, String dayToSelect) {
            
            ArrayList<String> tempList = new ArrayList<>();
            Iterator<String> listIt = dataToParse.iterator();
            //String tempString ="";
            while (listIt.hasNext()) {
                String tempString;
                tempString = listIt.next();
                String tempDate = StaticMethods.extractColumn(tempString,
                        MINIMUM_LINE_LENGTH, 0, Constants.SEPARATOR);
                // System.out.println(tempString+"  "+tempDate+"  "+dayToSelect );
                if (tempDate.trim().equals(dayToSelect.trim())) {
                    tempList.add(tempString);
                    // System.out.println("+ "+tempString);
                }
            }
            // StaticMethods.showDataArray(tempList);
            
            return tempList;
        }
    }// end of private class
    
    
    private static int maxPointsDay = 96;  // was 200 
    public ArrayList<Double> pHData_AL;   //
    public double[] pHData = new double[Constants.MAXPOINTS];
    public int numPoints;
    public int numDays;
    public ArrayList<String> days = new ArrayList<>();
    public int[] actualDayReadings = new int[Constants.MAX_pH_DAYS];
    private boolean metaDataYN = false;
    public ArrayList<String> raw = new ArrayList<>(); //to permit redrawing
    public ArrayList<String> oneDayData = new ArrayList<>(); // holds data for one day
    public String Expiry; // the predicted expiry date of the bolus
    public int readingsDay;

    
    /**
     * gives some basic stats for file selection
     */
    public ArrayList<String> getStats(ArrayList<String> inputData) {
        
        ArrayList<String> outPutData = new ArrayList<>();
        ExtractData getData = new ExtractData(inputData);
        if (getData.metaDataYN) {
            outPutData.add("has a metaData header ");
        } 
        else {
            outPutData.add("has no metaData ");
        }
        outPutData.add("Starts : " + getData.getStartDate(getData.days));
        outPutData.add("Ends   : " + getData.getEndDate(getData.days));
        outPutData.add("Days   : " + String.valueOf(getData.numDays));
        outPutData.add("Points : " + String.valueOf(getData.numPoints));
        outPutData.add("Lowest : " + (String.valueOf(getData.extractLowVal(numPoints))));
        outPutData.add("Highest: " + (String.valueOf(getData.extractHighVal(numPoints))));
        
        return outPutData;
    }

    /**
     * works through the list of days and takes out a unique element for each
     * and returns an arraylist
     */
    public ArrayList<String> getDays(ArrayList<String> inputArray, int[] keys) {
        
        ArrayList<String> outPutData = new ArrayList<>();
        Iterator<String> listIt = inputArray.iterator();
        String tempString;
        String tempDate;
        int i = 0;
        int j = 0;
        
        while (listIt.hasNext()) {
            tempString = listIt.next();
            //System.out.println(" tS2 "+tempString);
            if (StaticMethods.isValid(tempString, MINIMUM_LINE_LENGTH)) {
                tempDate = StaticMethods.extractColumn(tempString, MINIMUM_LINE_LENGTH, 0,
                        Constants.SEPARATOR);
                if (outPutData.contains(tempDate)) // check whether it has been added
                {
                    /**
                     * System.out.println(i+" not added "+tempDate);
                     */
                } else {
                    outPutData.add(tempDate);
                    //   System.out.println(j+"  "+i+"  "+tempDate);
                    keys[j] = new Integer(i);
                    j = j + 1;
                    i = 0;
                }
                i = i + 1;  // counts the number of readings between each day change
            }
        }
        
        return outPutData;
    }

    /**
     * works through the list of days and takes out a unique element for each
     * and returns an arraylist
     */
    public ArrayList<String> getDays(ArrayList<String> inputArray) {
        
        int[] keys = new int[Constants.MAXIMUM_DAYS];  // the day keys for files
        ArrayList<String> outPutData = new ArrayList<>();
        Iterator<String> listIt = inputArray.iterator();
        String tempString;
        String tempDate;
        int i = 0;
        int j = 0;
        
        while (listIt.hasNext()) {
            tempString = listIt.next();
            //System.out.println(" tS2 "+tempString);
            if (StaticMethods.isValid(tempString, MINIMUM_LINE_LENGTH)) {
                tempDate = StaticMethods.extractColumn(tempString, MINIMUM_LINE_LENGTH, 0,
                        Constants.SEPARATOR);
                if (outPutData.contains(tempDate)) // check whether it has been added
                {
                    /**
                     * System.out.println(i+" not added "+tempDate);
                     */
                } 
                else {
                    outPutData.add(tempDate);
                    System.out.println(j + "  " + i + "  " + tempDate);
                    keys[j] = new Integer(i);
                    j = j + 1;
                    i = 0;
                }
                i = i + 1;  // counts the number of readings between each day change
            }
        }
        
        return outPutData;
    }

    /**
     * iterates through the days in the ArrayList, creating new oneDayData and
     * saving them to a local arrayList
     */
    public ArrayList<oneDayData> splitDataToDays(ArrayList<String> allData) {
        
        ExtractData temp = new ExtractData(allData);
        temp.days = temp.getDays(raw, actualDayReadings);
        ArrayList<oneDayData> localDayArray = new ArrayList<>();
        System.out.println("trying to split " + temp.days.size());
        Iterator<String> listIt = temp.days.iterator();
        String tempString;
        
        while (listIt.hasNext()) {
            tempString = listIt.next();
            System.out.println("adding the next day " + tempString);
            oneDayData tempDay = new oneDayData(allData, tempString);
            localDayArray.add(tempDay);
        }
        System.out.println("completing splitting");
        
        return localDayArray;
    }

    /**
     * iterates through the days in the ArrayList, creating new oneDayData and
     * saving them to a local arrayList
     */
    public ArrayList<String> splitDataToDay(ArrayList<String> allData, String firstDay) {
        
        ExtractData temp = new ExtractData(allData);
        temp.days = temp.getDays(this.raw, actualDayReadings);
        oneDayData localDayArray = new oneDayData(allData, firstDay);
        
        return localDayArray.getThisDay(this.raw, firstDay);
    }

    /**
     * use this to create an arrayList for graphing
     */
    public ArrayList<String> getThisDay(oneDayData theDay) {
        
        return theDay.getThisDay(theDay.todaysData, theDay.DATE);
    }

    /**
     * use this to create an arrayList for graphing
     */
    public ArrayList<String> getThisDay(oneDayData theDay, String dayToGet) {
        
        return theDay.getThisDay(theDay.todaysData, dayToGet);
    }

    /**
     * iterates through the days in the ArrayList, creating new oneDayData and
     * saving them to a local arrayList
     */
    public void showData() {
        
        oneDayData localDaysData = new oneDayData(this.raw, this.getStartDate(this.raw));
        Iterator<String> listIt = this.raw.iterator();
        String tempDay; // = this.getStartDate(this.raw);
        int i = 0;
        
        while (listIt.hasNext() && (i <= this.days.size() - 1)) {
            tempDay = days.get(i);
            listIt.next();
            System.out.println("data for day " + tempDay);
            StaticMethods.showDataArray(localDaysData.getThisDay(this.raw, tempDay));
            i++;
        }
        System.out.println("completed print");
    }

    /**
     * to enable maths utils to be used to select max and min there has to be a
     * better way
     */
    private double[] convertToArray(ArrayList<Double> inputData) {
        
        double[] localpHData = new double[inputData.size()];
        Iterator<Double> listIt = inputData.iterator();
        Double tempNum;
        int i = 0;
        
        while (listIt.hasNext()) {
            tempNum = listIt.next();
            localpHData[i] = tempNum;
            //System.out.println(tempNum);
            i = i + 1;
        }
        
        return localpHData;
    }

    /**
     * converts the ArrayList of string to an array of strings
     */
    private String[] convertToStrArray(ArrayList<String> inputData) {
        
        String[] localdate = new String[inputData.size()];
        Iterator<String> listIt = inputData.iterator();
        String tempDate;
        int i = 0;
        
        while (listIt.hasNext()) {
            tempDate = listIt.next();
            localdate[i] = tempDate;
            //System.out.println(tempNum);
            i = i + 1;
        }
        
        return localdate;
    }

    /**
     * get the first date in the list of dates
     */
    public String getStartDate(ArrayList<String> inputDays) {
        
        String retVal = "empty array";
        
        if (!inputDays.isEmpty()) {
            retVal = inputDays.get(0);
        }
        
        return retVal;
    }

    /**
     * get the index of the given date in the list of dates
     */
    public int getIndex(ArrayList<String> dates, String dateChck) {
        
        int index = 0;
        Iterator<String> listIt = dates.iterator();
        int i = 0;
        
        while (listIt.hasNext()) {
            String tempDate = listIt.next();
            //  System.out.println(i+"  "+tempDate);  
            if (tempDate.trim().equals(dateChck.trim())) {
                index = i;
            }
            i = i + 1;
        }
        
        return index;
    }

    /**
     * gets the last date in the array List
     */
    public static String getEndDate(ArrayList<String> inputDays) {
        
        String retVal = "empty array";
        
        if (!inputDays.isEmpty()) {
            retVal = ArrayFilter.extractColumn(inputDays.get(inputDays.size() - 1), 0, ",");
        }
        
        return retVal;
    }

    /**
     * removes the column containing the pH data and puts it into the returned 
     * arrayList
     */
    public ArrayList<Double> getPHOnly(ArrayList<String> inputData, int keyCol, String token) {
        
        ArrayList<Double> outPutData = new ArrayList<>();
        Iterator<String> listIt = inputData.iterator();
        String tempString;
        double tempNum;
        
        while (listIt.hasNext()) {
            tempString = listIt.next();
            //System.out.println(tempString);
            if (StaticMethods.isValid(tempString, MINIMUM_LINE_LENGTH)) {
                tempNum = StaticMethods.extractValColumn(
                        tempString,
                        keyCol,
                        Constants.SEPARATOR);
                outPutData.add(tempNum);
            }
        }
        
        return outPutData;
    }

    /**
     * opens the file, extracts and averages the last 200 data points from the
     * file adds an offset looks this up in a table of depletion and adds number
     * of days to the last date in the file and calculates the expiry date
     * returns a date string
     */
    public String predictExpiryDate(ArrayList<String> allData) {

        Battery b = new Battery();

        Date d = DateProcess.addDays(
                getEndDate(this.raw),
                b.lookUp(b.getVoltageData(allData),
                b.voltDays_4Hr));

        return DateProcess.convertDateToShortString(d);
    }

    /**
     * starts with the highest possible value for pH and works down looking for
     * minima which is returned
     */
    public double extractLowVal(int numPoints) {
        
        double nextVal = 10;
        
        for (int i = 0; i < pHData.length; i++) {
            nextVal = Math.min(nextVal, pHData[i]);
            //System.out.println("Minimum = "+nextVal);
        }
        
        return nextVal;
    }

    /**
     * * takes the private array pHData and extracts the maxima for the Y axis
     *
     * @return
     */
    public double extractHighVal(int numPoints) {
        
        double nextVal = 5.0;
        
        for (int i = 0; i < pHData.length; i++) {
            nextVal = Math.max(nextVal, pHData[i]);
        }
        //System.out.println("maximum = "+nextVal);
        
        return nextVal;

    }

    /**
     * converts the ArrayList of Strings into String[] for the spinner
     */
    public static String[] getList(ArrayList<String> showData) {
        
        ExtractData mydata = new ExtractData(showData);
        String[] dayList = new String[Constants.MAXIMUM_DAYS];
        
        for (int i = 0; i < mydata.days.size(); i++) {
            dayList[i] = mydata.days.get(i);
        }
        
        return dayList;
    }

    /**
     * returns an arrayList of all the points between the two days entered as
     * Strings used with the Spinner Panel to display data
     *
     * @param args
     */
    public ArrayList<String> buildArrayBetween(int start, int end) {
        
        ArrayList<String> temp = new ArrayList<>();
        int j = start;
        System.out.println(start + " " + this.days.get(start));
        System.out.println(end + " " + this.days.get(end));
        //   for ( j=start; j>= end; j++)
        
        while (j <= end) {
            oneDayData toDay = new oneDayData(this.raw, this.days.get(j));
            System.out.println("j value = " + j);
            for (int i = 0; i < toDay.todaysData.size(); i++) {
                temp.add(toDay.todaysData.get(i));
                System.out.println(toDay.todaysData.get(i));
            }
            j++;
        }
        
        return temp;
    }

    /**
     * seeds the standardisation of time
     */
    private static String getFirstDate(String firstLine) {
        
        String str_date = "";
        
        if (StaticMethods.isValid(firstLine, MINIMUM_LINE_LENGTH)) {
            str_date = StaticMethods.extractColumn(
                    firstLine,
                    MINIMUM_LINE_LENGTH,
                    0,
                    Constants.SEPARATOR);
        }
        
        return str_date;
    }

    /**
     * opens the line of data and extracts the date and time and returns the
     * equivalent long value.
     */
    private static Long getDateTime(String lastLine) {
        
        String str_date = "";
        
        if (StaticMethods.isValid(lastLine, MINIMUM_LINE_LENGTH)) {
            str_date = StaticMethods.extractColumn(
                    lastLine,
                    MINIMUM_LINE_LENGTH,
                    0,
                    Constants.SEPARATOR);
            str_date += ",";
            str_date += StaticMethods.extractColumn(
                    lastLine,
                    MINIMUM_LINE_LENGTH,
                    1,
                    Constants.SEPARATOR);
        }
        System.out.println("date to parse " + str_date);
        
        return DateProcess.getLongofDate(str_date.trim(), "dd/MM/yyyy , HH:mm:ss");
    }

    /**
     * 
     * @param inputData
     * @return 
     */
    public static long getQuarters(ArrayList<String> inputData) {
        
        return (getDateTime(inputData.get(inputData.size() - 1)) - getDateTime(inputData.get(0))) / (15 * 60 * 1000);
    }

    /**
     * map the time stamps to a quarter basis
     */
    public static ArrayList<String> standardiseTimes(ArrayList<String> inputData) {
        
        Iterator<String> listIt = inputData.iterator();
        ArrayList<String> output = new ArrayList<>();
        System.out.println("New standardsise using getNextQuarter ");
        String tempDay = "";
        int i = 0;
        
        while (!ArrayFilter.isValid(tempDay)) {
            tempDay = getFirstDate(inputData.get(i));
            i++;
        }
        
        while (listIt.hasNext()) {
            String thisLine = listIt.next();
            if (ArrayFilter.isValid(thisLine)) {
                String str_date = StaticMethods.extractColumn(thisLine, MINIMUM_LINE_LENGTH, 0,
                        Constants.SEPARATOR);
                String str_time = StaticMethods.extractColumn(thisLine, MINIMUM_LINE_LENGTH, 1,
                        Constants.SEPARATOR);
                //System.out.println(tempDay);
                try {
                    String newLine = DateProcess.getNextQuarter(tempDay + " 00:00:00 ", tidyDates(" dd/MM/yyyy , HH:mm:ss "), tidyDates(str_date + " , " + str_time), tidyDates(" dd/MM/yyyy , HH:mm:ss "));
                    newLine = newLine + ","
                            + StaticMethods.extractColumn(thisLine, MINIMUM_LINE_LENGTH, 2, Constants.SEPARATOR) + "," //pH
                            + StaticMethods.extractColumn(thisLine, MINIMUM_LINE_LENGTH, 3, Constants.SEPARATOR) + "," //redox
                            + StaticMethods.extractColumn(thisLine, MINIMUM_LINE_LENGTH, 4, Constants.SEPARATOR) + "," // temp  
                            + StaticMethods.extractColumn(thisLine, MINIMUM_LINE_LENGTH, 5, Constants.SEPARATOR) + "," //battery
                            + StaticMethods.extractColumn(thisLine, MINIMUM_LINE_LENGTH, 6, Constants.SEPARATOR); //RSSI
                    //System.out.println(thisLine+" =  "+newLine);
                    output.add(newLine);
                    if (!str_date.equals(tempDay)) {
                        tempDay = str_date;
                    }  // update
                    i++;
                }
                catch (Exception ex) {
                    System.out.println("Exception " + ex);
                }
            }
        }
        
        return output;
    }

    /**
     * identify gaps and fill with repeated data run this after time has been
     * standardised
     */
    public static ArrayList<String> fillGaps(ArrayList<String> inputData) {
        
        Iterator<String> listIt = inputData.iterator();
        ArrayList<String> output = new ArrayList<>();
        int i = 0;
        int count = 0;
        //System.out.println("fillingGaps ");
        String str_time1, str_time2;
        String newLine = "";
        
        str_time1 = StaticMethods.extractColumn(inputData.get(0), MINIMUM_LINE_LENGTH, 0, Constants.SEPARATOR)
                  + ","
                  + StaticMethods.extractColumn(inputData.get(0), MINIMUM_LINE_LENGTH, 1, Constants.SEPARATOR);
        //System.out.println(inputData.get(0) + "  date " + str_time1);

        while (listIt.hasNext()) {
            String thisLine = listIt.next();

            if (ArrayFilter.isValid(thisLine)) {
            //    System.out.println(thisLine);
            //}
            //{
                str_time2 = thisLine.substring(0, 22);

                long timeGap = (DateProcess.getLongofDate(str_time2, "dd/MM/yyyy , HH:mm:ss")
                        - DateProcess.getLongofDate(str_time1, "dd/MM/yyyy , HH:mm:ss")) / (60 * 1000); // in minutes
                //System.out.println(str_time1 + " " + str_time2 + " gap " + timeGap);

                int num = (int) timeGap / 15;
                // create a new line with the last data plus a new time 
                //str_time1 is the last valid time 
                if (num != 0) {
                    for (int j = 0; j <= num - 1; j++) {
                        long time3 = DateProcess.getLongofDate(str_time1, "dd/MM/yyyy , HH:mm:ss") + (15 * 60000);
                        // System.out.println(str_time1+"  "+time3+ "  "+DateProcess.convertLongtoString(time3,"dd/mm/yyyy , HH:mm:ss"));
                        newLine = DateProcess.convertLongtoString(time3, "dd/MM/yyyy , HH:mm:ss ")
                                + ","
                                + StaticMethods.extractColumn(thisLine, MINIMUM_LINE_LENGTH, 2, Constants.SEPARATOR) + "," //pH
                                + StaticMethods.extractColumn(thisLine, MINIMUM_LINE_LENGTH, 3, Constants.SEPARATOR) + "," //redox
                                + StaticMethods.extractColumn(thisLine, MINIMUM_LINE_LENGTH, 4, Constants.SEPARATOR) + "," // temp  
                                + StaticMethods.extractColumn(thisLine, MINIMUM_LINE_LENGTH, 5, Constants.SEPARATOR) + "," //battery
                                + StaticMethods.extractColumn(thisLine, MINIMUM_LINE_LENGTH, 6, Constants.SEPARATOR); //RSSI
                        // + " ,"+Integer.toString(count) + " ,"+Integer.toString(j); */

                        count++;
                        output.add(newLine); // needs to repeat
                        str_time1 = DateProcess.convertLongtoString(time3, "dd/MM/yyyy , HH:mm:ss");
                    }
                    thisLine = newLine;
                }
                else {
                    i++;
                    try {
                        output.add(thisLine);
                    }
                    catch (Exception ex) {
                        System.out.println("Exception " + ex);
                    }
                }
            }
        }
        //System.out.println("new lines " + count);
        
        return output;
    }

    /**
     * create two new text files to save, the original is unchanged needs
     * tidying to prevent nullpointer exceptions
     */
    public static ArrayList<String> removeDuplicates(ArrayList<String> al) {
        
        ArrayList<String> newAL = new ArrayList<>();
        // s is a linked Hash Set set of Strings which cannot have duplicates
        Set<String> s = new LinkedHashSet<>(al);
        s.addAll(al);
        al.clear();

        al.addAll(s);
        System.out.println("  no duplicates to clean ");
        
        for (String cl : al) { //System.out.println(cl);
            newAL.add(cl);
        }
        System.out.println("duplicates removed ");
        
        return newAL;
    }

    public static void main(String[] args) {

        System.out.println(DateProcess.getLongofDate("07/06/2013 , 21:09:00", "dd/mm/yyyy , HH:mm:ss"));
        System.out.println(DateProcess.getLongofDate("06/06/2013 , 20:00:00", "dd/mm/yyyy , HH:mm:ss"));
        System.out.println(DateProcess.convertLongtoString(22625175, "dd/mm/yyyy , HH:mm:ss"));

    }
}
