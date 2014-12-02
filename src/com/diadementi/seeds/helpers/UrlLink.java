package com.diadementi.seeds.helpers;

public  class UrlLink {


	public static	final	String featured="http://192.168.56.1/seeds3/v2/featured";
	public final static	String  campaigns="http://192.168.56.1/seeds3/v2/campaigns";
	public static final	String add="http://192.168.56.1/seeds3/v2/ncampaign";
	public static final String update ="http://192.168.56.1/seeds3/v2/campaign/" ;
	public static final String delete = "http://192.168.56.1/seeds3/v2/campaign/";
	private static String campaign="http://192.168.56.1/seeds3/v2/campaign/";
	private static String category="http://192.168.56.1/seeds3/v2/category/";
	public static String catergories="http://192.168.56.1/seeds3/v2/categories";
	public static final String register="http://192.168.56.1/seeds3/v2/register";
	public static final String login="http://192.168.56.1/seeds3/v2/login";
	public static final String usersCampaign="http://192.168.56.1/seeds3/v2/mycampaign";
	public static final String donate ="http://192.168.56.1/seeds3/v2/donation" ;

	public static String getCategory(int id) {
		return category+id;
	}
	public static String getCampaign(int id) {
		return campaign+id;
	}
	public static String getCampaignView(int id){
		return "http://192.168.56.1/seeds3/v2/mcampaign/"+id;
	}

	public static String update(int id){
		return update+id;
	}
	public static String delete(int id){
		return delete+id;
	}

}
