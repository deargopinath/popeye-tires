package com.popeyetires.popeyetires.core.filters;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Value;

import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.WCMUse;
import com.popeyetires.popeyetires.core.models.TireInfo;

public class RelatedTiresWcmUse extends WCMUse {
	private final Logger logger = LoggerFactory.getLogger(RelatedTiresWcmUse.class);
	
	private List<TireInfo> relatedTireInfo;
	private String locale;
	private String enLang = "en-us";
	private String frLang = "fr-ca";

	@Override
	public void activate() throws Exception {
		if(getCurrentPage().getPath().contains("/en/")) {
			locale = "en-us";
		} else if(getCurrentPage().getPath().contains("/fr/")) {
			locale = "fr-ca";
		}
		try {
			Property prop = null;
			TireInfo tireInfo = null;
			logger.info("Resource :" + getResource());
			Node currentNode = getResource().adaptTo(Node.class);
			logger.info("currentNode :: " + currentNode);
			if (currentNode.hasProperty("relatedTires")) {
				prop = currentNode.getProperty("relatedTires");
				if (prop != null) {
					JSONObject productJson = null;
					Value[] values = null;
					if (prop.isMultiple()) {
						values = prop.getValues();
					} else {
						values = new Value[1];
						values[0] = prop.getValue();
					}
					logger.info("The elements :" + values.length);
					relatedTireInfo = new ArrayList<TireInfo>();

					for (Value val : values) {
						logger.info("value :" + val);
						productJson = new JSONObject(val.getString());
						logger.info("Tire JSON :" + productJson.getString("title"));
						tireInfo = new TireInfo();
						tireInfo.setDescriptionEn(productJson.getString("descriptionEn"));
						tireInfo.setDescriptionFr(productJson.getString("descriptionFr"));
						tireInfo.setTitle(productJson.getString("title"));
						tireInfo.setTreadDepth(productJson.getString("treadDepth"));
						tireInfo.setWarrantyInKM(productJson.getString("warrantyInKM"));
						tireInfo.setWarrantyInMiles(productJson.getString("warrantyInMiles"));
						tireInfo.setWinterTire(productJson.getString("winterTire").equals("N") ? true : false);
						tireInfo.setSnowTire(productJson.getString("snowTire").equals("Y") ? true : false);
						tireInfo.setTireImage("/content/dam/bst/tires/models/" + tireInfo.getTitle().toLowerCase().replaceAll(" ", "-").replaceAll("/", "-") + "/quick-look.png");
						relatedTireInfo.add(tireInfo);
					}
				}
				logger.info("relatedTires List size :" + relatedTireInfo.size());
				logger.info(relatedTireInfo.toString());
			} else {
				logger.info("relatedTireInfo is empty");
			}
			
			//TireService tireService = getSlingScriptHelper().getService(TireService.class);
			//this.relatedTireInfo = tireService.getRelatedTireInformation(tireName);
		} catch(Exception e) {
			e.printStackTrace();
			logger.error("Exception :", e);
		}
	}

	public String getEnLang() {
		return enLang;
	}

	public String getFrLang() {
		return frLang;
	}

	public String getLocale() {
		return locale;
	}
	
	public List<TireInfo> getRelatedTireInfo() {
		return relatedTireInfo;
	}
}
