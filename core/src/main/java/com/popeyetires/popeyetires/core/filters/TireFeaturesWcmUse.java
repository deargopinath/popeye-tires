package com.popeyetires.popeyetires.core.filters;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.WCMUse;
import com.popeyetires.popeyetires.service.TireService;

public class TireFeaturesWcmUse extends WCMUse {
	
	private final Logger logger = LoggerFactory.getLogger(TireFeaturesWcmUse.class);
	private HashMap<String, String> tireFeatures;
	private String locale;
	
	@Override
	public void activate() throws Exception {
		if(getCurrentPage().getPath().contains("/en/")) {
			locale = "en-us";
		} else if(getCurrentPage().getPath().contains("/fr/")) {
			locale = "fr-ca";
		}
		logger.info("Locale :" + locale);
		String tireName = getCurrentPage().getName();
		TireService tireService = getSlingScriptHelper().getService(TireService.class);
		tireFeatures = tireService.getTireFeaturesInformation(tireName);
	}

	public HashMap<String, String> getTireFeatures() {
		return tireFeatures;
	}
	
	public String getLocale() {
		return locale;
	}
}
