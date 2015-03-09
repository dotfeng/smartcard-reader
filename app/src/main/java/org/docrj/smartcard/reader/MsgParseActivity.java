package org.docrj.smartcard.reader;

import android.os.Build;
import android.support.v4.view.MenuItemCompat;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.support.v7.widget.ShareActionProvider;
import android.widget.TextView;


public class MsgParseActivity extends ActionBarActivity {

    String mMsgName;
    String mHtml;
    String mActivityName;
    int mTestMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle b = intent.getBundleExtra("parsed_msg");
        String msgText = b.getString("text");

        mMsgName = b.getString("name");
        mHtml = b.getString("html");
        mActivityName = b.getString("activity");
        mTestMode = b.getInt("test_mode");

        setContentView(R.layout.activity_msg_parse);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // toolbar reference does not work for setting title
        getSupportActionBar().setTitle(mActivityName);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView heading = (TextView) findViewById(R.id.heading);
        heading.setText(mMsgName);

        TextView contents = (TextView) findViewById(R.id.msg_text);
        contents.setText(msgText);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,
                    WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS |
                            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            w.setStatusBarColor(getResources().getColor(R.color.primary_dark));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_msg_parse, menu);
        MenuItem item = menu.findItem(R.id.menu_share_msgs);
        ShareActionProvider sp = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(mHtml));
        String subject = getString(R.string.app_name) + ": " + mActivityName +
                ": " + mMsgName;
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        sendIntent.setType("text/html");
        sp.setShareIntent(sendIntent);
        return true;
    }
}