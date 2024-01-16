package com.iwmvp.api.master.model.numberrange;

import java.util.Date;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
/*
 * `LANG_ID`,`COMP_ID`, `NUM_RAN_CODE`,'NUM_RAN_OBJ;
 */
@Table(
		name="tblmvpnumberrange",
		uniqueConstraints={
				@UniqueConstraint(
						name = "unique_key_mvp_numberrange",
						columnNames = {"LANG_ID", "COMP_ID", "NUM_RAN_CODE","NUM_RAN_OBJ"})
		}
)
@IdClass(NumberRangeCompositeKey.class)
public class NumberRange {
	@Id
	@Column(name = "LANG_ID", columnDefinition = "nvarchar(5)")
	private String languageId;
	@Id
	@Column(name = "COMP_ID", columnDefinition = "nvarchar(50)")
	private String companyId;
	@Id
	@Column(name = "NUM_RAN_CODE")
	private Long numberRangeCode;
	@Id
	@Column(name="NUM_RAN_OBJ",columnDefinition = "nvarchar(100)")
	private String numberRangeObject;
	@Column(name = "NUM_RAN_FROM")
	private Long numberRangeFrom;
	@Column(name = "NUM_RAN_TO")
	private Long numberRangeTo;
	@Column(name = "NUM_RAN_CURRENT")
	private Long numberRangeCurrent;
	@Column(name="NUM_RAN_STATUS")
	private Long numberRangeStatus;
	@Column(name = "IS_DELETED")
	private Long deletionIndicator;
	@Column(name = "CTD_BY")
	private String createdBy;
	@Column(name = "CTD_ON")
    private Date createdOn = new Date();
	@Column(name = "UTD_BY")
    private String updatedBy;
	@Column(name = "UTD_ON")
	private Date updatedOn = new Date();
}
