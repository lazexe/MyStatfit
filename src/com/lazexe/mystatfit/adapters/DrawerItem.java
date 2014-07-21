package com.lazexe.mystatfit.adapters;

public class DrawerItem {

	String itemName;
	int imageResourseID;
	String title;
	
	public DrawerItem(String itemName, int imageResourseID) {
		this.itemName = itemName;
		this.imageResourseID = imageResourseID;
	}
	
	public DrawerItem(String title) {
		this(null, 0);
		this.title = title;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getImageResourseID() {
		return imageResourseID;
	}

	public void setImageResourseID(int imageResourseID) {
		this.imageResourseID = imageResourseID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}
