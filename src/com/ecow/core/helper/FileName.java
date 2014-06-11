/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ecow.core.helper;

import java.util.ArrayList;

/**
 * to provide utilities for creating and analysing filenames containing metadata
 * created in the format [user]-[farm]_A_0000_yyyymmdd_XXX where [user] is the
 * customer set up in software, [farm] is the location of the bolus A is the
 * device type B= bolus C= collar 0000 is the device number
 *
 * @author Toby Mottram
 */
public class FileName {

    String deviceType;
    String deviceID;
    String dateS;

    public FileName() {
    }

    public void getFilename(String fName) {
        deviceType = getDevice(fName);
        deviceID = getID(fName);
        dateS = getDate(fName);
    }

    private String getDevice(String fileName) {
        String typeID = StaticMethods.extractColumn(fileName, 10, 1, "_");
        String device = "Bolus";  // default
        if (typeID.equals("B")) {
        } else if (typeID.equals("C")) {
            device = "Collar";
        } else {
            device = "unknown";
        }
        return device;
    }

    public static String getDate(String fileName) {

        return StaticMethods.extractColumn(fileName, 10, 3, "_");
    }

    public static String getID(String fileName) {

        return StaticMethods.extractColumn(fileName, 10, 2, "_");
    }

    public static String getCustomer(String fileName) {

        String custFarm = StaticMethods.extractColumn(fileName, 10, 0, "_");
        return StaticMethods.extractColumn(custFarm, 10, 0, "-");
    }

    public static String getFarm(String fileName) {

        String custFarm = StaticMethods.extractColumn(fileName, 10, 0, "_");
        return StaticMethods.extractColumn(custFarm, 10, 1, "-");
    }

    private void printDetails() {
        System.out.println("This file was created on "
                + this.dateS + "by a "
                + this.deviceType + " serial number "
                + this.deviceID);
    }

    /**
     * compares the bolusID from each fileName to check whether the bolus is
     * continuous data
     *
     * @param newID
     * @param firstID
     * @return
     */
    public static boolean checkFile(String newID, String firstID) {
        boolean control = true;
        if (newID.equals(firstID)) {
        } else {
            control = false;
            System.out.println(" device is not the same ");
        }
        return control;
    }

    /**
     * builds a list of IDs for checking
     */
    public static ArrayList<String> buildIDList(ArrayList<String> fileNames) {
        ArrayList<String> IDList = new ArrayList<>();
        for (int i = 0; i < fileNames.size(); i++) {
            FileName ID = new FileName();
            ID.getFilename(fileNames.get(i));
            IDList.add(ID.deviceID);
        }
        return IDList;
    }

    /**
     * checks a list of IDs to ensure they are the same and returns a warning
     */
    public static String checkIDList(ArrayList<String> IDList) {
        String mesg = "Checking List OK";
        if (IDList.size() > 1) {
            System.out.println(mesg);
            for (int i = 1; i < IDList.size(); i++) {
                if (IDList.get(0).equals(IDList.get(i))) {
                } // all the same
                else {
                    mesg = "WARNING Device IDs are not all the same";
                }
            }
        }
        return mesg;
    }

    /**
     * checks a list of IDs to ensure they are the same and returns a warning
     */
    public static boolean checkIDs(ArrayList<String> IDList) {
        boolean mesg = true;
        if (IDList.size() > 1) {
            System.out.println(mesg);
            for (int i = 1; i < IDList.size(); i++) {
                if (IDList.get(0).equals(IDList.get(i))) {
                } // all the same
                else {
                    mesg = false;
                }
            }
        }
        return mesg;
    }

    public static void main(String[] args) {

        System.out.println("Customer="+getCustomer("TCF-TwoPools_B_9999_20140127_XXX.csv"));
        System.out.println("Farm="+getFarm("TCF-TwoPools_B_9999_20140127_XXX.csv"));
        System.out.println("Id="+getID("TCF-TwoPools_B_9999_20140127_XXX.csv"));
        System.out.println("Date="+getDate("TCF-TwoPools_B_9999_20140127_XXX.csv"));
        
        ArrayList<String> filenames = new ArrayList<String>();
        filenames.add("C:/bolus/data/Provimi/Provimi_B_146_02022012_XXX.csv");
        filenames.add("C:/bolus/data/Provimi/Provimi_B_146_02022012_XXX.csv");
        filenames.add("C:/bolus/data/Provimi/Provimi_B_146_02022012_XXX.csv");
        filenames.add("C:/bolus/data/Provimi/Provimi_B_146_02022012_XXX.csv");
        filenames.add("C:/bolus/data/Provimi/Provimi_B_146_02022012_XXX.csv");
        filenames.add("C:/bolus/data/Provimi/Provimi_B_146_02022012_XXX.csv");


        ArrayList<String> IDS = buildIDList(filenames);

        System.out.println(checkIDList(IDS));
    }
}
