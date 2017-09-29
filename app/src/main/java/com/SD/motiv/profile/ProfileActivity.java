package com.SD.motiv.profile;

import android.os.Bundle;

import com.SD.motiv.Dependencies;
import com.SD.motiv.BaseActivity;
import com.SD.motiv.R;
import com.SD.motiv.profile.displayer.ProfileDisplayer;
import com.SD.motiv.profile.presenter.ProfilePresenter;

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