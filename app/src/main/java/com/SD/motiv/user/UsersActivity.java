package com.SD.motiv.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.SD.motiv.Dependencies;
import com.SD.motiv.navigation.AndroidNavigator;
import com.SD.motiv.user.displayer.UsersDisplayer;
import com.SD.motiv.user.presenter.UsersPresenter;
import com.SD.motiv.BaseActivity;
import com.SD.motiv.R;
import com.SD.motiv.channel.data.model.Channel;

public class UsersActivity extends BaseActivity {

    private static final String NAME_EXTRA = "channel_name_extra";
    private static final String ACCESS_EXTRA = "channel_access_extra";
    private UsersPresenter presenter;

    public static Intent createIntentFor(Context context, Channel channel) {
        Intent intent = new Intent(context, UsersActivity.class);

        intent.putExtra(NAME_EXTRA, channel.getName());
        intent.putExtra(ACCESS_EXTRA, channel.getAccess().name());
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        UsersDisplayer usersDisplayer = (UsersDisplayer) findViewById(R.id.users_view);
        Channel channel = new Channel(
                getIntent().getStringExtra(NAME_EXTRA),
                Channel.Access.valueOf(getIntent().getStringExtra(ACCESS_EXTRA))
        );
        presenter = new UsersPresenter(
                Dependencies.INSTANCE.getUserService(),
                Dependencies.INSTANCE.getChannelService(),
                usersDisplayer,
                channel,
                new AndroidNavigator(this),
                Dependencies.INSTANCE.getErrorLogger(),
                Dependencies.INSTANCE.getAnalytics()
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.startPresenting();
    }

    @Override
    protected void onStop() {
        presenter.stopPresenting();
        super.onStop();
    }
}
