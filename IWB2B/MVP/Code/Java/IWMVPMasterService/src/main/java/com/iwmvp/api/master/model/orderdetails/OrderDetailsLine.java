package com.iwmvp.api.master.model.orderdetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tblmvporderdetailsline")
@Where(clause = "IS_DELETED=0")
public class OrderDetailsLine {

    @Column(name = "LANG_ID", columnDefinition = "nvarchar(5)")
    private String languageId;

    @Column(name = "COMP_ID", columnDefinition = "nvarchar(50)")
    private String companyId;

    @Column(name="ORDER_ID")
    private Long  orderId;

    @Column(name="REF_NO",columnDefinition = "nvarchar(50)")
    private String referenceNo;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="LINE_NO")
    private Long lineNo;
    @Column(name="COMMODITY_NAME",columnDefinition = "nvarchar(500)")
    private String commodityName;
    @Column(name="NUM_PIECES",columnDefinition = "nvarchar(25)")
    private String noOfPieces;
    @Column(name="DECLARED_VALUE",columnDefinition = "nvarchar(50)")
    private String declaredValue;
    @Column(name="DIMENSION_UNIT",columnDefinition = "nvarchar(25)")
    private String dimensionUnit;
    @Column(name="LENGTH",columnDefinition = "nvarchar(25)")
    private String length;
    @Column(name="WIDTH",columnDefinition = "nvarchar(25)")
    private String width;
    @Column(name="HEIGHT",columnDefinition = "nvarchar(25)")
    private String height;
    @Column(name="WEIGHT_UNIT",columnDefinition = "nvarchar(25)")
    private String weightUnit;
    @Column(name="WEIGHT",columnDefinition = "nvarchar(25)")
    private String weight;
    @Column(name = "IS_DELETED")
    private Long deletionIndicator;
    @Column(name = "CTD_BY",columnDefinition = "nvarchar(50)")
    private String createdBy;
    @Column(name = "CTD_ON")
    private Date createdOn;
    @Column(name = "UTD_BY",columnDefinition = "nvarchar(50)")
    private String updatedBy;
    @Column(name = "UTD_ON")
    private Date updatedOn;
}
