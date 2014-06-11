/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ecow.core.helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

/** a collection of public static methods used regularly
 * 
 */
public class StaticMethods {

	/**check that the string to be processed is not a date or time by
 looking for separators */
	public static boolean isNumber(String inputCol)
	{
		boolean check = true;
		//System.out.println("isNumber checking "+inputCol);
		if (inputCol!=null)
		{
			if (inputCol.contains(":"))
			{
				System.out.println("not a number : "+inputCol);
				check = false;
			}
			else
				if (inputCol.contains("/"))
				{
					System.out.println("not a number  / " + inputCol);
					check = false;
				}
		}
		else
                {check = false;}  //if null string
		return check;
	}
	/** checks the data line for validity - some files may acquire empty headers */

	public static boolean isValid(String inputLine, int minLength)
	{ boolean temp = false;

	if ((inputLine.length() < minLength))
	{
		System.out.println("invalid line skipped "+ inputLine);
	}
	else

		if (inputLine.trim().isEmpty()   )
		{
			System.out.println("invalid line skipped "+ inputLine);
		}
		else
		{
			temp = true;
		}
	return temp;
	}


	/** takes in an arrayList of strings and prints everything for debug */

	public static void showDataArray(ArrayList<String> inputData)
	{
		int i = 0;
		Iterator<String> listIt = inputData.iterator();
		while (listIt.hasNext())
		{
			System.out.println(i + "  "+listIt.next());
			i=i + 1;
		}
	}

	/** extracts the string value from the specified column, trimming it
 and converting it to a double */
public static double extractValColumn(String inputLine, int valueCol,String token)
	{
		String[] tempArray = new String[25];  //TODO make this generic 
		StringTokenizer em = new StringTokenizer(inputLine,token);
		double d =0;
		int i = 0; //counts the elements
		if (em.hasMoreTokens())
		{
			while (em.hasMoreTokens())
			{
				tempArray[i]=em.nextToken();
				//System.out.println("temp Array "+ i + " = "+tempArray[i]);
				i = i +1;
			}
			if (isNumber(tempArray[valueCol])) {
                        try {
                                d = Double.valueOf((tempArray[valueCol]).trim()).doubleValue();
                        } catch (NumberFormatException nfe) {
                                System.out.println("NumberFormatException: " + nfe.getMessage());
                        }
                    }
		}
		else {System.out.println("No tokens d =" + d);}
		return d;
	}

	/** takes a String and splits it into columns and returns the requested columns
 data in a String */
	public static String extractColumn(String inputLine, int minLength, int keyCol, String token)
            {
		if (StaticMethods.isValid(inputLine,minLength))
		{
			String[] tempArray = new String[20];
			StringTokenizer em = new StringTokenizer(inputLine,token);
			int i = 0; //counts the elements
			while (em.hasMoreTokens())
			{
				tempArray[i]=em.nextToken();
				//System.out.println("extracting col  "+ tempArray[i]);
				i = i +1;
			}
			String temp = tempArray[keyCol];
			return temp;
		}
		else {return "no line to parse";}
            }
	
        /** takes in an arrayList of strings and removes the indicated row and prints it*/
	public static ArrayList<String> deleteRow(ArrayList<String> inputData,
			int row, String reportFileName)
	  {
		//ArrayList<String> outPutData = new ArrayList<String>();
		String tempString = inputData.get(row);
		inputData.remove(row);
		System.out.println("line : "+tempString+"  removed");    
		return inputData;
			}

  /**
* Method rounds up decimal values to specified amount of places
*
* @param value - a double that requires rounding up
* @param places - the number of places required
* @return - round up figure of <code>value</code>
*/
    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    /** extracts the metaData header from a file and returns it as an ArrayList 
 length of the header is fixed by METADATA_LINES */
public static ArrayList<String> getHeader(String fName)
{
    ArrayList<String> myData = new ArrayList<>();
    ArrayList<String> tempData;
    FileOpenClose thisFile = new FileOpenClose(fName);
    tempData = thisFile.readInData(fName);
    if (tempData.get(0).contains("eCow metadata bolus format, v1"))
    {
    for (int i=0; i<Constants.METADATA_LINES; i++)
    {
        myData.add(i, i+","+  tempData.get(i));
    }
    }
    return myData;
}

/** adds the metaData header to a file 
 it opens the file 
 checks whether there is a header
 overwrites it if there is one
 or creates a new header */
public static void changeHeader(String fName, ArrayList<String> myData)
  {
    FileOpenClose thisFile = new FileOpenClose(fName);
    ArrayList<String> tempData  = thisFile.readInData(fName);
    for (int i=0; i<tempData.size(); i++)
    {
        System.out.println(tempData.get(i));
    }
    if (tempData.get(0).equals("eCow metadata bolus format, v1"))
    {
        System.out.println("file has a header already");
        for (int i=0; i<Constants.METADATA_LINES; i++)
    {
        tempData.set(i, myData.get(i));
    }    
    }   
    else
    {
        System.out.println("file has no header, adding new data ");
       tempData.addAll(0, myData);       //appends the file data to the metadata
        for (int i=0; i<50; i++)
        {
            System.out.println(tempData.get(i));
        }
   }
    thisFile.setFileName(fName, "XXX", "MET");
    System.out.println("created "+fName);
    thisFile.writeOutData(tempData, fName);
  }
 
    
    
}