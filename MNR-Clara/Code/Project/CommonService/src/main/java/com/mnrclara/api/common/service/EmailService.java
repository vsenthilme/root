package com.mnrclara.api.common.service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.mnrclara.api.common.config.PropertiesConfig;
import com.mnrclara.api.common.model.email.EMail;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailService {
	
	@Autowired
    private JavaMailSender javaMailSender;
	
	@Autowired
	PropertiesConfig propertiesConfig;
	
	/**
	 * sendMail
	 * @param email
	 * @throws MessagingException
	 * @throws IOException
	 */
	public void sendMail (EMail email) throws MessagingException, IOException {
		MimeMessage msg = javaMailSender.createMimeMessage();

        // true = multipart message
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        log.info("helper : " + email.getFromAddress());
        
        // Set From
        if (email.getFromAddress() != null && email.getFromAddress().isEmpty()) {
        	helper.setFrom(email.getFromAddress());
        } else {
        	helper.setFrom(propertiesConfig.getEmailFromAddress());
        }

        helper.setTo(email.getToAddress());
        if (email.getCcAddress() != null) {
        	helper.setCc(email.getCcAddress());
        } else {
        	helper.setCc(email.getToAddress());
        }
        
        helper.setSubject(email.getSubject());
        
        // true = text/html
        helper.setText(email.getBodyText(), true);
		
        javaMailSender.send(msg);
	}
	
	/**
	 * sendEmailWithAttachment
	 * @param email
	 * @throws MessagingException
	 * @throws IOException
	 */
	public void sendEmailWithAttachment(EMail email) throws MessagingException, IOException {
        MimeMessage msg = javaMailSender.createMimeMessage();

        // true = multipart message
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo(email.getToAddress());
        helper.setSubject(email.getSubject());

        // true = text/html
        helper.setText(email.getBodyText(), true);
        FileSystemResource file = new FileSystemResource("D:\\Murugavel.R\\Project\\7horses\\root\\MNR-Clara\\Code\\RND\\muru.png");
		helper.addAttachment(file.getFilename(), file);
		
        javaMailSender.send(msg);
    }
	
	/**
	 * send
	 * @param calendarRequest
	 * @return 
	 * @throws Exception
	 */
	public boolean send(EMail calendarRequest) throws Exception {
        try {
//            String from = "intake@montyramirezlaw.com";
            String from = calendarRequest.getFromAddress();
            log.info("from address: " + from);
            
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    		mimeMessage.addHeaderLine("method=REQUEST");
    		mimeMessage.addHeaderLine("charset=UTF-8");
    		mimeMessage.addHeaderLine("component=VEVENT");
    		mimeMessage.setFrom(new InternetAddress(from));

    		log.info("calendarRequest.getToAddress(): " + calendarRequest.getToAddress());
    		String toAddress = calendarRequest.getToAddress() + "," + from;
    		String recipients = calendarRequest.getToAddress() + ";" + from;
    		if (calendarRequest.getToAddress() != null && calendarRequest.getToAddress().indexOf(",") > 0) {
    			InternetAddress[] parse = InternetAddress.parse(toAddress , true);
    			mimeMessage.setRecipients(Message.RecipientType.TO,  parse);
    		} else {
    			//mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(calendarRequest.getToAddress()));
    			InternetAddress[] parse = InternetAddress.parse(toAddress , true);
    			mimeMessage.setRecipients(Message.RecipientType.TO,  parse);
    		}
    		mimeMessage.setSubject(calendarRequest.getSubject());
    		mimeMessage.setText(calendarRequest.getBodyText());
    		log.info("calendarRequest.getBodyText(): " + calendarRequest.getBodyText());
    		
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HHmmss");
    		log.info("Start date: " + formatter.format(calendarRequest.getMeetingStartTime()).replace(" ", "T"));
    		log.info("End date: " + formatter.format(calendarRequest.getMeetingEndTime()).replace(" ", "T"));
    		
    		log.info("calendarRequest.getMeetingStartTime() : " + calendarRequest.getMeetingStartTime());
    		log.info("calendarRequest.getMeetingEndTime() : " + calendarRequest.getMeetingEndTime());
    		
    		boolean hasNewline = calendarRequest.getBodyText().contains("\n");
    		String bodyTextNewLineReplacedWithDoubleSlashes = calendarRequest.getBodyText();
    		if (hasNewline) {
    			bodyTextNewLineReplacedWithDoubleSlashes = calendarRequest.getBodyText().replace("\n", "\\n");
    			log.info("bodyTextNewLineReplacedWithDoubleSlashes : " + bodyTextNewLineReplacedWithDoubleSlashes);
    		}
    		
            StringBuffer builder = new StringBuffer();
//            StringBuffer buffer = sb.append("BEGIN:VCALENDAR\n" + "METHOD:REQUEST\n" +
//                    "PRODID:-//Microsoft Corporation//Outlook 9.0 MIMEDIR//EN\n" +
//                    "VERSION:2.0\n" +
//                    "BEGIN:VTIMEZONE\n" + 
//                    "TZID:US/Central\n" + 
//                    "END:VTIMEZONE\n" +
//                    "BEGIN:VEVENT\n" +
//                    "ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:MAILTO:" + calendarRequest.getToAddress() + "\n" +
//                    "ORGANIZER:MAILTO:" + from + "\n" +
////                    "DESCRIPTION;LANGUAGE=en-US:" + calendarRequest.getBodyText() + "\n" +
//					"DTSTART:" + formatter.format(calendarRequest.getMeetingStartTime()).replace(" ", "T") + "\n" +
//					"DTEND:" + formatter.format(calendarRequest.getMeetingEndTime()).replace(" ", "T") + "\n" +
//                    "LOCATION:Conference room\n" +
//                    "TRANSP:OPAQUE\n" +
//                    "SEQUENCE:0\n" +
//                    "UID:040000008200E00074C5B7101A82E00800000000002FF466CE3AC5010000000000000000100\n" +
//                    " 000004377FE5C37984842BF9440448399EB02\n" +
//                    "DTSTAMP:20211106T120102Z\n" +
//                    "CATEGORIES:Meeting\n" +
//                    "DESCRIPTION:" + calendarRequest.getBodyText() + "\n\n" +
//                    "SUMMARY:" + calendarRequest.getSubject() + "\n" +
//                    "PRIORITY:5\n" +
//                    "CLASS:PUBLIC\n" +
//                    "BEGIN:VALARM\n" +
//                    "TRIGGER:PT1440M\n" +
//                    "ACTION:DISPLAY\n" +
//                    "DESCRIPTION:Reminder\n" +
//                    "END:VALARM\n" +
//                    "END:VEVENT\n" +
//                    "END:VCALENDAR");
            
            builder.append("BEGIN:VCALENDAR\n" +
                    "METHOD:REQUEST\n" +
                    "PRODID:Microsoft Exchange Server 2010\n" +
                    "VERSION:2.0\n" +
                    "BEGIN:VTIMEZONE\n" +
                    "TZID:US/Central\n" +
                    "END:VTIMEZONE\n" +
                    "BEGIN:VEVENT\n" +
                    "ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:MAILTO:" + recipients + "\n" +
					"CONTACT;CN:CLARA;MAILTO:" + from + "\n" +
                    "DESCRIPTION;LANGUAGE=en-US:" + bodyTextNewLineReplacedWithDoubleSlashes + "\n" +
                    "UID:" + calendarRequest.getUid() +"\n" +
                    "SUMMARY;LANGUAGE=en-US:" + calendarRequest.getSubject() + "\n" +
                    "DTSTART:" + formatter.format(calendarRequest.getMeetingStartTime()).replace(" ", "T") + "\n" +
                    "DTEND:" + formatter.format(calendarRequest.getMeetingEndTime()).replace(" ", "T") + "\n" +
                    "CLASS:PUBLIC\n" +
                    "PRIORITY:5\n" +
                    "DTSTAMP:20200922T105302Z\n" +
                    "TRANSP:OPAQUE\n" +
                    "STATUS:CONFIRMED\n" +
                    "SEQUENCE:$sequenceNumber\n" +
                    "LOCATION: \n" +
                    "BEGIN:VALARM\n" +
                    "DESCRIPTION:REMINDER\n" +
                    "TRIGGER;RELATED=START:-PT15M\n" +
                    "ACTION:DISPLAY\n" +
                    "END:VALARM\n" +
                    "END:VEVENT\n" +
                    "END:VCALENDAR");

            MimeBodyPart messageBodyPart = new MimeBodyPart();
    		messageBodyPart.setHeader("Content-Class", "urn:content-classes:calendarmessage");
    		messageBodyPart.setHeader("Content-ID", "calendar_message");
    		messageBodyPart.setContent("Content-type", "text/calendar;method=REQUEST");
    		messageBodyPart.setDataHandler(new DataHandler(
    				new ByteArrayDataSource(builder.toString(), "text/calendar;method=REQUEST;name=\"invite.ics\"")));

    		MimeMultipart multipart = new MimeMultipart();
    		multipart.addBodyPart(messageBodyPart);
    		mimeMessage.setContent(multipart);
    		
    		javaMailSender.send(mimeMessage);
    		log.info("Sent");
    		return true;
        } catch (MessagingException me) {
            me.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		return false;
    }
	
	/**
	 * sendCalendarInvite
	 * @param calendarRequest
	 * @return 
	 * @throws Exception
	 */
//	public boolean sendCalendarInvite(EMail calendarRequest) throws Exception {
//		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//		mimeMessage.addHeaderLine("method=REQUEST");
//		mimeMessage.addHeaderLine("charset=UTF-8");
//		mimeMessage.addHeaderLine("component=VEVENT");
////		mimeMessage.setFrom(new InternetAddress(propertiesConfig.getEmailFromAddress()));
//		mimeMessage.setFrom(new InternetAddress(calendarRequest.getFromAddress()));
//		
////		String to = "muruvel@gmail.com , karthik@tekclover.com, murugavel.r@prodapt.com";
//		if (calendarRequest.getToAddress() != null && calendarRequest.getToAddress().indexOf(",") > 0) {
//			InternetAddress[] parse = InternetAddress.parse(calendarRequest.getToAddress() , true);
//			mimeMessage.setRecipients(Message.RecipientType.TO,  parse);
//		} else {
//			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(calendarRequest.getToAddress()));
//		}
//
//		mimeMessage.setSubject(calendarRequest.getSubject());
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HHmmss");
//		StringBuilder builder = new StringBuilder();
//		builder.append("BEGIN:VCALENDAR\n" + "METHOD:REQUEST\n" +  "PRODID:-//Microsoft Corporation//Outlook 9.0 MIMEDIR//EN\n"
//               
//		//"PRODID:Microsoft Exchange Server 2010\n"
//				+ "VERSION:2.0\n" + "BEGIN:VTIMEZONE\n" + "TZID:US/Central\n" + "END:VTIMEZONE\n" + "BEGIN:VEVENT\n"
//				+ "ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:MAILTO:" + calendarRequest.getToAddress() + "\n"
//				+ "ORGANIZER;CN=" + calendarRequest.getSenderName() +":MAILTO:" + propertiesConfig.getEmailFromAddress() + "\n" + "DESCRIPTION;LANGUAGE=en-US:"
//				+ calendarRequest.getBodyText() + "\n" + "UID:" + calendarRequest.getUid() + "\n"
//				+ "SUMMARY;LANGUAGE=en-US:" + calendarRequest.getSubject() + "\nDTSTART:"
//				+ formatter.format(calendarRequest.getMeetingStartTime()).replace(" ", "T") + "\n" + "DTEND:"
//				+ formatter.format(calendarRequest.getMeetingEndTime()).replace(" ", "T") + "\n" + "CLASS:PUBLIC\n"
//				+ "PRIORITY:5\n" + "DTSTAMP:20210922T105302Z\n" + "TRANSP:OPAQUE\n" + "STATUS:CONFIRMED\n"
//				+ "SEQUENCE:$sequenceNumber\n" + "LOCATION;LANGUAGE=en-US:Microsoft Teams Meeting\n" + "BEGIN:VALARM\n"
//				+ "DESCRIPTION:REMINDER\n" + "TRIGGER;RELATED=START:-PT15M\n" + "ACTION:DISPLAY\n" + "END:VALARM\n"
//				+ "END:VEVENT\n" + "END:VCALENDAR");
//
//		MimeBodyPart messageBodyPart = new MimeBodyPart();
//		messageBodyPart.setHeader("Content-Class", "urn:content-classes:calendarmessage");
//		messageBodyPart.setHeader("Content-ID", "calendar_message");
//		messageBodyPart.setDataHandler(new DataHandler(
//				new ByteArrayDataSource(builder.toString(), "text/calendar;method=REQUEST;name=\"invite.ics\"")));
//
//		MimeMultipart multipart = new MimeMultipart();
//		multipart.addBodyPart(messageBodyPart);
//		mimeMessage.setContent(multipart);
//		System.out.println(builder.toString());
//
//		javaMailSender.send(mimeMessage);
//		log.info("Calendar invite sent");
//		return true;
//	}
}
