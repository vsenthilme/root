package com.mnrclara.api.management.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tblmattergenaccid")
public class MatterNumberDropDown {

	@Id
	@Column(name = "MATTER_NO")
	private String matterNumber;

	@Column(name = "MATTER_TEXT")
	private String matterDescription;

	@Column(name = "IS_DELETED")
	private Long deletionIndicator;
}
