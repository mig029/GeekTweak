package com.migliori.start.geektweek;

import android.app.Fragment;
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
import android.widget.ImageView;
import android.widget.ListView;
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

/**
 * Created by Anthony on 2/23/14.
 */
public class TimeLineFragment extends Fragment{

    Query query;
    QueryResult result;
    Button timeLineButton;
    Button tweet;
    String updateStatus;
    public EditText statusText;

    ArrayList<String> setTimeLine;
    ListView timeline;
    ImageView profilePic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
       View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        initCards();

        timeLineButton = (Button) getActivity().findViewById(R.id.refreshTimeline);
        statusText = (EditText) getActivity().findViewById(R.id.statusText);
                timeLineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "Timeline Refreshed", Toast.LENGTH_SHORT).show();
                initCards();
            }
        });

        tweet = (Button) getActivity().findViewById(R.id.updateStatus);
        tweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(statusText.getWindowToken(), 0);

                Toast.makeText(getActivity().getApplicationContext(), "Status Updated", Toast.LENGTH_SHORT).show();

                updateStatus = statusText.getText().toString();
                Twitter twitter = TwitterFactory.getSingleton();
                try {
                    twitter.updateStatus(updateStatus);
                } catch (TwitterException e) {
                    e.printStackTrace();
                }

            }
        });


        statusText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {

                    handled = true;
                    imm.hideSoftInputFromWindow(statusText.getWindowToken(), 0);
                    Toast.makeText(getActivity().getApplicationContext(), "Status Updated", Toast.LENGTH_SHORT).show();

                    updateStatus = statusText.getText().toString();
                    Twitter twitter = TwitterFactory.getSingleton();
                    try {
                        twitter.updateStatus(updateStatus);
                    } catch (TwitterException e) {
                        e.printStackTrace();
                    }
                    statusText.setText("");


                }
                return handled;
            }
        });




    }


    private void initCards() {

        ArrayList<Card> cards = new ArrayList<Card>();
        Twitter twitter = TwitterFactory.getSingleton();
        List<Status> statuses = null;
        try {
            statuses = twitter.getHomeTimeline();
        } catch (TwitterException e) { e.printStackTrace(); }
        for (Status status : statuses) {
            //(status.getUser().getName() + ":" +    status.getText());
            //header.setTitle(status.getUser().getName());
            //url = status.getUser().getBiggerProfileImageURL();
            //profilePic.setImageBitmap(downloadBitmap(status.getUser().getBiggerProfileImageURL()));

            CardExample card = new CardExample(getActivity(),status.getUser().getName(),status.getText());
            CardThumbnail thumb = new CardThumbnail(getActivity());
            thumb.setUrlResource(status.getUser().getBiggerProfileImageURL());
            card.addCardThumbnail(thumb);
            card.setSwipeable(true);
            cards.add(card);
        }

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(),cards);

        CardListView listView = (CardListView) getActivity().findViewById(R.id.carddemo_list);
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
/*
        Bitmap image;

        public Bitmap downloadBitmap(String url) {
            // initilize the default HTTP client object
            final DefaultHttpClient client = new DefaultHttpClient();

            //forming a HttoGet request
            final HttpGet getRequest = new HttpGet(url);
            try {

                HttpResponse response = client.execute(getRequest);

                //check 200 OK for success
                final int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode != HttpStatus.SC_OK) {
                    Log.w("ImageDownloader", "Error " + statusCode +
                            " while retrieving bitmap from " + url);
                    return null;

                }

                final HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream inputStream = null;
                    try {
                        // getting contents from the stream
                        inputStream = entity.getContent();

                        // decoding stream data back into image Bitmap that android understands
                        image = BitmapFactory.decodeStream(inputStream);


                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        entity.consumeContent();
                    }
                }
            } catch (Exception e) {
                // You Could provide a more explicit error message for IOException
                getRequest.abort();
                Log.e("ImageDownloader", "Something went wrong while" +
                        " retrieving bitmap from " + url + e.toString());
            }

            return image;
        }
*/
/*
        timeline = (ListView) view.findViewById(R.id.twitFeed);
        profilePic = (ImageView) view.findViewById(R.id.profilePic);

        timeLineButton = (Button) view.findViewById(R.id.TimeLineButton);
        timeLineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //loginToTwitter();
                Toast.makeText(getActivity().getApplicationContext(), "Timeline Refreshed", Toast.LENGTH_SHORT).show();

                Twitter twitter = TwitterFactory.getSingleton();
                List<Status> statuses = null;
                try {
                   // query = new Query("Olympics");
                    //result = twitter.search(query);
                    statuses = twitter.getHomeTimeline();
                    //statuses = result.getTweets();

                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                setTimeLine = new ArrayList<String>();
                System.out.println("Showing home timeline.");
                for (Status status : statuses) {
                    setTimeLine.add(status.getUser().getName() + ":" +
                            status.getText());
                    profilePic.setImageBitmap(downloadBitmap(status.getUser().getBiggerProfileImageURL()));
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

        return view;

    }

    Bitmap image;
    public Bitmap downloadBitmap(String url) {
        // initilize the default HTTP client object
        final DefaultHttpClient client = new DefaultHttpClient();

        //forming a HttoGet request
        final HttpGet getRequest = new HttpGet(url);
        try {

            HttpResponse response = client.execute(getRequest);

            //check 200 OK for success
            final int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                Log.w("ImageDownloader", "Error " + statusCode +
                        " while retrieving bitmap from " + url);
                return null;

            }

            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    // getting contents from the stream
                    inputStream = entity.getContent();

                    // decoding stream data back into image Bitmap that android understands
                    image = BitmapFactory.decodeStream(inputStream);


                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            // You Could provide a more explicit error message for IOException
            getRequest.abort();
            Log.e("ImageDownloader", "Something went wrong while" +
                    " retrieving bitmap from " + url + e.toString());
        }

        return image;
    }

*/

}
