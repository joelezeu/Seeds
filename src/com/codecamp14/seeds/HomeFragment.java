package com.codecamp14.seeds;

import java.util.ArrayList;
import java.util.List;

import com.codecamp14.seeds.models.*;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeFragment extends Fragment {
	ListView list;
	ImageView img;
	TextView tv;
	TextView cat;
	private static List<Home> home = new ArrayList<Home>();

	public HomeFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_home, container,
				false);
		
		
		list = (ListView) rootView.findViewById(R.id.HomelistView);
		populateHomList();
		populateListView();
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "You Click" +position, Toast.LENGTH_SHORT).show();
			}
			
		});
			
		
		
		return rootView;
	}

	private void populateHomList() {
		// TODO Auto-generated method stub
		home.add(new Home("Help Start a Tomato Business", "SME/SMALL BUSINESS",
				R.drawable.tomato));
		home.add(new Home("Help cure Ebola", "TECHNOLOGY",
				R.drawable.ebola));
		home.add(new Home("Help cure cancer", "HEALTH/MEDICAL",
				R.drawable.ayocanc));
		home.add(new Home("Disaster in Anambra", "ENVIRONMENT",
				R.drawable.floods));
		
	}

	private void populateListView() {
		// TODO Auto-generated method stub
		ArrayAdapter<Home> adapter = new MyListAdapter();
		list.setAdapter(adapter);
	}

	public class MyListAdapter extends ArrayAdapter<Home> {

		public MyListAdapter() {
			super(getActivity(), R.layout.list_item, home);
			// TODO Auto-generated constructor stub
			
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View itemView = convertView;
			if (itemView == null) {
				itemView = getActivity().getLayoutInflater().inflate(
						R.layout.list_item, parent, false);
			}
			
			img = (ImageView) itemView.findViewById(R.id.imageView1);
			tv = (TextView) itemView.findViewById(R.id.signUP);
			cat = (TextView) itemView.findViewById(R.id.textView2);
			Home homeAdd = home.get(position);
			img.setImageResource(homeAdd.getIcon());

			tv.setText(homeAdd.getTitle());
			cat.setText(homeAdd.getCategory());
			return itemView;
		}
	}
}
