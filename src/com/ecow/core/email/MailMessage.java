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

import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.event.TransportListener;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Generic email class enabling the creation of an email with multiple cc, bcc
 * and attachments sent via a specified SMTP server using service provider
 * login details.
 * 
 * @author Iain Redmore
 */
public class MailMessage {
    
    String smtpServer;
    String fromAddress = "";
    String subject = "";
    String content = "";
    String debug = "false";
    ArrayList<String> toAddress = new ArrayList<>();
    ArrayList<String> ccAddress = new ArrayList<>();
    ArrayList<String> bccAddress = new ArrayList<>();
    ArrayList<String> attachments = new ArrayList<>();
    
    TransportListener listener = null;
    
    
    /**
     * MailMessage constructor
     * 
     * @param server - name of SMTP server
     */
    public MailMessage(String server, String from) {
        
        smtpServer = server;
        fromAddress = from;
    }
    
    public void addListener(TransportListener l) {
        
        listener = l;
    }
    
    public void setDebugging(boolean b) {
        
        if(b) {
            debug = "true";
        }
        else {
            debug = "false";
        }
    }
    
    /**
     * Add email address to recipient list.
     * 
     * @param address 
     */
    public void addRecipient(String address) {
        
        if (validateEmailAddress(address)) {
            toAddress.add(address);
        }
    }
    
    /**
     * Add email address to CC list.
     * 
     * @param address 
     */
    public void addCcRecipient(String address) {
        
        if (validateEmailAddress(address)) {
            ccAddress.add(address);
        }
    }
    
    /**
     * Add email address to BCC list.
     * 
     * @param address 
     */
    public void addBccRecipient(String address) {
        
        if (validateEmailAddress(address)) {
            bccAddress.add(address);
        }
    }
    
    /**
     * Add filename of file to attachment list.
     * 
     * @param filename 
     */
    public void addAttachment(String filename) {
        
        attachments.add(filename);
    }
    
    /**
     * Set the message subject.
     * 
     * @param s 
     */
    public void setSubject(String s) {
        
        subject = s;
    }
    
    /**
     * Set the message text.
     * 
     * @param s 
     */
    public void setContent(String s) {
        
        content = s;
    }
    
    public void sendMail(String mailUser, String mailPassword) {
        
        Session session = createSmtpSession(mailUser, mailPassword);
        
        try {
            // Create the message
            MimeMessage msg = new MimeMessage(session);
            
            // Add sender address
            msg.setFrom(new InternetAddress(fromAddress));
            
            // Add recipient(s)
            for (String s : toAddress) {
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(s));
            }
            
            // Add cc's
            for (String s : ccAddress) {
                msg.addRecipient(Message.RecipientType.CC, new InternetAddress(s));
            }
            
            // Add bcc's
            for (String s : bccAddress) {
                msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(s));
            }
            
            // Set the subject
            msg.setSubject(subject);
            
            // Fill the message
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(content);
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            
            // Add any attachments
            DataSource source;
            for (String s : attachments) {
                messageBodyPart = new MimeBodyPart();
                source = new FileDataSource(s);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(source.getName());
                multipart.addBodyPart(messageBodyPart);
            }
            msg.setContent(multipart);
            msg.setSentDate(new Date());
            if (listener != null) {
                session.getTransport().addTransportListener(listener);
            }
            Transport.send(msg);
            System.out.println("Mail sent!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Create the Session.
     * 
     * @param mailUsername - mail server login name
     * @param mailPassword - mail server password
     * @return 
     */
    private Session createSmtpSession(final String mailUsername, final String mailPassword) {
        
        final Properties props = new Properties();
        /*props.setProperty("mail.smtp.host", smtpServer);
        props.setProperty("mail.smtp.auth", "true");
        //props.setProperty("mail.smtp.port", "" + 587);
        props.setProperty("mail.smtp.starttls.enable", debug);
        props.setProperty("mail.debug", "true");
        props.setProperty("mail.smtp.ssl.trust", smtpServer);*/

        //String host = "smtp.gmail.com";
        //String from = "hathor.dvl";
        //String pass = "h4th0rdvl";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpServer);
        //props.put("mail.smtp.user", from);
        //props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.trust", smtpServer);
        
        return Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailUsername, mailPassword);
            }
        });
    }

    /**
     * Validate email address against RFC822 syntax.
     * 
     * @param emailAddress
     * @return 
     */
    private boolean validateEmailAddress(String emailAddress) {
        
        boolean retVal = true;
        
        try {
            InternetAddress addr = new InternetAddress(emailAddress, true);
        }
        catch (AddressException ex) {
            retVal = false;
        }
        
        return retVal;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        MailMessage emailer = new MailMessage("smtp.gmail.com", "hathor.dvl");
        emailer.addRecipient("iain@redmore.me.uk");
        emailer.setSubject("MailMessage class test");
        emailer.setContent("Test email with attachments from MailMessage");
        emailer.addAttachment("C:/hathor/data/TEST/TEST_B_0990_20131209_CLN.csv");
        emailer.sendMail("hathor.dvl", "h4th0rdvl");
    }
}
