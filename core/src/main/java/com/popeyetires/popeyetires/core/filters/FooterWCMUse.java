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
import com.popeyetires.popeyetires.core.models.Footer;

public class FooterWCMUse extends WCMUse {
	private final Logger logger = LoggerFactory.getLogger(FooterWCMUse.class);
	
	private List<Footer> tiresByVehicles;
	private List<Footer> tiresByType;
	private List<Footer> support;
	private List<Footer> companyInfo;
	private String locale;
	

	public List<Footer> getTiresByType() {
		return tiresByType;
	}

	public List<Footer> getSupport() {
		return support;
	}

	public List<Footer> getCompanyInfo() {
		return companyInfo;
	}

	public List<Footer> getTiresByVehicles() {
		return tiresByVehicles;
	}

	@Override
	public void activate() throws Exception {
		System.out.println("Inside activate method FooterWCMUse Component");
		logger.info("Inside activate method FooterWCMUse Component");
		
		String currentPath = getCurrentPage().getPath();
		if(currentPath.contains("/en/")) {
			this.locale = "en-us"; 
		} else if(currentPath.contains("/fr/")) {
			this.locale = "fr-ca";
		}
		
		try {
			Property prop = null;
			Footer footer = null;
			Node currentNode = getResource().adaptTo(Node.class);
			System.out.println("currentNode :: "+currentNode);
			if (currentNode.hasProperty("tiresbyvehicle")) {
				prop = currentNode.getProperty("tiresbyvehicle");

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
					tiresByVehicles = new ArrayList<Footer>();

					for (Value val : values) {
						logger.info("value :" + val);
						productJson = new JSONObject(val.getString());
						logger.info("Product JSON :"
								+ productJson.getString("linktext"));
						footer = new Footer();
						logger.info("product :" + footer);

						footer.setLinkText(productJson.getString("linktext"));
						footer.setLinkUrl(productJson.getString("linkurl"));
						footer.setNewTab(productJson.getBoolean("newTab"));

						tiresByVehicles.add(footer);
					}
				}
				logger.info("tiresByType List size :" + tiresByVehicles.size());
			} else {
				logger.info("tiresByVehicles is empty");
			}

			if (currentNode.hasProperty("tiresbytype")) {
				prop = currentNode.getProperty("tiresbytype");

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
					tiresByType = new ArrayList<Footer>();

					for (Value val : values) {
						logger.info("value :" + val);
						productJson = new JSONObject(val.getString());
						logger.info("Product JSON :"
								+ productJson.getString("linktext"));
						footer = new Footer();
						logger.info("product :" + footer);

						footer.setLinkText(productJson.getString("linktext"));
						footer.setLinkUrl(productJson.getString("linkurl"));
						footer.setNewTab(productJson.getBoolean("newTab"));

						tiresByType.add(footer);
					}

				}
				logger.info("tiresByType List size :" + tiresByType.size());
			} else {
				logger.info("tiresByType is empty");
			}

			if (currentNode.hasProperty("support")) {
				prop = currentNode.getProperty("support");

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
					support = new ArrayList<Footer>();

					for (Value val : values) {
						logger.info("value :" + val);
						productJson = new JSONObject(val.getString());
						logger.info("Product JSON :"
								+ productJson.getString("linktext"));
						footer = new Footer();
						logger.info("product :" + footer);

						footer.setLinkText(productJson.getString("linktext"));
						footer.setLinkUrl(productJson.getString("linkurl"));
						footer.setNewTab(productJson.getBoolean("newTab"));

						support.add(footer);
					}
				}
				logger.info("support List size :" + support.size());
			} else {
				logger.info("support is empty");
			}

			if (currentNode.hasProperty("companyinfo")) {
				prop = currentNode.getProperty("companyinfo");

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
					companyInfo = new ArrayList<Footer>();

					for (Value val : values) {
						logger.info("value :" + val);
						productJson = new JSONObject(val.getString());
						logger.info("Product JSON :"
								+ productJson.getString("linktext"));
						footer = new Footer();
						logger.info("product :" + footer);

						footer.setLinkText(productJson.getString("linktext"));
						footer.setLinkUrl(productJson.getString("linkurl"));
						footer.setNewTab(productJson.getBoolean("newTab"));

						companyInfo.add(footer);
					}
				}
				logger.info("products List size :" + companyInfo.size());
			} else {
				logger.info("tiresByVehicles is empty");
			}


			logger.info(tiresByVehicles.toString());

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public String getLocale() {
		return locale;
	}
}
