package com.iweb2b.api.integration.model.consignment.dto.jnt;

import java.util.Date;
import java.util.List;

import com.iweb2b.api.integration.model.consignment.dto.Items;
import com.iweb2b.api.integration.model.consignment.dto.Receiver;
import com.iweb2b.api.integration.model.consignment.dto.Sender;

import lombok.Data;

@Data
public class JNTOrderCreateRequest {

    private String customerCode;
    private String digest;
    private String serviceType;
    private String orderType;
    private String deliveryType;
    private String expressType;
    private String network;
    private Double length;
    private Date   sendStartTime;
    private Double weight;
    private String remark;
    private String billCode;
    private String batchNumber;
    private String txlogisticId;
    private String goodsType;
    private String totalQuantity;

    private Receiver receiver;      //Receiver
    private Sender sender;          //Sender

    private Double itemsValue;
    private String priceCurrency;
    private Double width;
    private Double offerFee;

    private List<Items> items;      //Item List

    private Date   sendEndTime;
    private Double height;
    private String payType;
    private Double operateType;
    private String platformName;
    private String customerAccount;
    private Boolean isUnpackEnabled;

}