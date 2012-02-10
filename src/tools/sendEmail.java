/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

//you'll need mail.jar
// http://www.oracle.com/technetwork/java/index-138643.html

import java.util.Properties;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;


// see : http://www.tutorialspoint.com/java/java_sending_email.htm


// note: this will only work if you are at UP, otherwise you'll need to authenticate 
//       to send email, which is messy to do in java

/**
 *
 * @author toddbodnar
 */
public class sendEmail {
    public static void send(String from, String to[], String subject, String message)
    {
        Properties p = System.getProperties();
        p.setProperty("mail.smtp.host", "smtp.psu.edu"); 

        Session s = Session.getDefaultInstance(p);

        
        try{
         // Create a default MimeMessage object.
         MimeMessage m = new MimeMessage(s);

         // Set From: header field of the header.
         m.setFrom(new InternetAddress(from));

         // Set To: header field of the header.
         for(String t:to)
            m.addRecipient(Message.RecipientType.TO,new InternetAddress(t));

         // Set Subject: header field
         m.setSubject(subject);

         // Now set the actual message
         m.setText(message);

         // Send message
         Transport.send(m);
         System.out.println("Sent message successfully....");
      }catch (MessagingException mex) {
         mex.printStackTrace();
      }
    }
    public static void main(String args[])
    {
        send("tjb5215@psu.edu",new String[]{"tjb5215@psu.edu"}, "test", "message");
    }
}
