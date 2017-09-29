package com.SD.motiv.profile.presenter;

import com.SD.motiv.user.data.model.User;
import com.SD.motiv.login.data.model.Authentication;
import com.SD.motiv.login.service.LoginService;
import com.SD.motiv.profile.displayer.ProfileDisplayer;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by ushoc_000 on 7/23/2017.
 */

public class ProfilePresenter {

    private final LoginService loginService;
    private final ProfileDisplayer profileDisplayer;

    private User user;

    private Disposable subscription;

    public ProfilePresenter(
            LoginService loginService, ProfileDisplayer profileDisplayer
    ) {
        this.loginService = loginService;
        this.profileDisplayer = profileDisplayer;
    }

    public void startPresenting() {
        subscription = loginService.getAuthentication()
                .subscribe(new Consumer<Authentication>() {
                    @Override
                    public void accept(@NonNull Authentication authentication) throws Exception {
                        if (authentication.isSuccess()) {
                            user = authentication.getUser();
                            profileDisplayer.setTitle(user.getName());
                            profileDisplayer.setProfilePicture(user.getPhotoUrl());

                        } else {
                            //TODO show error for not having auth
                            //errorLogger.reportError(authentication.getFailure(), "Authentication failed");
                            //loginDisplayer.showAuthenticationError(authentication.getFailure().getLocalizedMessage()); //TODO improve error display
                        }
                    }
                });


    }


    private final ProfileDisplayer.ProfileActionListener profileActionListener = new ProfileDisplayer.ProfileActionListener() {
        @Override
        public void onOwnsClicked() {
            System.out.println("CLICKED onOwnsClicked");

        }
    };

    public void stopPresenting() {
        profileDisplayer.detach(profileActionListener);
        subscription.dispose(); //TODO handle checks
    }
}
