package com.diadementi.seeds.helpers;

public  class UrlLink {

	public static final String baseUrl="http://192.168.56.1/seeds3";
	public static	final	String featured=baseUrl+"/v2/featured";
	public final static	String  campaigns=baseUrl+"/v2/campaigns";
	public static final	String add=baseUrl+"/v2/campaign";
	public static final String update =baseUrl+"/v2/campaign/" ;
	public static final String delete = baseUrl+"/v2/campaign/";
	private static String campaign=baseUrl+"/v2/campaign/";
	private static String category=baseUrl+"/v2/category/";
	public static String catergories=baseUrl+"/v2/categories";
	public static final String register=baseUrl+"/v2/register";
	public static final String login=baseUrl+"/v2/login";
	public static final String usersCampaign=baseUrl+"/v2/mycampaign";
	public static final String donate =baseUrl+"/v2/donation" ;
	public static final String withraw =baseUrl+"/v2/withdrawal" ;

	public static String getCategory(int id) {
		return category+id;
	}
	public static String getCampaign(int id) {
		return campaign+id;
	}
	public static String getCampaignView(int id){
		return baseUrl+"v2/mcampaign/"+id;
	}

	public static String update(int id){
		return update+id;
	}
	public static String delete(int id){
		return delete+id;
	}

}
