package com.iweb2b.api.integration.model.consignment.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tblqualitycheckimagelist")
public class QualityCheckImageListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QUALITY_CHECK_IMAGE_LIST_ID")
    private long quality_check_image_list_id;

    @Column(name = "CONSIGNMENT_WEBHOOK_ID")
    private Long consignment_webhook_id;

    @Column(name = "Description", columnDefinition = "nvarchar(255)")
    private String description;
}
