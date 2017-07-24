package com.novoda.bonfire.profile.presenter;

import com.novoda.bonfire.login.data.model.Authentication;
import com.novoda.bonfire.login.service.LoginService;
import com.novoda.bonfire.navigation.Navigator;
import com.novoda.bonfire.profile.displayer.ProfileDisplayer;
import com.novoda.bonfire.user.data.model.User;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

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
