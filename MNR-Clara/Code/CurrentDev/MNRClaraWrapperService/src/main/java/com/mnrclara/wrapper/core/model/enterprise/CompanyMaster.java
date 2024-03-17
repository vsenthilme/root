package com.mnrclara.wrapper.core.model.enterprise;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CompanyMaster {

    private String companyId;
    private String languageId;
    private String verticalId;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String country;
    private Long currency;
    private Long zipCode;
    private String contactName;
    private String designation;
    private String phoneNumber;
    private String eMail;
    private String registrationNo;
    private Long noOfPlants;
    private Long noOfOutlets;
    private Long noOfWarehouses;
    private String createdBy;
}
