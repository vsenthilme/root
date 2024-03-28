package com.tekclover.wms.api.idmaster.service;

import com.tekclover.wms.api.idmaster.config.PropertiesConfig;
import com.tekclover.wms.api.idmaster.controller.exception.BadRequestException;
import com.tekclover.wms.api.idmaster.model.email.EMailDetails;
import com.tekclover.wms.api.idmaster.model.email.OrderCancelInput;
import com.tekclover.wms.api.idmaster.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SendMailService {

    private static final String ACCESS_TOKEN = null;

    @Autowired
    private PropertiesConfig propertiesConfig;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private EMailDetailsService eMailDetailsService;

    public void sendMail(OrderCancelInput orderCancelInput) throws MessagingException, IOException {
        //Send Email
        log.info("Send Mail Initiated " + new Date());

        List<EMailDetails> userEMail = eMailDetailsService.getEMailDetailsList();
        Set<String> toAddressList = new HashSet<>();
        Set<String> ccAddressList = new HashSet<>();

        if (userEMail == null || userEMail.isEmpty()) {
            throw new BadRequestException("Email Id is Empty");
        }

        if (userEMail != null && !userEMail.isEmpty()) {
            toAddressList = userEMail.stream().map(EMailDetails::getToAddress).collect(Collectors.toSet());
            ccAddressList = userEMail.stream().map(EMailDetails::getCcAddress).collect(Collectors.toSet());
        }
        String toAddress = "";
        String ccAddress = "";

        for (String dbToAddress : toAddressList) {

            if (dbToAddress != null) {
                toAddress = dbToAddress + "," + toAddress;
            }
        }
        for (String dbCcAddress : ccAddressList) {
            if (dbCcAddress != null) {
                ccAddress = dbCcAddress + "," + ccAddress;
            }
        }
        String localDate = DateUtils.getCurrentDateWithoutTimestamp();
        String c_id = orderCancelInput.getCompanyCodeId();
        String plant_id = orderCancelInput.getPlantId();
        String ref_doc_no = orderCancelInput.getRefDocNumber();

        String emailSubject = "Almailem - AutoLab / Amghara - Order Failed Status - " + ref_doc_no + ", " + c_id + ", " +  plant_id + ", " + localDate;
        String emailBodyText = "Dear Almailem Team,<br><br>" + "Please find the failed order details for your reference" +
                "<br><br> OrderNo   : " + ref_doc_no +
                "<br> CompanyCode   : " + c_id +
                "<br> BranchCode    : " + plant_id +
                "<br> Warehouse     : " + orderCancelInput.getWarehouseId() +
                "<br> OrderType     : " + orderCancelInput.getReferenceField2() +
                "<br> ErrorDesc     : " + orderCancelInput.getRemarks() +
                "<br>" + orderCancelInput.getReferenceField1() +
                "<br><br>Regards<br>Operations Team - Almailem";

        EMailDetails email = new EMailDetails();

        email.setSenderName("Almailem-Support");
        email.setSubject(emailSubject);
        email.setBodyText(emailBodyText);
        email.setToAddress(toAddress);
        email.setCcAddress(ccAddress);
        sendMail(email);
    }

    /**
     * sendMail
     *
     * @param email
     * @throws MessagingException
     * @throws IOException
     */
    public void sendMail(EMailDetails email) throws MessagingException, IOException {

        MimeMessage msg = null;
        MimeMessageHelper helper = null;
        try {
            msg = javaMailSender.createMimeMessage();
            helper = new MimeMessageHelper(msg, true);
            log.info("helper (From Address): " + email.getFromAddress());

            // Set From
            if (email.getFromAddress() != null && email.getFromAddress().isEmpty()) {
                helper.setFrom(email.getFromAddress());
            } else {
                helper.setFrom(propertiesConfig.getEmailFromAddress());
            }

            helper.setTo(InternetAddress.parse(email.getToAddress()));

            log.info("Email: To Address- " + email.getToAddress());

            if (email.getCcAddress() != null) {

                helper.setCc(InternetAddress.parse(email.getCcAddress()));

                log.info("Email: Cc Address- " + email.getCcAddress());

            } else {
                helper.setCc(InternetAddress.parse(email.getToAddress()));
            }

            helper.setSubject(email.getSubject());

            // true = text/html
            helper.setText(email.getBodyText(), true);
            javaMailSender.send(msg);
            log.info("Failed Order Detail Mail sent successful");
        } catch (Exception e) {
            e.printStackTrace();
            helper.setFrom(propertiesConfig.getEmailFromAddress());
            helper.setTo("raj@tekclover.com");
            helper.setCc("senthil.v@tekclover.com");
            helper.setSubject("Failed Order Details sending through eMail Failed");
            helper.setText("Failed Order Details sending through eMail Failed", true);
            javaMailSender.send(msg);
            log.info("Failed Order Detail Mail sent Unsuccessful");
            throw new BadRequestException("Mail Sent Failed" + e);
        }
    }
}
