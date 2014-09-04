package com.codecamp14.seeds;

import java.util.ArrayList;
import java.util.List;

import com.codecamp14.seeds.models.Browse;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BrowseProject extends Fragment {
	
	ListView browseList;
	TextView browseText;
	TextView browseCat;
	
	private static List<Browse> home = new ArrayList<Browse>();
	
	static{
		home.add(new Browse("ARTS", "Creative Handworks"));
		home.add(new Browse("TECHNOLOGY", "ICT project or related"));
		home.add(new Browse("SMALL BUSINESS", "SMEs and Start Ups"));
		home.add(new Browse("HEALTH/MEDICALS", "Emergency/Disease"));
		home.add(new Browse("ENVIRONMENT", "Erosion, Floods"));
		home.add(new Browse("OTHERS", "From all works off life."));
	}
	
	public BrowseProject() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_browse, container,
				false);
		browseList = (ListView)rootView.findViewById(R.id.browseList);
		
		BrowseListView();
//		BrosweHomeList();
		
		browseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
//				for(int i=0;i<BrosweHomeList().length; i++){
//					
//				}
			}
		});
		
		return rootView;
	}
	
//	private void BrosweHomeList() {
//		// TODO Auto-generated method stub
//		home.add(new Browse("ARTS", "Creative Handworks"));
//		home.add(new Browse("TECHNOLOGY", "ICT project or related"));
//		home.add(new Browse("SMALL BUSINESS", "SMEs and Start Ups"));
//		home.add(new Browse("HEALTH/MEDICALS", "Emergency/Disease"));
//		home.add(new Browse("ENVIRONMENT", "Erosion, Floods"));
//		home.add(new Browse("OTHERS", "From all works off life."));
//	}

	private void BrowseListView() {
		// TODO Auto-generated method stub
		ArrayAdapter<Browse> adapter = new MyBrowseAdapter();
		browseList.setAdapter(adapter);
	}

	public class MyBrowseAdapter extends ArrayAdapter<Browse> {

		public MyBrowseAdapter() {
			super(getActivity(), R.layout.list_browse, home);
			// TODO Auto-generated constructor stub
			
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View itemView = convertView;
			if (itemView == null) {
				itemView = getActivity().getLayoutInflater().inflate(
						R.layout.list_browse, parent, false);
			}
			
			browseText = (TextView) itemView.findViewById(R.id.textViewArts);
			browseCat = (TextView) itemView.findViewById(R.id.textViewDesc);
			Browse homeAdd = home.get(position);
			

			browseText.setText(homeAdd.getCategory());
			browseCat.setText(homeAdd.getCatdesc());
			return itemView;
}
	}
}