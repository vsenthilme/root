package com.mnrclara.api.crm.model.itform;


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
