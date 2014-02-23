package com.migliori.start.geektweek;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import static android.R.layout.simple_list_item_1;

/**
 * Created by Anthony on 2/23/14.
 */
public class PlaceholderFragment extends Fragment {

    public Twitter latestTweetChecker;
    public List statuses;
    public View rootView;
    public ListView timeline;
    public ArrayList<String> setTimeLine;
    public Button timeLineButton;
    public Query query;
    public QueryResult result;
    EditText statusText;
    Button updateStatus;
    Twitter twitter;

    public PlaceholderFragment() {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ///login = (Button) rootView.findViewById(R.id.btnLoginToTwitter);
        timeLineButton = (Button) rootView.findViewById(R.id.refreshTimeline);
        statusText = (EditText) rootView.findViewById(R.id.statusText);
        statusText.setOnEditorActionListener(new TextView.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if(arg1 == EditorInfo.IME_ACTION_SEARCH)
                {
                    // search pressed and perform your functionality.
                }
                return false;
            }

        });

            /*
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loginToTwitter();
                }
            });
            */
        updateStatus = (Button) rootView.findViewById(R.id.updateStatus);
        updateStatus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String status = statusText.getText().toString();
                if(status.trim().length() > 0){
                    try {

                        twitter.updateStatus(statusText.getText().toString());

                    } catch (TwitterException e) {
                        e.printStackTrace();
                    }
                    statusText.setText("");
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(statusText.getWindowToken(), 0);
                    Toast.makeText(getActivity().getApplicationContext(), "Status Updated", Toast.LENGTH_SHORT).show();
                }
            }
        });
        timeline = (ListView) rootView.findViewById(R.id.twitFeed);
        // logoutButton = (Button) rootView.findViewById(R.id.btnLogoutTwitter);
        statusText = (EditText) rootView.findViewById(R.id.statusText);
        timeLineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //loginToTwitter();
                Toast.makeText(getActivity().getApplicationContext(), "Timeline Refreshed", Toast.LENGTH_SHORT).show();

                Twitter twitter = TwitterFactory.getSingleton();
                List<Status> statuses = null;
                try {
                    query = new Query("Olympics");
                    result = twitter.search(query);
                    //statuses = twitter.getHomeTimeline();
                    statuses = result.getTweets();

                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                setTimeLine = new ArrayList<String>();
                System.out.println("Showing home timeline.");
                for (Status status : statuses) {
                    setTimeLine.add(status.getUser().getName() + ":" +
                            status.getText());
                }
                // Create The Adapter with passing ArrayList as 3rd parameter
                ArrayAdapter<String> arrayAdapter;
                arrayAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), simple_list_item_1, setTimeLine);
                // Set The Adapter
                timeline.setAdapter(arrayAdapter);

                // Create new fragment and transaction
                Fragment newFragment = new TimeLineFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack
                transaction.replace(R.id.container, newFragment);
                transaction.addToBackStack(null);

// Commit the transaction
                transaction.commit();


            }
        });
        return rootView;
    }
}

