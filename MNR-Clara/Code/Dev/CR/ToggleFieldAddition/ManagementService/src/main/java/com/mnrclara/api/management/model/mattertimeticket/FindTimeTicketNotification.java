package com.mnrclara.api.management.model.mattertimeticket;


import lombok.Data;

import java.util.List;

@Data
public class FindTimeTicketNotification {

    private List<String> timeKeeperCode;
    private List<Long> weekOfYear;
    private Long year;
    private List<Long> classId;
}
