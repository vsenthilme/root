package com.flourish.b2b.api.gateway.model.consignmentoutbound;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "consignment_outbound_error_log")
public class ErrorOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "error_id")
    private Long errorId;
    
    @Column(name = "source_json")
    private String sourceJson;
    
    @Column(name = "received_date")
    private Date receivedDate = new Date();
    
    @Column(name = "remarks")
    private String remarks;
}
