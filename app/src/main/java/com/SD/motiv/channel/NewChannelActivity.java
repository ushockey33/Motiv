package com.SD.motiv.channel;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.SD.motiv.Dependencies;
import com.SD.motiv.channel.displayer.NewChannelDisplayer;
import com.SD.motiv.channel.presenter.NewChannelPresenter;
import com.SD.motiv.navigation.AndroidNavigator;
import com.SD.motiv.BaseActivity;
import com.SD.motiv.R;

public class NewChannelActivity extends BaseActivity {

    private NewChannelPresenter newChannelPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_channel);
        Dependencies dependencies = Dependencies.INSTANCE;
        newChannelPresenter = new NewChannelPresenter((NewChannelDisplayer) findViewById(R.id.create_channel_view),
                                                      dependencies.getChannelService(),
                                                      dependencies.getLoginService(),
                                                      new AndroidNavigator(this),
                                                      dependencies.getErrorLogger()
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        newChannelPresenter.startPresenting();
    }

    @Override
    protected void onStop() {
        newChannelPresenter.stopPresenting();
        super.onStop();
    }
}
