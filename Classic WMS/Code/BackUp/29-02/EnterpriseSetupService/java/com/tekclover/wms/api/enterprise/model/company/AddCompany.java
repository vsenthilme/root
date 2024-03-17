package com.tekclover.wms.api.enterprise.model.company;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.Email;

import lombok.Data;

import java.util.Date;

@Data
public class AddCompany {

    private String languageId;
    private String companyId;
    private Long verticalId;
    private String description;
    private String verticalIdAndDescription;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String country;
    private Long zipCode;
    private Long currencyId;
    private String contactName;
    private String desigination;
    private String phoneNumber;
    private String emailId;
    private String registrationNo;
    private Long noOfPlants;
    private Long noOfOutlets;
    private Long noOfWarehouse;
    private Long deletionIndicator;

}
