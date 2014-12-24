package com.diadementi.seeds.models;

import org.json.JSONObject;

import com.codecamp14.seeds.models.Category;

public class Campaign {
	/*{
		"id": "1",
		"title": "Space Rites: Interactive Installation & Performance Series",
		"brief": "Taylor Lee Shepherd's oscilloscopes will translate footsteps, voices and music into responsive visuals in the St. Maurice church.",
		"creator": "New Orleans airlift",
		"goal": "6000",
		"created": "2014-08-26 10:55:00",
		"goal_duration": "2014-09-04",
		"total_donations": "0",
		"total_donors": "0"
		},*/
	
	protected int id;
	protected String title;
	protected String desc;
	protected String creator;
	protected String timeCreated;
	protected String  goalDuration;
	protected int goal;
	protected int totalDonations;
	protected int totalDonors;
	protected String imageUrl;
	protected Category category;
		public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String image) {
		this.imageUrl = image;
	}
		public Campaign(){
			
		}
		/**
		 * 
		 * @param json
		 */
		public static Campaign getInstance(JSONObject json) {
				Campaign mInstance=new Campaign();
			try{
				mInstance.setId(json.getInt("id"));
				mInstance.title=json.getString("title");
				mInstance.desc=json.getString("brief");
				mInstance.creator=json.getString("creator");
				mInstance.timeCreated=json.getString("created");
				mInstance.goal=json.getInt("goal");
				mInstance.goalDuration=json.getString("goal_duration");
				mInstance.totalDonations=json.getInt("total_donations");
				mInstance.totalDonors=json.getInt("total_donors");
				mInstance.imageUrl=json.getString("photos");
				JSONObject cat=json.getJSONObject("category");
				mInstance.category=new Category(cat.getInt("id"),cat.getString("name"));
				return mInstance;
				}catch(Exception m){
					m.printStackTrace();
					
				}
			return null;
		}
		public int getId() {
			return id;
		}
		private void setId(int id) {
			this.id = id;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getDesc() {
			return desc;
		}
		public void setDesc(String desc) {
			this.desc = desc;
		}
		public String getCreator() {
			return creator;
		}
		public void setCreator(String creator) {
			this.creator = creator;
		}
		public String getTimeCreated() {
			return timeCreated;
		}
		public void setTimeCreated(String timeCreated) {
			this.timeCreated = timeCreated;
		}
		public String getGoalDuration() {
			return goalDuration;
		}
		public void setGoalDuration(String goalDuration) {
			this.goalDuration = goalDuration;
		}
		public int getGoal() {
			return goal;
		}
		public void setGoal(int goal) {
			this.goal = goal;
		}
		public int getTotalDonations() {
			return totalDonations;
		}
		public void setTotalDonations(int totalDonations) {
			this.totalDonations = totalDonations;
		}
		public int getTotalDonors() {
			return totalDonors;
		}
		public void setTotalDonors(int totalDonors) {
			this.totalDonors = totalDonors;
		}
		public Category getCategory() {
			return category;
		}
		public double getPercentDonation(){
			double percent=(totalDonations/goal)*100.0;
			return percent;
		}
		public int getDaysLeft(){
			return 2;
		}
		
	

}
