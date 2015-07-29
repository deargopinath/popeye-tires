package com.popeyetires.popeyetires.core.models;

public class Footer {

	private String linkUrl;
	private String linkText;
	private boolean newTab;

    public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getLinkText() {
		return linkText;
	}

	public void setLinkText(String linkText) {
		this.linkText = linkText;
	}

	public boolean isNewTab() {
		return newTab;
	}

	public void setNewTab(boolean newTab) {
		this.newTab = newTab;
	}

	
	@Override
	public String toString() {
		return "Footer [linkUrl=" + linkUrl + ", linkText=" + linkText
				+ ", newTab=" + newTab + "]";
	}

}