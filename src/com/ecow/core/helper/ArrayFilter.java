package com.ecow.core.helper;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * * get a file from the file selector the file will contain duplicate groups of
 * lines due to failed data reads create a running report box Open file to read
 * works through the array and creates a new text files named *****_CLN.csv
 * containing the cleaned data
 *
 */
public class ArrayFilter {

    /**
     * * checks the data line for validity - some files may acquire empty
     * headers
     */
    public static boolean isValid(String inputLine) {
        boolean temp = false;

        if ((inputLine.length() < Constants.MINIMUM_LINE_LENGTH)) {
            System.out.println("too short to be valid data  = " + inputLine);
        } else if (inputLine.trim().isEmpty()) {
            System.out.println("no data in this line " + inputLine);
        } else {
            temp = true;
        }
        return temp;
    }

    /**
     * lists the lines of data to console
     */
    public static void list(ArrayList<String> datatoList) {
        Iterator<String> listIt = datatoList.iterator();

        while (listIt.hasNext()) {

            System.out.println(listIt.next());

        }
        System.out.println("completed print");
    }

    /**
     * takes a String and splits it into columns and returns the requested
     * columns data in a String
     */
    public static String extractColumn(String inputLine, int keyCol, String token) {
        if (isValid(inputLine)) {
            String[] tempArray = new String[20];
            StringTokenizer em = new StringTokenizer(inputLine, token);
            int i = 0; //counts the elements
            while (em.hasMoreTokens()) {
                tempArray[i] = em.nextToken();
                //System.out.println("extracting col  "+ tempArray[i]);
                i = i + 1;
            }
            String temp = tempArray[keyCol];
            return temp;
        } else {
            return "";
        }
    }

    /**
     * uses the date format found in the bolus .csv files
     */
    public static Date parsepHTimestamp(String timestamp) throws Exception {
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy , HH:mm:ss", Locale.ENGLISH);
        Date d = sdf.parse(timestamp.trim());

        return d;
    }

    /**
     * assumes a file name formatted *******_XXX.csv or "xxx" and changes and
     * substitutes the "XXX" with toCreate
     */
    public String setFileName(String original, String toCreate) {
        if (original.contains("xxx")) {
            original.replace("xxx", "XXX");
        } else {
            if (original.contains("CAT")) {
                String //<editor-fold defaultstate="collapsed" desc="comment">
                        replace //</editor-fold>
                        = original.replace(original, toCreate);
            } else {
                System.out.println(" file name is not formatted correctly");
            }
        }
        return original.replace("XXX", toCreate);
    }

    /**
     * create two new text files to save, the original is unchanged needs
     * tidying to prevent nullpointer exceptions
     */
    public ArrayList<String> removeDuplicates(ArrayList<String> al) {
        ArrayList<String> newAL = new ArrayList<>();
        // s is a linked Hash Set set of Strings which cannot have duplicates
        Set<String> s = new LinkedHashSet<>(al);
        newAL.addAll(s);
        s.addAll(al);
        al.clear();

        al.addAll(s);
        //System.out.println("  no duplicates to clean ");
        for (String cl : al) { //System.out.println(cl);
            newAL.add(cl);
        }
        //System.out.println("duplicates removed ");
        return newAL;
    }

    /**
     * converts the ArrayList into a sorted list with time values as the key to
     * remove duplicates
     */
    public ArrayList<String> sortByDate(ArrayList<String> inArray) {
        //  Map<Date,String> newMap = new TreeMap<Date,String>();
        //    newMap = getALMap(inArray); 
        System.out.println("sortByDate: inArray[0]="+inArray.get(0));
        SortedMap<Date, String> sm;//=new TreeMap<>();
        ArrayList<String> sortedList = new ArrayList<>();
        sm = getALMap(inArray);
        Set s = sm.entrySet();
        // Using iterator in SortedMap 
        Iterator i = s.iterator();

        while (i.hasNext()) {
            Map.Entry m = (Map.Entry) i.next();

            //m.getKey();
            String value = (String) m.getValue();
            String dateStr = DateProcess.convertDateToString((Date) m.getKey());
            /*if (dateStr.startsWith(" ")) {
                System.out.println("##### ALERT: dateStr="+dateStr);
            }*/
            sortedList.add(dateStr + "," + value);
            // System.out.println("Key :"+DateProcess.convertDateToString((Date)m.getKey())+"  value :"+value);
        }
        //for (int j = 0; j < sortedList.size(); j++) {//System.out.println(j+"  "+sortedList.get(j));
        //}
        //System.out.println("Data Sorted by sampling time  ");
        return sortedList;
    }

    /**
     * take in an arrayList and return it as a Map where the k is the Date and
     * the String is the remainder of the line
     */
    public static TreeMap<Date, String> getALMap(ArrayList<String> inArray) {
        //DateProcess dP = new DateProcess();
        System.out.println("getALMap: inArray[0]="+inArray.get(0));
        TreeMap<Date, String> dateMap = new TreeMap<>();
        String dateStr, dataStr;
        Date newDate;
        Iterator<String> listIt = inArray.iterator();
        String tempStr;
        int i = 0;
        while (listIt.hasNext()) {
            tempStr = listIt.next();
            try {
                dateStr = extractColumn(tempStr, 0, ",").trim() + " , "
                        + extractColumn(tempStr, 1, ",").trim();
                newDate = parsepHTimestamp(dateStr);
                dataStr = extractColumn(tempStr, 2, ",") + ","
                        + extractColumn(tempStr, 3, ",") + ","
                        + extractColumn(tempStr, 4, ",") + ","
                        + extractColumn(tempStr, 5, ",") + ","
                        + extractColumn(tempStr, 6, ",");
                dateMap.put(newDate, dataStr);
            } catch (Exception e) {
                System.out.println("ArrayFilter.getALMap - exception thrown at " + tempStr + "  " + e);
            }

            //   System.out.println(i+"  "+tempStr);
            i = i + 1;
        }
        return dateMap;
    }

    public static void main(String[] args) {
    }
}
