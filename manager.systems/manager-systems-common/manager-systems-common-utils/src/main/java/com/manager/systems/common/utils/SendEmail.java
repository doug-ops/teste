package com.manager.systems.common.utils;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

//import com.sun.mail.util.MailSSLSocketFactory;

public class SendEmail 
{
	public static boolean sendEmailAttachment(final String to, final String body, final String titleEmail, final byte[] attachment, final Properties properties){
		boolean sended = false;		
		final String canSendEmail = properties.getProperty("habilita.envio.email");
		if("true".equalsIgnoreCase(canSendEmail)){
//			MailSSLSocketFactory sf;
//			try {
//				 sf = new MailSSLSocketFactory();
//				 sf.setTrustAllHosts(true);
//				 properties.put("mail.smtp.ssl.enable", "true");  
//				 properties.put("mail.smtp.ssl.socketFactory", sf); 
//			} catch (GeneralSecurityException e) {
//				throw new RuntimeException("send mail:" + e.getMessage(), e);
//			}  
			final Session session = Session.getInstance(properties,  new javax.mail.Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(properties.getProperty("mail.username"), properties.getProperty("mail.password"));
				}
			});
			 
			try 
			{
				final Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(properties.getProperty("mail.from")));
				String[] toArray = to.split(";");
				final InternetAddress[] toAddresses = new InternetAddress[toArray.length];
				for (int i = 0; i < toArray.length; i++) 
				{
					toAddresses[i] = new InternetAddress(toArray[i]); 
				}
				message.setRecipients(Message.RecipientType.TO, toAddresses);
			        
				//message.addRecipient(RecipientType.TO, new InternetAddress(to));
				//message.addRecipient(RecipientType.BCC, new InternetAddress(properties.getProperty("mail.from")));
				message.setSubject(titleEmail);
				
				// Create the message part
		        BodyPart messageBodyPart = new MimeBodyPart();

		        // Now set the actual message
		        messageBodyPart.setText(body);

		        // Create a multipar message
		        final Multipart multipart = new MimeMultipart();

		        // Set text message part
		        multipart.addBodyPart(messageBodyPart);

		        if(attachment!=null) {
		        // Part two is attachment
		        messageBodyPart = new MimeBodyPart();
		        
		        final ByteArrayDataSource dataSource = new ByteArrayDataSource(attachment, "application/pdf");
   	            messageBodyPart.setDataHandler(new DataHandler(dataSource));
		        messageBodyPart.setFileName(titleEmail+".pdf");
		        multipart.addBodyPart(messageBodyPart);
		        }

		        // Send the complete message parts
		        message.setContent(multipart);
		         
				Transport.send(message);
				sended = true;
	 		} catch (final MessagingException e) {
				throw new RuntimeException(e);
			}
		}
		return sended;
	}
}