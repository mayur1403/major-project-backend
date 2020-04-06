package com.example.demo.service;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import lombok.NoArgsConstructor;
@Service
@NoArgsConstructor
public class SendEmailService {
private JavaMailSender mailSender= new JavaMailSenderImpl();

    public String sendMail(String email) {
    	String otp = generateOtp();
//		email = email.replaceAll("%40", "@");
//       // email = email.substring(0, email.length()-1);
//        System.out.println(email);
//		SimpleMailMessage msg = new SimpleMailMessage();  
//        msg.setTo(email);
//        msg.setFrom("mayurpehlwani@gmail.com");
//        
//        msg.setSubject("OTP to register to our job portal");
//        msg.setText(otp+" is your one time OTP.");
//        try{
//            mailSender.send(msg);
//        }
//        catch (MailException ex) {
//            
//            System.err.println(ex.getMessage());
//        }
    	 final String username = "mayurpehelwani@gmail.com";
         final String password = "***";

         Properties prop = new Properties();
 		prop.put("mail.smtp.host", "smtp.gmail.com");
         prop.put("mail.smtp.port", "587");
         prop.put("mail.smtp.auth", "true");
         prop.put("mail.smtp.starttls.enable", "true"); //TLS
         
         Session session = Session.getInstance(prop,
                 new javax.mail.Authenticator() {
                     protected PasswordAuthentication getPasswordAuthentication() {
                         return new PasswordAuthentication(username, password);
                     }
                 });

         try {

             Message message = new MimeMessage(session);
             message.setFrom(new InternetAddress("from@gmail.com"));
             message.setRecipients(
                     Message.RecipientType.TO,
                     InternetAddress.parse(email)
             );
             message.setSubject("otp");
             message.setText(otp);

             Transport.send(message);

             System.out.println("Done");

         } catch (MessagingException e) {
             e.printStackTrace();
         }
        return otp;
    }
    public String generateOtp() {
		int length = 4;
		String numbers = "1234567890";
	      Random random = new Random();
	      String otp = "";

	      for(int i = 0; i< length ; i++) {
	         otp+= ""+ numbers.charAt(random.nextInt(numbers.length()));
	      }
	      return otp;
	}
}
