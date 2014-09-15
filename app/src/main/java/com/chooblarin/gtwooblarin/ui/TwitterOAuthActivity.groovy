package com.chooblarin.gtwooblarin.ui
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.chooblarin.gtwooblarin.R
import com.chooblarin.gtwooblarin.helper.TwitterHelper
import com.chooblarin.gtwooblarin.task.Fluent
import groovy.transform.CompileStatic
import twitter4j.Twitter
import twitter4j.auth.AccessToken
import twitter4j.auth.RequestToken

@CompileStatic
class TwitterOAuthActivity extends Activity {

    public static Intent generateIntent(Context context) {
        return new Intent(context, TwitterOAuthActivity.class)
    }

    public static final String CALLBACK = "twooblarin://callback"

    Twitter twitter;

    RequestToken requestToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_oauth)

        twitter = TwitterHelper.getTwitter(this.applicationContext)
        twitter.setOAuthAccessToken(null) // initialize token

        findViewById(R.id.button_authorize).setOnClickListener {
            Fluent.async {
                this.requestToken = this.twitter.getOAuthRequestToken(this.CALLBACK)
                return this.requestToken.getAuthorizationURL()

            } then {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(it as String))
                this.startActivity(intent)
            }
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        Uri uri = intent?.getData()
        if (uri == null || !uri.toString().startsWith(CALLBACK)) {
            return
        }

        String verifier = uri.getQueryParameter("oauth_verifier")

        Fluent.async {
            RequestToken reqToken = getRequestToken()
            AccessToken token = this.twitter.getOAuthAccessToken(reqToken, verifier)
            TwitterHelper.storeAccessToken(this.applicationContext, token)
            return token

        } then {
            Intent i = new Intent(this, MainActivity.class)
            this.startActivity(i)
            this.finish()

        }
    }
}