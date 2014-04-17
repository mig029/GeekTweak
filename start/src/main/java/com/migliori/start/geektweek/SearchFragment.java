package com.migliori.start.geektweek;

import android.app.Fragment;
<<<<<<< HEAD
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
=======
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
>>>>>>> 96d2d7565c8eb8b39e06986d43d2719d7315e48b

/**
 * Created by Anthony on 2/22/14.
 */

public class SearchFragment extends Fragment{

<<<<<<< HEAD
    Query query;
    Button searchButton;
    QueryResult result;
    EditText search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<Card> cards = new ArrayList<Card>();
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        search = (EditText) getActivity().findViewById(R.id.searchText);
        search.requestFocus();
        searchButton = (Button) getActivity().findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(search.getWindowToken(), 0);

                initSearch();

                }
        });

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    handled = true;
                    initSearch();
                }
                return handled;
            }
        });
    }


    private void initSearch() {

        ArrayList<Card> cards = new ArrayList<Card>();
        Twitter twitter = TwitterFactory.getSingleton();
        List<Status> statuses = null;
        try {
                if(search.getText().toString() != null) {
                    query = new Query(search.getText().toString());
                    result = twitter.search(query);
                    // statuses = twitter.getHomeTimeline();
                    statuses = result.getTweets();
                    Toast.makeText(getActivity().getApplicationContext(), "Search Started", Toast.LENGTH_SHORT).show();

                }
                   else
                    Toast.makeText(getActivity().getApplicationContext(), "No Input Detected", Toast.LENGTH_SHORT).show();


        } catch (TwitterException e) { e.printStackTrace(); }
        if(statuses != null) {
            for (Status status : statuses) {
                CardExample card = new CardExample(getActivity(), status.getUser().getName(), status.getText());
                CardThumbnail thumb = new CardThumbnail(getActivity());
                thumb.setUrlResource(status.getUser().getBiggerProfileImageURL());
                card.addCardThumbnail(thumb);
                card.setSwipeable(true);
                cards.add(card);
            }
        }
        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(),cards);

        CardListView listView = (CardListView) getActivity().findViewById(R.id.card_search);
        if (listView!=null){
            listView.setAdapter(mCardArrayAdapter);
        }
    }


    public class CardExample extends Card {

        protected String mTitleHeader;
        protected String mTitleMain;

        public CardExample(Context context, String titleHeader, String titleMain) {
            super(context, R.layout.carddemo_example_inner_content);
            this.mTitleHeader = titleHeader;
            this.mTitleMain = titleMain;
            init();
        }

        private void init() {

            //Create a CardHeader
            CardHeader header = new CardHeader(getActivity());

            //Set the header title
            header.setTitle(mTitleHeader);
            addCardHeader(header);

            //Add ClickListener
            setOnClickListener(new OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    Toast.makeText(getContext(), "Click Listener card=" + mTitleHeader, Toast.LENGTH_SHORT).show();
                }
            });

            //Set the card inner text
            setTitle(mTitleMain);
        }
    }
=======

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

>>>>>>> 96d2d7565c8eb8b39e06986d43d2719d7315e48b
}
