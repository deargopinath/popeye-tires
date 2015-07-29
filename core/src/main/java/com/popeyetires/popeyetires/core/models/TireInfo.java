package com.popeyetires.popeyetires.core.models;

public class TireInfo {
	private String material;
	private String descriptionEn;
	private String descriptionFr;
	private String title;
	private String treadDepth;
	private String warrantyInKM;
	private String warrantyInMiles;
	private String fullSize;
	private boolean snowTire;
	private boolean winterTire;
	private String price;
	private String tireImage;
	
	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getDescriptionEn() {
		return descriptionEn;
	}

	public void setDescriptionEn(String descriptionEn) {
		this.descriptionEn = descriptionEn;
	}

	public String getDescriptionFr() {
		return descriptionFr;
	}

	public void setDescriptionFr(String descriptionFr) {
		this.descriptionFr = descriptionFr;
	}

	public String getWarrantyInKM() {
		return warrantyInKM;
	}

	public void setWarrantyInKM(String warrantyInKM) {
		this.warrantyInKM = warrantyInKM;
	}

	public String getWarrantyInMiles() {
		return warrantyInMiles;
	}

	public void setWarrantyInMiles(String warrantyInMiles) {
		this.warrantyInMiles = warrantyInMiles;
	}

	public String getFullSize() {
		return fullSize;
	}

	public void setFullSize(String fullSize) {
		this.fullSize = fullSize;
	}

	public boolean isSnowTire() {
		return snowTire;
	}

	public void setSnowTire(boolean snowTire) {
		this.snowTire = snowTire;
	}

	public boolean isWinterTire() {
		return winterTire;
	}

	public void setWinterTire(boolean winterTire) {
		this.winterTire = winterTire;
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTreadDepth() {
		return treadDepth;
	}
	
	public void setTreadDepth(String treadDepth) {
		this.treadDepth = treadDepth;
	}
	
	public String getPrice() {
		return price;
	}
	
	public void setPrice(String price) {
		this.price = price;
	}

	public String getTireImage() {
		return tireImage;
	}

	public void setTireImage(String tireImage) {
		this.tireImage = tireImage;
	}
	
	public String toString() {
		return "title=[" + title + "]description=[" + descriptionEn + "-" + descriptionFr + "]treadDepth=[" + treadDepth + "]warranty=[" + warrantyInKM + "-" + warrantyInMiles + "]price=[" + price + "]";
	}
}
