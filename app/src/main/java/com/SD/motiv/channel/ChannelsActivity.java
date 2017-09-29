package com.SD.motiv.channel;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.SD.motiv.BuildConfig;
import com.SD.motiv.Dependencies;
import com.SD.motiv.channel.displayer.ChannelsDisplayer;
import com.SD.motiv.channel.presenter.ChannelsPresenter;
import com.SD.motiv.link.FirebaseDynamicLinkFactory;
import com.SD.motiv.navigation.AndroidNavigator;
import com.SD.motiv.BaseActivity;
import com.SD.motiv.R;

public class ChannelsActivity extends BaseActivity {

    private ChannelsPresenter channelsPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channels);
        Dependencies dependencies = Dependencies.INSTANCE;
//TODO change deep link
        FirebaseDynamicLinkFactory firebaseDynamicLinkFactory = new FirebaseDynamicLinkFactory(
                getResources().getString(R.string.dynamicLinkDomain),
                getResources().getString(R.string.deepLinkBaseUrl),
                getResources().getString(R.string.iosBundleIdentifier),
                BuildConfig.APPLICATION_ID
        );
        channelsPresenter = new ChannelsPresenter(
                (ChannelsDisplayer) findViewById(R.id.channels_view),
                dependencies.getChannelService(),
                dependencies.getLoginService(),
                dependencies.getConfig(),
                new AndroidNavigator(this),
                firebaseDynamicLinkFactory,
                dependencies.getAnalytics()
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        channelsPresenter.startPresenting();
    }

    @Override
    protected void onStop() {
        channelsPresenter.stopPresenting();
        super.onStop();
    }
}
