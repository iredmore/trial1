/*
 * Copyright (c) EEZ Ltd 2012
 * 
 * This software and the intellectual property in it is proprietary to
 * EEZ Ltd and/or its licensors. Your use of it is subject to the terms and
 * restrictions set out in the contract under which it was supplied. You must
 * not use it for any other purpose without EEZ's prior written permission
 * and in particular, but without limitation, you must not copy, reverse
 * engineer or decompile this software nor permit or purport to permit any
 * third party to do (other than and to the extent the same cannot be prohibited
 * by law).
 */

package com.ecow.core.email;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * Helper class for email associated actions.
 * 
 * @author Iain Redmore
 */
public class MailUtil {
    
    static public boolean isValidAddress(String emailAddress) {
        
        boolean retVal = true;
        
        try {
            InternetAddress addr = new InternetAddress(emailAddress, true);
        }
        catch (AddressException ex) {
            retVal = false;
        }
        
        return retVal;
    }
}
