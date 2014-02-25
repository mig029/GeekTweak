package com.migliori.start.geektweek;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;

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
    Button searchButton;
    Twitter twitter;

    public PlaceholderFragment() {


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ///login = (Button) rootView.findViewById(R.id.btnLoginToTwitter);
        
        timeLineButton = (Button) rootView.findViewById(R.id.TimeLineButton);
        //statusText = (EditText) rootView.findViewById(android.R.id.statusText);
        /*
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
        searchButton = (Button) rootView.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Fragment testFragment = new SearchFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, testFragment);
                transaction.addToBackStack(null);
                transaction.commit();
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

