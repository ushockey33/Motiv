package com.SD.motiv.login.presenter;

import com.SD.motiv.analytics.Analytics;
import com.SD.motiv.analytics.ErrorLogger;
import com.SD.motiv.login.data.model.Authentication;
import com.SD.motiv.login.displayer.LoginDisplayer;
import com.SD.motiv.login.service.LoginService;
import com.SD.motiv.navigation.LoginNavigator;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class LoginPresenter {

    private final LoginService loginService;
    private final LoginDisplayer loginDisplayer;
    private final LoginNavigator navigator;
    private final ErrorLogger errorLogger;
    private final Analytics analytics;

    private Disposable subscription;

    public LoginPresenter(LoginService loginService,
                          LoginDisplayer loginDisplayer,
                          LoginNavigator navigator,
                          ErrorLogger errorLogger,
                          Analytics analytics) {
        this.loginService = loginService;
        this.loginDisplayer = loginDisplayer;
        this.navigator = navigator;
        this.errorLogger = errorLogger;
        this.analytics = analytics;
    }

    public void startPresenting() {
        navigator.attach(loginResultListener);
        loginDisplayer.attach(actionListener);
        subscription = loginService.getAuthentication()
                .subscribe(new Consumer<Authentication>() {
                    @Override
                    public void accept(@NonNull Authentication authentication) throws Exception {
                        if (authentication.isSuccess()) {
                            navigator.toChannels();
                        } else {
                            errorLogger.reportError(authentication.getFailure(), "Authentication failed");
                            loginDisplayer.showAuthenticationError(authentication.getFailure().getLocalizedMessage()); //TODO improve error display
                        }
                    }
                });
    }

    public void stopPresenting() {
        navigator.detach(loginResultListener);
        loginDisplayer.detach(actionListener);
        subscription.dispose(); //TODO handle checks
    }

    private final LoginDisplayer.LoginActionListener actionListener = new LoginDisplayer.LoginActionListener() {

        @Override
        public void onGooglePlusLoginSelected() {
            analytics.trackSignInStarted("google");
            navigator.toGooglePlusLogin();
        }

    };

    private final LoginNavigator.LoginResultListener loginResultListener = new LoginNavigator.LoginResultListener() {
        @Override
        public void onGooglePlusLoginSuccess(String tokenId) {
            analytics.trackSignInSuccessful("google");
            loginService.loginWithGoogle(tokenId);
        }

        @Override
        public void onGooglePlusLoginFailed(String statusMessage) {
            loginDisplayer.showAuthenticationError(statusMessage);
        }
    };


}
