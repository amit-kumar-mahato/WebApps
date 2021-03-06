package com.blbz.fundoonotes.utility;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.blbz.fundoonotes.responses.MailObject;

@Component
public class MailServiceProvider {

	/*
	 * @Autowired private static JavaMailSender javaMailSender;
	 */

	@Value("${EMAIL}")
	private String email;
	@Value("${EMAILPSWD}")
	private String pswd;
	public static void sendEmail(String toEmail, String subject, String body) {

		String fromEmail = System.getenv("EMAIL");//Setting email at runtime environment
		String password = System.getenv("EMAILPSWD");//Setting password at runtime environment

		Properties prop = new Properties();
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587");

		Authenticator auth = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};
		Session session = Session.getInstance(prop, auth);
		send(session, fromEmail, toEmail, subject, body);
	}

	private static void send(Session session, String fromEmail, String toEmail, String subject, String body) {
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromEmail, "BridgeLabz"));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
			message.setSubject(subject);
			message.setText(body);
			Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("exception occured while sending mail");

		}
	}
	
	@RabbitListener(queues = "rmq.rube.queue")
	public void recievedMessage(MailObject user) {
	
		sendEmail(user.getEmail(),user.getSubject(),user.getMessage());
		System.out.println("Recieved Message From RabbitMQ: " + user);
	}
}
