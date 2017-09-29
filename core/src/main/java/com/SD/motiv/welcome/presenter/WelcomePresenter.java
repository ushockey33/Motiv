package com.SD.motiv.welcome.presenter;

import com.SD.motiv.analytics.Analytics;
import com.SD.motiv.navigation.Navigator;
import com.SD.motiv.user.data.model.User;
import com.SD.motiv.user.service.UserService;
import com.SD.motiv.welcome.displayer.WelcomeDisplayer;
import com.SD.motiv.welcome.displayer.WelcomeDisplayer.InteractionListener;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class WelcomePresenter {

    private final UserService userService;
    private final WelcomeDisplayer welcomeDisplayer;
    private final Navigator navigator;
    private final Analytics analytics;
    private final String senderId;

    private CompositeDisposable subscriptions = new CompositeDisposable();

    public WelcomePresenter(UserService userService, WelcomeDisplayer welcomeDisplayer, Navigator navigator, Analytics analytics, String senderId) {
        this.userService = userService;
        this.welcomeDisplayer = welcomeDisplayer;
        this.navigator = navigator;
        this.analytics = analytics;
        this.senderId = senderId;
    }

    public void startPresenting() {
        welcomeDisplayer.attach(interactionListener);
        analytics.trackInvitationOpened(senderId);
        subscriptions.add(
                userService.getUser(senderId).subscribe(new Consumer<User>() {
                    @Override
                    public void accept(@NonNull User user) throws Exception {
                        welcomeDisplayer.display(user);
                    }
                })
        );
    }

    public void stopPresenting() {
        welcomeDisplayer.detach(interactionListener);
        subscriptions.clear(); //TODO sort out checks
        subscriptions = new CompositeDisposable();
    }

    private final InteractionListener interactionListener = new InteractionListener() {
        @Override
        public void onGetStartedClicked() {
            analytics.trackInvitationAccepted(senderId);
            navigator.toLogin();
        }
    };
}
