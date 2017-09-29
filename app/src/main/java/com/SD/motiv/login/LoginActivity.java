package com.SD.motiv.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.SD.motiv.navigation.AndroidNavigator;
import com.SD.motiv.BaseActivity;
import com.SD.motiv.Dependencies;
import com.SD.motiv.R;
import com.SD.motiv.login.displayer.LoginDisplayer;
import com.SD.motiv.login.presenter.LoginPresenter;
import com.SD.motiv.navigation.AndroidLoginNavigator;

public class LoginActivity extends BaseActivity {

    private static final int RC_SIGN_IN = 42;

    private LoginPresenter presenter;
    private AndroidLoginNavigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        LoginDisplayer loginDisplayer = (LoginDisplayer) findViewById(R.id.login_view);
        LoginGoogleApiClient loginGoogleApiClient = new LoginGoogleApiClient(this);
        loginGoogleApiClient.setupGoogleApiClient();
        navigator = new AndroidLoginNavigator(this, loginGoogleApiClient, new AndroidNavigator(this));
        presenter = new LoginPresenter(Dependencies.INSTANCE.getLoginService(),
                                       loginDisplayer,
                                       navigator,
                                       Dependencies.INSTANCE.getErrorLogger(),
                                       Dependencies.INSTANCE.getAnalytics());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!navigator.onActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.startPresenting();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.stopPresenting();
    }

}
