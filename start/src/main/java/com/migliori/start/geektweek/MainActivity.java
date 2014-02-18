package com.migliori.start.geektweek;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class MainActivity extends FragmentActivity{
    private static Twitter twitter;

    protected static final String AUTHENTICATION_URL_KEY = "AUTHENTICATION_URL_KEY";
    protected static final int LOGIN_TO_TWITTER_REQUEST= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */

    public class PlaceholderFragment extends Fragment  {

        public View rootView;
        public PlaceholderFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            Button login = (Button) rootView.findViewById(R.id.btnLoginToTwitter);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    loginToTwitter();
                }
            });

            return rootView;
        }
    }

    private void loginToTwitter() {
        GetRequestTokenTask getRequestTokenTask = new GetRequestTokenTask();
        getRequestTokenTask.execute();
    }

    private class GetRequestTokenTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            twitter = TwitterFactory.getSingleton();
            twitter.setOAuthConsumer(
                    getString(R.string.TWITTER_CONSUMER_KEY),
                    getString(R.string.TWITTER_CONSUMER_SECRET));

            try {
                RequestToken requestToken = twitter.getOAuthRequestToken(
                        getString(R.string.TWITTER_CALLBACK_URL));
                launchLoginWebView(requestToken);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    private void launchLoginWebView(RequestToken requestToken) {
        Intent intent = new Intent(this, LoginToTwitter.class);
        intent.putExtra(AUTHENTICATION_URL_KEY, requestToken.getAuthenticationURL());
        startActivityForResult(intent, LOGIN_TO_TWITTER_REQUEST);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_TO_TWITTER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                getAccessToken(data.getStringExtra(LoginToTwitter.CALLBACK_URL_KEY));
            }
        }
    }


    private void getAccessToken(String callbackUrl) {
        Uri uri = Uri.parse(callbackUrl);
        String verifier = uri.getQueryParameter("oauth_verifier");

        GetAccessTokenTask getAccessTokenTask = new GetAccessTokenTask();
        getAccessTokenTask.execute(verifier);
    }


    private class GetAccessTokenTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String verifier = strings[0];
            try {
                AccessToken accessToken = twitter.getOAuthAccessToken(verifier);
                Log.d(MainActivity.class.getSimpleName(), accessToken.getToken());
            } catch (Exception e) {

            }
            return null;
        }
    }

}

/*


public class MainActivity extends Activity {


    private static Twitter twitter;

    protected static final String AUTHENTICATION_URL_KEY = "AUTHENTICATION_URL_KEY";
    protected static final int LOGIN_TO_TWITTER_REQUEST= 0;
   // Button updateStatus;
    //TextView txtUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnLoginToTwitter)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loginToTwitter();
                    }
                });
        //updateStatus = (Button) findViewById(R.id.updateStatus);

        // updateStatus.setOnClickListener(new View.OnClickListener(){
        //    public void onClick(View v){
        //      updateStatus();
        // }

        //});
    }

    private void loginToTwitter() {
        GetRequestTokenTask getRequestTokenTask = new GetRequestTokenTask();
        getRequestTokenTask.execute();
    }
    /*
        private void updateStatus(){
            txtUpdate = (TextView) findViewById(R.id.status);
        }
            String status = txtUpdate.getText().toString();
    /*
            // Check for blank text
            if (status.trim().length() > 0) {
                // update status
                new updateTwitterStatus().execute(status);
            } else {
                // EditText is empty
                Toast.makeText(getApplicationContext(),
                        "Please enter status message", Toast.LENGTH_SHORT)
                        .show();

        }
    */
/*
    private class GetRequestTokenTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            twitter = TwitterFactory.getSingleton();
            twitter.setOAuthConsumer(
                    getString(R.string.TWITTER_CONSUMER_KEY),
                    getString(R.string.TWITTER_CONSUMER_SECRET));

            try {
                RequestToken requestToken = twitter.getOAuthRequestToken(
                        getString(R.string.TWITTER_CALLBACK_URL));
                launchLoginWebView(requestToken);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    private void launchLoginWebView(RequestToken requestToken) {
        Intent intent = new Intent(this, LoginToTwitter.class);
        intent.putExtra(AUTHENTICATION_URL_KEY, requestToken.getAuthenticationURL());
        startActivityForResult(intent, LOGIN_TO_TWITTER_REQUEST);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_TO_TWITTER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                getAccessToken(data.getStringExtra(LoginToTwitter.CALLBACK_URL_KEY));
            }
        }
    }


    private void getAccessToken(String callbackUrl) {
        Uri uri = Uri.parse(callbackUrl);
        String verifier = uri.getQueryParameter("oauth_verifier");

        GetAccessTokenTask getAccessTokenTask = new GetAccessTokenTask();
        getAccessTokenTask.execute(verifier);
    }


    private class GetAccessTokenTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String verifier = strings[0];
            try {
                AccessToken accessToken = twitter.getOAuthAccessToken(verifier);
                Log.d(MainActivity.class.getSimpleName(), accessToken.getToken());
            } catch (Exception e) {

            }
            return null;
        }
    }





}
*/