package com.chooblarin.gtwooblarin.ui
import android.app.Activity
import android.os.Bundle
import com.chooblarin.gtwooblarin.helper.TwitterHelper
import com.chooblarin.gtwooblarin.ui.fragment.TweetListFragment
import groovy.transform.CompileStatic

@CompileStatic
class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!TwitterHelper.hasStoredToken(this.applicationContext)) {
            startActivity(TwitterOAuthActivity.generateIntent(this))
            finish()
        } else {
            TweetListFragment fragment = new TweetListFragment()
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, fragment).commit()
        }
    }
}
