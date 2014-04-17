package com.migliori.start.geektweek;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class MainActivity extends FragmentActivity {
    private static Twitter twitter;
    Button twitFeed;
    Button updateStatus;
    EditText status;
    Thread thread;
    public static boolean loginState = false;
    private static SharedPreferences mSharedPreferences;
    public  EditText statusText;
    Bitmap image;
    FragmentTransaction ft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ft = getFragmentManager().beginTransaction();



        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (savedInstanceState == null) {
            loginToTwitter();
            getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment())
                    .commit();        }

        mSharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
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
            Toast.makeText(getApplicationContext(), "Settings Pressed", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_home) {
            Toast.makeText(getApplicationContext(), "Home Pressed", Toast.LENGTH_SHORT).show();
            getFragmentManager().beginTransaction().replace(R.id.container, new TimeLineFragment())
                    .commit();
            return true;
        }
        if (id == R.id.action_search) {
            Toast.makeText(getApplicationContext(), "Search Pressed", Toast.LENGTH_SHORT).show();
           // setContentView(R.layout.fragment_search);
           //         ft.show(new SearchFragment());
           // ft.commit();
            getFragmentManager().beginTransaction().replace(R.id.container, new SearchFragment())
                    .commit();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onPrepareOptionsMenu(Menu menu)
    {

        return true;
    }

    private void loginToTwitter() {
        GetRequestTokenTask getRequestTokenTask = new GetRequestTokenTask();
        getRequestTokenTask.execute();
        loginState = true;
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
        intent.putExtra(Consts.AUTHENTICATION_URL_KEY, requestToken.getAuthenticationURL());
        startActivityForResult(intent, Consts.LOGIN_TO_TWITTER_REQUEST);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Consts.LOGIN_TO_TWITTER_REQUEST) {
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

    private void logoutFromTwitter() {
        // Clear the shared preferences
        Editor e = mSharedPreferences.edit();
        e.remove(Consts.PREF_KEY_OAUTH_TOKEN);
        e.remove(Consts.PREF_KEY_OAUTH_SECRET);
        e.remove(Consts.PREF_KEY_TWITTER_LOGIN);
        e.commit();

        // After this take the appropriate action
        // I am showing the hiding/showing buttons again
        // You might not needed this code

        //lblUpdate.setVisibility(View.GONE);
        // lblUserName.setText("");
        // lblUserName.setVisibility(View.GONE);

        //  btnLoginTwitter.setVisibility(View.VISIBLE);
    }


    public void checkLogin(){
        MenuItem loginS = (MenuItem) findViewById(R.id.action_login);
        MenuItem logoutS = (MenuItem) findViewById(R.id.action_logout);
        if(loginState)
        {
            loginS.setVisible(false);
        }
        else
        {
            logoutS.setVisible(false);
        }
    }
}