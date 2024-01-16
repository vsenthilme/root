package com.mnrclara.api.setup.model.language;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
/*
 * `LANG_ID`, `CLASS_ID`
 */
@Table(
		name = "tbllanguageid", 
		uniqueConstraints = { 
				@UniqueConstraint (
						name = "unique_key_language", 
						columnNames = { "LANG_ID", "CLASS_ID"})
				}
		)
@IdClass(LanguageCompositeKey.class)
public class Language { 
	
	@Id
	@Column(name = "LANG_ID")
	private String languageId;
	
	@Id
	@Column(name = "CLASS_ID")
	private Long classId;
	
	@Column(name = "LANG_TEXT")
	private String languageDescription;
	
	@Column(name = "IS_DELETED")
	private Long deletionIndicator = 0L;

	@Column(name = "CTD_BY")
	private String createdBy;

	@Column(name = "CTD_ON")
    private Date createdOn = new Date();

	@Column(name = "UTD_BY")
    private String updatedBy;

	@Column(name = "UTD_ON")
	private Date updatedOn = new Date();
}
