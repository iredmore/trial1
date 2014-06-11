/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ecow.core.battery;

import com.ecow.core.helper.Constants;
import com.ecow.core.helper.DateProcess;
import com.ecow.core.helper.ExtractData;
import com.ecow.core.helper.FileOpenClose;
import com.ecow.core.helper.StaticMethods;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 *
 * @author Toby Mottram
 * 
 * Calculates the life remaining in the bolus battery at different 
 * radio on times.
 * takes a voltage (generally 2.4-3.2)  multiplies by 10000 and looks up the position 
 * in the different weekly depletion arrays and multiplies by 7 to get a days to add to the date
 */
public class Battery {


    /** an array to get started values are V*10000 to allow us to work in integers, these have been derived from measurement
      saved in battery depletion spreadsheets */

   public int[] voltDays_2Hr = {24400, 24600, 24800,25000,25200,25400,25600,25800,26000,26200,26400,26600,26800,27000,27200,27400,
      27600,27800,28000,28200,28350,28500,28650,28800,
    28950,29100,29250,29400,29550,29700,29950,30100,30250,30400,30550,30700,
    30850,30998,31150,31300,31450,31600};
   
   
   
   public int[] voltDays_4Hr = {24401, 24601, 24801,25001,25202,25400,25600,25800,26001,26140,26280,26420,26560,26701,26840,26980,27196,27645,28025,28355,28645,28885,29085,29265
,29405,29535,29635,29725,29815,29895,29975,30075,30175,30295,30445,30615,30815
,31055,31335};
   
   public int[] voltDays_12Hr = {24000,24250,24600,24950,25300,25650,26000,26192,26385,26577,26770,26962,27155,27347,27540,27732,27925,28117,28310,28502,28695,
      28887,29080,29272,29465,29657,29850,30042,30235,30427,30620,30812,31100,31500};
   
   public int[] voltDays_24Hr = {24001,24501,25001,25501,26001,26259,26518,26777,27036,27295,27554,27813,28072,28331,28590,28849,29108,29367,29626,29885,30144,30403,30662,30921,31200,31600};

public static final int NUM_POINTS = 100;     //period to average   

public static void printArray(int[] input)
{   int j=0;
    System.out.println("V array length = "+input.length);

    for (j=0; j<input.length; j++)
    {
       System.out.println("[ "+j+"]"+input[j]); 
    }
}
  
/** takes in an arrayList of String and extracts the column of battery to a new 
 * arrayList */
/** removes the column containing the voltage and puts it into the returned 
 * arrayList*/
public ArrayList<Double> getVoltage(ArrayList<String> inputData,
        int keyCol, String token)
{
    ArrayList<Double> outPutData = new ArrayList<Double>();
    Iterator<String> listIt = inputData.iterator();
    String tempString ="";
    double tempNum;
     while (listIt.hasNext())
    {
      tempString = listIt.next();
      //System.out.println(tempString);
      if (StaticMethods.isValid(tempString,Constants.MINIMUM_LINE_LENGTH))
      {
       tempNum=StaticMethods.extractValColumn(tempString,keyCol, 
                                                     Constants.SEPARATOR);
       outPutData.add(tempNum);
      }      
    }
    return outPutData;
}


/** gets data from the arraylist of raw data and calculates
 * the mean voltage in the recent days */
public int getVoltageData( ArrayList<String> data)
{
   int volts; 
   double voltSum = 0;
   ArrayList<Double> voltArray; 
   voltArray = this.getVoltage(data,Constants.VOLT_COLUMN , ",");
   System.out.println("looking up recent Volts ");
   for (int i= voltArray.size()-NUM_POINTS; i<voltArray.size();i++)
   {
       voltSum = voltSum+ voltArray.get(i);
       //System.out.println(i+" sum  "+voltSum);
   }
   volts = (int) ((voltSum/NUM_POINTS)*10000);
   System.out.println("volts = "+volts/10000);
   return volts;
}



/** opens a constant array developed from experimental data
 * where the index is the number of weeks remaining and the value
 * is the lower voltage , the measured voltage is checked
 to locate the index which is returned */
public int lookUp(int voltsIn,int[] input )
{
    int localInt = 0;
    int j =0;
    System.out.println("array length = "+input.length+
            "looking up "+voltsIn);
    for (j=0; j<input.length-1; j++)
    {
      System.out.println(j+" V "+voltsIn+" Vn "+ input[j]+" Vm "+ input[j+1]);
       if ((voltsIn>input[j])&&(voltsIn<input[j+1]))
       {
           System.out.println("yes  "+input[j]+" days left = "+j*7);
           localInt = j*7;
       }
       else
       {
          System.out.println(" no match ");
       }
    } 
    return localInt;
}    

public String getLastDate(String title)
{
   FileOpenClose newFile = new FileOpenClose(title);
   ExtractData myData = new ExtractData(newFile.readInData(title));
   return ExtractData.getEndDate(myData.raw);
}       

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String title = "C:/bolus/data/Wall Farm/eCow Wall Farm_B_0205_28072012_XXX.csv";
        System.out.println(title);
        FileOpenClose myFile = new FileOpenClose(title);
        ArrayList<String> myArray = myFile.readInData(title);
        Battery b = new Battery();
        int result =    b.getVoltageData(myArray);    
        System.out.println("result "+result);
        
      //  System.out.println("lookUp = "+b.lookUp(result));
       b.printArray(b.voltDays_4Hr);
       System.out.println(b.lookUp(result, b.voltDays_4Hr));
       System.out.println("last date = "+b.getLastDate(title));
       Date date2Show =
       DateProcess.addDays(ExtractData.getEndDate(myArray),
           b.lookUp(b.getVoltageData(myArray), b.voltDays_4Hr));
       System.out.println(date2Show);
       
    
        
    }
}
