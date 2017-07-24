package com.novoda.bonfire.profile;

import android.os.Bundle;

import com.novoda.bonfire.BaseActivity;
import com.novoda.bonfire.Dependencies;
import com.novoda.bonfire.R;
import com.novoda.bonfire.profile.displayer.ProfileDisplayer;
import com.novoda.bonfire.profile.presenter.ProfilePresenter;

/**
 * Created by ushoc_000 on 7/23/2017.
 */

public class ProfileActivity extends BaseActivity {

    private ProfilePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ProfileDisplayer profileDisplayer = (ProfileDisplayer) findViewById(R.id.profile_view);

        presenter = new ProfilePresenter(
                Dependencies.INSTANCE.getLoginService(),
                profileDisplayer
        );
    }
    protected void onStart() {
        super.onStart();
        presenter.startPresenting();
    }

    protected void onStop() {
        super.onStop();
        presenter.stopPresenting();
    }

}