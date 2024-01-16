package com.mnrclara.api.crm.service;

import com.mnrclara.api.crm.model.timeticket.TimeTicketImpl;
import com.mnrclara.api.crm.repository.InquiryRepository;
import com.mnrclara.api.crm.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class TimeTicketService {

    @Autowired
    InquiryRepository inquiryRepository;

    @Autowired
    NotificationService notificationService;

    //Cron job run every monday @10AM
    @Scheduled(cron = "0 0 10 * * 1")
    public void sendTicketTicketNotification() throws ParseException {
        //calculate date one week before (Monday)
        LocalDate from = LocalDate.now();
        from = from.minusDays(7);
        //calculate date one day before (Sunday)
        LocalDate to = LocalDate.now();
        to = to.minusDays(1);

        Date fromDate = Date.from(from.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date toDate = Date.from(to.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date[] dates = DateUtils.addTimeToDatesForSearch(fromDate, toDate);
        fromDate = dates[0];
        toDate = dates[1];

        List<TimeTicketImpl> timeTicketList = this.inquiryRepository.getTimeTicketFrNotification(fromDate,toDate);
        if (timeTicketList != null && !timeTicketList.isEmpty()) {
            for (TimeTicketImpl timeTicket : timeTicketList) {
                Double timeTicketHours = timeTicket.getTimeTicketHours();
                if(timeTicketHours != null) {
                    //timeTicketHours.setScale(2, RoundingMode.FLOOR);
                	DecimalFormat decfor = new DecimalFormat("0.00");
            		timeTicketHours = Double.valueOf(decfor.format(timeTicketHours));
                }
                
                this.notificationService.saveNotifications(
                        Arrays.asList(timeTicket.getTkCode()),
                        null, "Hi " +
                                (timeTicket.getTimeKeeperName() != null ? timeTicket.getTimeKeeperName() : timeTicket.getTkCode()) +
                                ", Billable hours for the last week was " + timeTicketHours + "(Target 35 Hours) ",
                        "Time Ticket",
                        new Date(), "System");
            }
        }
    }
    
    public static void main(String[] args) {
		Double d = 12.78777777777;
		DecimalFormat decfor = new DecimalFormat("0.00");
		System.out.println(decfor.format(2));
	}
}
