package com.codecamp14.seeds;

import java.util.ArrayList;
import java.util.List;

import com.codecamp14.seeds.models.Category;
import com.diadementi.seeds.helpers.UrlLink;
import com.diadementi.seeds.views.ListFragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
//import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BrowseProject extends Fragment {
	//changed to grid view
	ListView browseList;
//	GridView browseList;
	TextView browseText;
	TextView browseCat;
	
	private static List<Category> home = new ArrayList<Category>();
	static{
		home.add(new Category(1,"ARTS", "Creative Handworks"));
		home.add(new Category(2,"TECHNOLOGY", "ICT project or related"));
		home.add(new Category(3,"SMALL BUSINESS", "SMEs and Start Ups"));
		home.add(new Category(4,"HEALTH/MEDICALS", "Emergency/Disease"));
		home.add(new Category(5,"ENVIRONMENT", "Erosion, Floods"));
		home.add(new Category(6,"OTHERS", "From all works off life."));
	}
	
	public BrowseProject() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_browse, container,
				false);
		browseList = (ListView)rootView.findViewById(R.id.browseList);
//		browseList = (GridView)rootView.findViewById(R.id.browseList);
		BrowseListView();
		//valentine commented this out to stop duplication
//		BrosweHomeList();
		
		browseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Category category=(Category) parent.getItemAtPosition(position);
				Fragment fragment=new ListFragment(UrlLink.getCategory(category.getId()));
				Bundle b=new Bundle();
				b.putString("title", category.getName());
				fragment.setArguments(b);
				getFragmentManager().beginTransaction()
				.replace(R.id.frame_container, fragment).addToBackStack(null).commit();
				
//				for(int i=0;i<BrosweHomeList().length; i++){
//					
//				}
			}
		});
		
		return rootView;
	}
/*	valentine edit*/
	
	/*private void BrosweHomeList() {
		// TODO Auto-generated method stub
		home.add(new Browse("ARTS", "Creative Handworks"));
		home.add(new Browse("TECHNOLOGY", "ICT project or related"));
		home.add(new Browse("SMALL BUSINESS", "SMEs and Start Ups"));
		home.add(new Browse("HEALTH/MEDICALS", "Emergency/Disease"));
		home.add(new Browse("ENVIRONMENT", "Erosion, Floods"));
		home.add(new Browse("OTHERS", "From all works off life."));
	}*/

	private void BrowseListView() {
		// TODO Auto-generated method stub
		ArrayAdapter<Category> adapter = new MyBrowseAdapter();
		browseList.setAdapter(adapter);
	}

	public class MyBrowseAdapter extends ArrayAdapter<Category> {

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
			Category homeAdd = home.get(position);
			

			browseText.setText(homeAdd.getName());
			browseCat.setText(homeAdd.getCatdesc());
			return itemView;
}
	}
}