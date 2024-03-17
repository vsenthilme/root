package com.mnrclara.api.management.model.matterrate;

import java.io.Serializable;

import lombok.Data;

@Data
public class MatterRateCompositeKey implements Serializable { 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8683171990037148764L;
	/*
	 * `MATTER_NO`, `TK_CODE`
	 */
	
	private String matterNumber;
	private String timeKeeperCode;
}
