package com.SD.motiv.welcome;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.SD.motiv.Dependencies;
import com.SD.motiv.link.FirebaseDynamicLinkFactory;
import com.SD.motiv.navigation.AndroidNavigator;
import com.SD.motiv.welcome.displayer.WelcomeDisplayer;
import com.SD.motiv.welcome.presenter.WelcomePresenter;
import com.SD.motiv.BaseActivity;
import com.SD.motiv.R;

public class WelcomeActivity extends BaseActivity {

    private WelcomePresenter welcomePresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        String sender = getIntent().getData().getQueryParameter(FirebaseDynamicLinkFactory.SENDER);
        welcomePresenter = new WelcomePresenter(
                Dependencies.INSTANCE.getUserService(),
                (WelcomeDisplayer) findViewById(R.id.welcome_view),
                new AndroidNavigator(this),
                Dependencies.INSTANCE.getAnalytics(),
                sender
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        welcomePresenter.startPresenting();
    }

    @Override
    protected void onStop() {
        super.onStop();
        welcomePresenter.stopPresenting();
    }
}
