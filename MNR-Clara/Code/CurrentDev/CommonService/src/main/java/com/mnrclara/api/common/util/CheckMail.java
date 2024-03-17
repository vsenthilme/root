package com.mnrclara.api.common.util;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

public class CheckMail {

	public static void check(String host, String storeType, String user, String password) {
		try {

			// create properties field
			Properties properties = new Properties();

			properties.put("mail.pop3.host", host);
			properties.put("mail.pop3.port", "995");
			properties.put("mail.pop3.starttls.enable", "true");
			Session emailSession = Session.getDefaultInstance(properties);

			// create the POP3 store object and connect with the pop server
			Store store = emailSession.getStore("pop3s");

			store.connect(host, user, password);

			// create the folder object and open it
			Folder emailFolder = store.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);

			// retrieve the messages from the folder in an array and print it
			Message[] messages = emailFolder.getMessages();
			System.out.println("messages.length---" + messages.length);

			for (int i = 0, n = messages.length; i < n; i++) {
				Message message = messages[i];
				System.out.println("-------------@@---" + message);
				System.out.println("-------------33--------" + message.getFrom());
//				Address[] fromAddress = message.getFrom();
//	            String from = fromAddress[0].toString();
//	            System.out.println(from);
//	            
//				System.out.println("Email Number " + (i + 1));
////				System.out.println("Subject: " + message.getSubject());
//				System.out.println("From: " + message.getFrom()[0]);
////				System.out.println("Text: " + message.getContent().toString());

			}

			// close the store and folder objects
			emailFolder.close(false);
			store.close();

		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		/*
		 * spring.mail.host=smtp.office365.com
			spring.mail.port=587
			spring.mail.username=intake@montyramirezlaw.com
			spring.mail.password=TestPass@123
		 */

		String host = "smtp.office365.com";// change accordingly
		String mailStoreType = "pop3";
		String username = "intake@montyramirezlaw.com";// change accordingly
		String password = "TestPass@123";// change accordingly

		check(host, mailStoreType, username, password);

	}

}
