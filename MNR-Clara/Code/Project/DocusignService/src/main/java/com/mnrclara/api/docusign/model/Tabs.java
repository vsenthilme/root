package com.mnrclara.api.docusign.model;

import java.util.List;

import lombok.Data;

@Data
public class Tabs {

	private List<SignHereTabs> signHereTabs;
	private List<TextTabs> textTabs;
	private List<TextCustomFields> textCustomFields;
}
