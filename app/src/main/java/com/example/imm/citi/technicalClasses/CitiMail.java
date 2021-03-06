package com.example.imm.citi.technicalClasses;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by Sujoy on 4/7/2017.
 */

public class CitiMail {
    final String emailPort = "587";// gmail's smtp port
    final String smtpAuth = "true";
    final String starttls = "true";
    final String emailHost = "smtp.gmail.com";

    String fromEmail;
    String fromPassword;
    String toEmailList;
    String emailSubject;
    String emailBody;

    Properties emailProperties;
    Session mailSession;
    MimeMessage emailMessage;

    public CitiMail() {

    }

    public CitiMail(String toEmailList, String code) {
        fromEmail = "citiscopeapp@gmail.com";
        fromPassword = "iticepocs32";
        this.toEmailList = toEmailList;
        emailSubject = "Confirmation Code";

        StringBuilder strBld = new StringBuilder("<p>Your Confirmation Code:</p><p> <b>" + code + "</b></p>").append(System.lineSeparator());
        strBld.append("<i>Thank you for using CITISCOPE.</i>");

        String sth = "<html>" +
                "                    <head>" +
                "                        <title>CITISCOPE</title>" +
                "                    </head>" +
                "                    <style>" +
                "                                p {" +
                "                            font-family: &quot;Calibri&quot;, Sans-serif;" +
                "                        }" +
                "                    </style>" +
                "                    <body>" +
                "                        <img src=&quot;@drawable/mail_logo&quot; alt=&quot;citiscope&quot; style=&quot;width:400px;height:90px;display: block;margin:auto;&quot;>" +
                "                        <p style=&quot;font-size: 28px;&quot; align=&quot;center&quot;>Your Confirmation Code</p>" +
                "                        <div style=&quot;background-color:#e2d6f5;padding:0px;display: block;margin:auto; width: 150px&quot;>" +
                "                            <p style=&quot;font-size: 28px;&quot; align=&quot;center&quot;><b>"+code+"</b></p>" +
                "                        </div>" +
                "                        <p style=&quot;font-size: 25px&quot; align=&quot;center&quot;>Thank you for using CITISCOPE</p>" +
                "                    </body>" +
                "                </html>";

        emailBody = strBld.toString();
        //emailBody = sth;

        emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.port", emailPort);
        emailProperties.put("mail.smtp.auth", smtpAuth);
        emailProperties.put("mail.smtp.starttls.enable", starttls);
        Log.i("GMail", "Mail server properties set.");
    }



    public MimeMessage createEmailMessage() throws AddressException,
            MessagingException, UnsupportedEncodingException {

        mailSession = Session.getDefaultInstance(emailProperties, null);
        emailMessage = new MimeMessage(mailSession);

        emailMessage.setFrom(new InternetAddress(fromEmail, fromEmail));
        Log.i("GMail","toEmail: "+toEmailList);
        emailMessage.addRecipient(Message.RecipientType.TO,
                new InternetAddress(toEmailList));

        emailMessage.setSubject(emailSubject);
        emailMessage.setContent(emailBody, "text/html");// for a html email
        // emailMessage.setText(emailBody);// for a text email
        Log.i("GMail", "Email Message created.");
        return emailMessage;
    }

    public void sendEmail() throws AddressException, MessagingException {

        Transport transport = mailSession.getTransport("smtp");
        transport.connect(emailHost, fromEmail, fromPassword);
        Log.i("GMail","allrecipients: "+emailMessage.getAllRecipients());
        transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
        transport.close();
        Log.i("GMail", "Email sent successfully.");
    }
}
