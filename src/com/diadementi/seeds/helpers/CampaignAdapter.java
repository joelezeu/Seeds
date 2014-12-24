package com.diadementi.seeds.helpers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codecamp14.seeds.R;
import com.diadementi.seeds.models.Campaign;
import com.squareup.picasso.Picasso;

public class CampaignAdapter extends ArrayAdapter<Campaign> {
	private Context ctx;
	private int res;
	private ArrayList<Campaign> data;

	public CampaignAdapter(Context context, int resource, ArrayList<Campaign> objects){
		super(context,resource,objects);
		this.ctx=context;
		this.res=resource;
		this.data=objects;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Add other attributes of Campaign to view
		if (convertView==null){
			LayoutInflater vi =(LayoutInflater)this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView=vi.inflate(this.res, null);

		}
		ImageView image=(ImageView)convertView.findViewById(R.id.thumbnail);
		TextView title=(TextView)convertView.findViewById(R.id.title);
		//		TextView desc=(TextView)v.findViewById(R.id.desc);
		//		TextView creator=(TextView)v.findViewById(R.id.creator);
		//		TextView totalDonation=(TextView)v.findViewById(R.id.totalDonation);
		//		TextView goalDuration=(TextView)v.findViewById(R.id.goalDuration);

		Campaign s=getItem(position);
		//		image.setImageResource(s.getImage());
		title.setText(s.getTitle());
		//		desc.setText(s.getDesc());
		//		creator.setText(s.getCreator());
		//		totalDonation.setText(s.getTotalDonations());
		//		goalDuration.setText(s.getGoalDuration());
		TextView category=(TextView)convertView.findViewById(R.id.category);
		category.setText(s.getCategory().getName());
		Picasso.with(ctx)
		.load(s.getImageUrl())
		.resize(90, 90)
		.centerCrop()
		.placeholder(R.drawable.progress)
		.into(image);
		Log.i("image url",s.getImageUrl());
		TextView date=(TextView)convertView.findViewById(R.id.dateCreated);
		String cdate=s.getTimeCreated();
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date mdateObject=format.parse(cdate);
			long time=mdateObject.getTime();
			long now=new Date().getTime();
			date.setText(DateUtils.getRelativeTimeSpanString(time, now, DateUtils.DAY_IN_MILLIS).toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}
	public Campaign getItem(int i){
		return this.data.get(i);
	}
	public void refill(ArrayList<Campaign> campaigns){
		data.clear();
		data.addAll(campaigns);
		notifyDataSetChanged();
	}
}