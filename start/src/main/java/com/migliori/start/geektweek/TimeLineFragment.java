package com.migliori.start.geektweek;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
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
public class TimeLineFragment extends Fragment{

    Query query;
    QueryResult result;
    Button timeLineButton;
    ArrayList<String> setTimeLine;
    ListView timeline;
    ImageView profilePic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
       View view = inflater.inflate(R.layout.fragment_timeline, container, false);

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



}
