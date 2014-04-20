package com.migliori.start.geektweek;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by macbook on 4/17/14.
 */
public class LoginActivity extends Fragment{
    private static final String TAG = "T4JSample";

    private Button buttonLogin;
    private Button getTweetButton;
    private TextView tweetText;
    private ScrollView scrollView;

    private static Twitter twitter;
    private static RequestToken requestToken;
    private static SharedPreferences mSharedPreferences;
    //private static TwitterStream twitterStream;
    private boolean running = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSharedPreferences = getActivity().getSharedPreferences(Consts.PREFERENCE_NAME, getActivity().MODE_PRIVATE);
        //scrollView = (ScrollView)findViewById(R.id.scrollView);
        //tweetText =(TextView)findViewById(R.id.tweetText);
        //getTweetButton = (Button)findViewById(R.id.getTweet);
        //getTweetButton.setOnClickListener(this);
        //buttonLogin = (Button) findViewById(R.id.twitterLogin);
        //buttonLogin.setOnClickListener(this);

        /**
         * Handle OAuth Callback
         */

        Uri uri = getActivity().getIntent().getData();
        if (uri != null && uri.toString().startsWith(getString(R.string.TWITTER_CALLBACK_URL))) {
            String verifier = uri.getQueryParameter(Consts.IEXTRA_OAUTH_VERIFIER);
            try {
                AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);

                SharedPreferences.Editor e = mSharedPreferences.edit();

                e.putString(Consts.PREF_KEY_TOKEN, accessToken.getToken());
                e.putString(Consts.PREF_KEY_SECRET, accessToken.getTokenSecret());
                e.commit();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                //Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onResume() {
        super.onResume();

        if (isConnected()) {
            String oauthAccessToken = mSharedPreferences.getString(Consts.PREF_KEY_TOKEN, "");
            String oAuthAccessTokenSecret = mSharedPreferences.getString(Consts.PREF_KEY_SECRET, "");

            ConfigurationBuilder confbuilder = new ConfigurationBuilder();
            Configuration conf = confbuilder
                    .setOAuthConsumerKey(getString(R.string.TWITTER_CONSUMER_KEY))
                    .setOAuthConsumerSecret(getString(R.string.TWITTER_CONSUMER_SECRET))
                    .setOAuthAccessToken(oauthAccessToken)
                    .setOAuthAccessTokenSecret(oAuthAccessTokenSecret)
                    .build();

            //buttonLogin.setText(R.string.label_disconnect);
            getTweetButton.setEnabled(true);
        } else {
           // buttonLogin.setText(R.string.label_connect);
        }
    }

    /**
     * check if the account is authorized
     * @return
     */
    private boolean isConnected() {
        return mSharedPreferences.getString(Consts.PREF_KEY_TOKEN, null) != null;
    }

    private void askOAuth() {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(getString(R.string.TWITTER_CONSUMER_KEY));
        configurationBuilder.setOAuthConsumerSecret(getString(R.string.TWITTER_CONSUMER_SECRET));
        Configuration configuration = configurationBuilder.build();
        twitter = new TwitterFactory(configuration).getInstance();

        try {
            requestToken = twitter.getOAuthRequestToken(getString(R.string.TWITTER_CALLBACK_URL));
            //Toast.makeText(this, "Please authorize this app!", Toast.LENGTH_LONG).show();
            this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL())));
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove Token, Secret from preferences
     */
    private void disconnectTwitter() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(Consts.PREF_KEY_TOKEN);
        editor.remove(Consts.PREF_KEY_SECRET);

        editor.commit();
    }

    /*
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.twitterLogin:
                if (isConnected()) {
                    disconnectTwitter();
                    //buttonLogin.setText(R.string.label_connect);
                } else {
                    askOAuth();
                }
                break;
            case R.id.getTweet:
                if (running) {
                    stopStreamingTimeline();
                    running = false;
                    getTweetButton.setText("start streaming");
                } else {
                    startStreamingTimeline();
                    running = true;
                    getTweetButton.setText("stop streaming");
                }
                break;
        }
    }
    */

    /*
    private void stopStreamingTimeline() {
        twitterStream.shutdown();
    }
    */
    /*

    public void startStreamingTimeline() {
        UserStreamListener listener = new UserStreamListener() {

            @Override
            public void onDeletionNotice(StatusDeletionNotice arg0) {
                System.out.println("deletionnotice");
            }

            @Override
            public void onScrubGeo(long arg0, long arg1) {
                System.out.println("scrubget");
            }

            @Override
            public void onStatus(Status status) {
                final String tweet = "@" + status.getUser().getScreenName() + " : " + status.getText() + "\n";
                System.out.println(tweet);
                tweetText.post(new Runnable() {
                    @Override
                    public void run() {
                        tweetText.append(tweet);
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }

            @Override
            public void onTrackLimitationNotice(int arg0) {
                System.out.println("trackLimitation");
            }

            @Override
            public void onException(Exception arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onBlock(User arg0, User arg1) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onDeletionNotice(long arg0, long arg1) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onDirectMessage(DirectMessage arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onFavorite(User arg0, User arg1, Status arg2) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onFollow(User arg0, User arg1) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onUnfollow(User user, User user2) {

            }

            @Override
            public void onFriendList(long[] arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onUnblock(User arg0, User arg1) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onUnfavorite(User arg0, User arg1, Status arg2) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onUserListCreation(User arg0, UserList arg1) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onUserListDeletion(User arg0, UserList arg1) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onUserListMemberAddition(User arg0, User arg1, UserList arg2) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onUserListMemberDeletion(User arg0, User arg1,  UserList arg2) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onUserListSubscription(User arg0, User arg1, UserList arg2) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onUserListUnsubscription(User arg0, User arg1, UserList arg2) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onUserListUpdate(User arg0, UserList arg1) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onUserProfileUpdate(User arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStallWarning(StallWarning arg0) {
                // TODO Auto-generated method stub

            }
        };
        twitterStream.addListener(listener);
        twitterStream.user();
        */
}
