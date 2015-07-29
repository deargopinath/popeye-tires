package com.popeyetires.popeyetires.service;

import java.util.HashMap;

import com.popeyetires.popeyetires.core.models.TireInfo;

public interface TireService {
	public TireInfo getTireInformation(String tireName);

	public HashMap<String, String> getTireFeaturesInformation(String tireName);
	
	public String[] getRelatedTireInformation(String tireName);
}
