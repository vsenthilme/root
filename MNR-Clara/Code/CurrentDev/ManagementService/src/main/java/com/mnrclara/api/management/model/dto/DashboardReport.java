package com.mnrclara.api.management.model.dto;

import java.util.List;

import lombok.Data;

@Data
public class DashboardReport {

	private List<String> listOfValues;
	private int count;
}
