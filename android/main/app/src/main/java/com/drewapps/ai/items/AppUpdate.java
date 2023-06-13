package com.drewapps.ai.items;

import com.google.gson.annotations.SerializedName;

public class AppUpdate{

	@SerializedName("updatePopupShow")
	private String updatePopupShow;

	@SerializedName("appLink")
	private String appLink;

	@SerializedName("cancelOption")
	private String cancelOption;

	@SerializedName("description")
	private String description;

	@SerializedName("newAppVersionCode")
	private String newAppVersionCode;

	@Override
 	public String toString(){
		return 
			"AppUpdate{" + 
			"updatePopupShow = '" + updatePopupShow + '\'' + 
			",appLink = '" + appLink + '\'' + 
			",cancelOption = '" + cancelOption + '\'' + 
			",description = '" + description + '\'' + 
			",newAppVersionCode = '" + newAppVersionCode + '\'' + 
			"}";
		}
}