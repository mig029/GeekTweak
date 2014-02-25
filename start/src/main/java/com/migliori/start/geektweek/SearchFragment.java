package com.migliori.start.geektweek;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Anthony on 2/22/14.
 */

public class SearchFragment extends Fragment{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_search, container, false);

       // searchResult = (ListView) view.findViewById(R.id.twitFeed);
        //profilePic = (ImageView) view.findViewById(R.id.profilePic);

        //timeLineButton = (Button) view.findViewById(R.id.TimeLineButton);
       // timeLineButton.setOnClickListener(new View.OnClickListener() {
        /*
            @Override
            public void onClick(View view) {
                //loginToTwitter();
             //  Toast.makeText(getActivity().getApplicationContext(), "Timeline Refreshed", Toast.LENGTH_SHORT).show();

                Twitter twitter = TwitterFactory.getSingleton();
               List<Status> statuses = null;
                try {
                    query = new Query("Olympics");
                    result = twitter.search(query);
                   // statuses = twitter.getHomeTimeline();
                    statuses = result.getTweets();

                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                setTimeLine = new ArrayList<String>();
                System.out.println("Showing home timeline.");
                for (Status status : statuses) {
                    setTimeLine.add(status.getUser().getName() + ":" +
                            status.getText());
                   // profilePic.setImageBitmap(downloadBitmap(status.getUser().getBiggerProfileImageURL()));
                }
                // Create The Adapter with passing ArrayList as 3rd parameter
                ArrayAdapter<String> arrayAdapter;
                arrayAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), simple_list_item_1, setTimeLine);
                // Set The Adapter
                timeline.setAdapter(arrayAdapter);

                // Create new fragment and transaction
                Fragment newFragment = new TimeLineFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
            }
        });
*/
        return view;

    }

}
