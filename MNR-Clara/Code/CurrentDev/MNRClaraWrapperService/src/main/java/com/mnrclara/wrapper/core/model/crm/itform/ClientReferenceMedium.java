package com.mnrclara.wrapper.core.model.crm.itform;


import java.util.List;

import lombok.Data;

@Data
public class ClientReferenceMedium {

	private List<String> listOfMediumAboutFirm;
	private String referredByOurClient;
	private String knownByRadio;
	private String referredByOthers;
}
