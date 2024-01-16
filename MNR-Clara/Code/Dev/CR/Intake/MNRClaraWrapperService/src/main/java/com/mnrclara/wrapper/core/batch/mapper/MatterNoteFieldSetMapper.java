package com.mnrclara.wrapper.core.batch.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import com.mnrclara.wrapper.core.batch.dto.MatterNote;

public class MatterNoteFieldSetMapper implements FieldSetMapper<MatterNote> {
	/*
	 * LANG_ID	CLASS_ID	CLIENT_ID	MATTER_NO	NOTE_NO	NOTE_TEXT	
	 * STATUS_ID NOTE_TYP_ID	Isdeleted	CTD_BY	CTD_ON	UTD_BY	UTD_ON
	 */
	@Override
	public MatterNote mapFieldSet(FieldSet fieldSet) {
		return new MatterNote(
			fieldSet.readString("languageId"),
			fieldSet.readLong("classId"),
			fieldSet.readString("clientId"),
			fieldSet.readString("matterNumber"),
			fieldSet.readString("notesNumber"),
			fieldSet.readString("notesDescription"),
			fieldSet.readLong("statusId"),
			fieldSet.readLong("noteTypeId"),
			fieldSet.readLong("deletionIndicator"),
			fieldSet.readString("createdBy"),
			fieldSet.readDate("createdOn"),
			fieldSet.readString("UpdatedBy"),
			fieldSet.readDate("updatedOn")
		);
	}
}