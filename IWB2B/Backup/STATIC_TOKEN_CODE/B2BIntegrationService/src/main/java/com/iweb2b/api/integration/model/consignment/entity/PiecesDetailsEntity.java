package com.iweb2b.api.integration.model.consignment.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tblpiecesdetail")
public class PiecesDetailsEntity {
	
    @Id
    @Column(name = "PIECES_ID")
    private Long piecesId;

    @Column(name="CONSIGNMENT_ID")
    private Long consignmentId;

    @Column(name="DESCRIPTION", columnDefinition = "nvarchar(255)")
    private String description;

    @Column(name="DECLARED_VALUE")
    private Double declared_value;

    @Column(name="VOLUME", columnDefinition = "nvarchar(100)")
    private String volume;

    @Column(name="WEIGHT")
    private Double weight;

    @Column(name="HEIGHT")
    private Double height;

    @Column(name="LENGTH")
    private Long length;

    @Column(name="WIDTH")
    private Long width;

    @Column(name="WEIGHT_UNIT", columnDefinition = "nvarchar(100)")
    private String weight_unit;

    @Column(name="DIMENSION_UNIT", columnDefinition = "nvarchar(100)")
    private String dimension_unit;

    @Column(name="VOLUME_UNIT", columnDefinition = "nvarchar(100)")
    private String volume_unit;

    @Column(name="PIECE_PRODUCT_CODE", columnDefinition = "nvarchar(100)")
    private String piece_product_code;

    @Column(name="CHARGEABLE_WEIGHT")
    private Long chargeable_weight;

    @Column(name="VOLUMETRIC_WEIGHT")
    private Long volumetric_weight;

    @Column(name="DENORMALIZED_WIDTH")
    private Long denormalized_width;

    @Column(name="DENORMALIZED_HEIGHT")
    private Long denormalized_height;

    @Column(name="DENORMALIZED_LENGTH")
    private Long denormalized_length;

    @Column(name="DENORMALIZED_VOLUME")
    private Long denormalized_volume;

    @Column(name="DENORMALIZED_WEIGHT")
    private Double denormalized_weight;

    @Column(name="DENORMALIZED_VOLUME_UNIT", columnDefinition = "nvarchar(100)")
    private String denormalized_volume_unit;

    @Column(name="DENORMALIZED_WEIGHT_UNIT", columnDefinition = "nvarchar(100)")
    private String denormalized_weight_unit;

    @Column(name="DENORMALIZED_DIMENSION_UNIT", columnDefinition = "nvarchar(100)")
    private String denormalized_dimension_unit;
}