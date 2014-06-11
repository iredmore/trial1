/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ecow.core.helper;

/**
 * *
 *
 *
 * @author Toby Mottram methods to put arrayLists and Strings into files and to
 * extract them
 */
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class FileOpenClose {

    String fileName;

    public FileOpenClose(String file2Open) {
        fileName = file2Open;

    }

    /**
     * assumes a file name formatted *******_XXX.csv and changes and substitutes
     * the "XXX" with toCreate
     */
    public String setFileName(String original, String toReplace, String toCreate) {
        
        return original.replace(toReplace, toCreate);

    }

    /**
     * reads data into an ArrayList from the named file
     */
    public ArrayList<String> readInData(String fName) {
        
        boolean control = true;
        String nextLine = "";
        ArrayList<String> newData = new ArrayList<>();
        try {
            Scanner sc = new Scanner(new File(fName));
            System.out.println("file opened " + fName);
            while (sc.hasNext() && control) {
                nextLine = sc.nextLine();
                if (!nextLine.equals("")) {
                    control = newData.add(nextLine.trim());
                }
            }
            sc.close();
            System.out.println("file closed " + fName);
        }
        catch (Exception e) {
            System.out.println("file exception found " + e);
        }

        return newData;
    }

    /**
     * reads data into an ArrayList from the named bolus data file
     */
    public ArrayList<String> readInBolusData(String fName) {
        
        boolean control = true;
        String nextLine = "";
        ArrayList<String> newData = new ArrayList<>();
        
        try {
            Scanner sc = new Scanner(new File(fName));
            System.out.println("file opened " + fName);
            while (sc.hasNext() && control) {
                nextLine = sc.nextLine();
                if (!nextLine.equals("")) {
                    String dataStr = (ArrayFilter.extractColumn(nextLine, 0, ",") + ","
                            + ArrayFilter.extractColumn(nextLine, 1, ",") + ","
                            + ArrayFilter.extractColumn(nextLine, 2, ",") + ","
                            + ArrayFilter.extractColumn(nextLine, 3, ",") + ","
                            + ArrayFilter.extractColumn(nextLine, 4, ",") + ","
                            + ArrayFilter.extractColumn(nextLine, 5, ","));
                    control = newData.add(dataStr.trim());
                }
            }
            sc.close();
            System.out.println("file closed " + fName);
        }
        catch (Exception e) {
            System.out.println("file exception found " + e);
        }

        return newData;
    }

    /**
     * writes a full ArrayList to file
     */
    public boolean writeOutData(ArrayList<String> newData, String fName) {
        
        boolean control = true;
        Iterator<String> listIt = newData.iterator();
        
        try {
            PrintWriter pw = new PrintWriter(new File(fName));
            System.out.println("file opened " + fName);
            while (listIt.hasNext()) {
                pw.println(listIt.next());
            }
            pw.close();
            System.out.println("file closed " + fName);
        }
        catch (Exception e) {
            System.out.println("file exception found " + e);
            control = false;
        }
        
        return control;
    }

    /**
     * reads in data from the file adds the new data and saves it
     */
    public boolean addToFile(String newData, String fName) {
        
        boolean control = true;
        ArrayList<String> allData = new ArrayList<>();
        allData = readInData(fName);
        System.out.println("data read in");
        allData.add(newData);
        Iterator<String> listIt = allData.iterator();
        try {
            FileWriter pw = new FileWriter(fName);
            System.out.println("file opened " + fName);
            while (listIt.hasNext()) {
                pw.write(listIt.next());
                pw.write(System.lineSeparator());
            }
            pw.close();
            System.out.println("file closed after adding data " + fName);
            control = true;
        }
        catch (Exception e) {
            System.out.println("file exception found " + e);
            control = false;
        }
        
        return control;
    }

    public void addArrayToFile(ArrayList<String> newData, String fName) {
        
        boolean control = false;
        ArrayList<String> allData = new ArrayList<>();
        allData = readInData(fName);
        control = allData.addAll(newData);

        if (control) {
            Iterator<String> listIt = allData.iterator();
            try {
                FileWriter pw = new FileWriter(fName);
                System.out.println("file opened " + fName);
                while (listIt.hasNext()) {
                    String tempString = listIt.next();
                    if (tempString.startsWith(" ")) {
                        System.out.println("##### addArrayToFile found space at start of line="+tempString);
                        tempString = tempString.trim();
                    }
                    // System.out.println(tempString);
                    pw.write(tempString);
                    pw.write(System.lineSeparator());
                }
                pw.close();
                System.out.println("file closed after arraylist added " + fName);
            }
            catch (Exception e) {

                System.out.println("file exception found " + e);
            }
        }
    }

    /**
     * check if this file exists and create it if it doesn't
     */
    public void setUpFile(String fName) {
        
        File f = new File(fName);
        if (f.exists()) {
            System.out.println(fName + " ready to use ");
        }
        else {
            try {
                f.createNewFile();
            }
            catch (IOException e) {
                String message = fName + "  " + e;
                System.out.println(message);
            }
        }
    }

    public boolean addArrayToFileB(ArrayList<String> newData, String fName) {
        
        boolean control = false;
        ArrayList<String> allData = new ArrayList<>();
        allData = readInData(fName);
        control = allData.addAll(newData);
        if (control) {
            Iterator<String> listIt = allData.iterator();
            try {
                FileWriter pw = new FileWriter(fName);
                System.out.println("file opened " + fName);
                while (listIt.hasNext()) {
                    String tempString = listIt.next();
                    // System.out.println(tempString);
                    pw.write(tempString);
                    pw.write(System.lineSeparator());
                }
                pw.close();
                System.out.println("file closed after boolean Add" + fName);
            }
            catch (Exception e) {
                System.out.println("file exception found " + e);
            }
        }
        
        return control;
    }

    public static String setUpLogFile(String path) {
        
        int n = 0;
        String logFileName = path + DateProcess.now(Constants.FILE_DATE_FORMAT_NOW + "_" + Constants.FILE_TIME_FORMAT_NOW) + ".log";
        
        return logFileName;
    }

    /**
     * brings an image and converts it into an icon
     */
    public static Icon getImage(String fName) {
        
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(fName));
        }
        catch (IOException e) {
        }
        ImageIcon icon = new ImageIcon();
        icon.setImage(img);
        return icon;
    }

    public static void main(String[] args) {
        
        // String path ="C:/bolus/data/";
        String path = "C:\\bolus\\data\\play\\";
        String fName = "FileList1.txt";
        path = path.concat(fName);
        FileOpenClose foc = new FileOpenClose(path);

        System.out.println("path " + path);
        String createFileName = path + "NONSENSE";
        System.out.println(createFileName);
        foc.addToFile(path, fName);

        // System.out.println(" log file created "+ setUpLogFile("C:/bolus/data/"));

        /*     ArrayList<String> playData = new ArrayList<String>();       
           
         FileOpenClose thisFile = new FileOpenClose(createFileName);
         playData= thisFile.readInData(createFileName);

         playData.add(" more junk1");
         playData.add(" more junk2");
         playData.add(" more junk3");
         thisFile.addArrayToFileB(playData,createFileName) ;*/


    }
}
