package com.mnrclara.wrapper.core.model.crm.itform;


import lombok.Data;

@Data
public class ClientEntry {

	private String dateOfEntry;
	private String placeOfArrival;
	private String wereYouInspectedByAnImmigrationOfficer;
	private String unitedStatesEnteredThrough;
	private String kindOfDocuments;
	private String hiddenLocation;
}
