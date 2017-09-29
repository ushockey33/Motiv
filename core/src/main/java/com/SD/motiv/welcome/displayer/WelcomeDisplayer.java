package com.SD.motiv.welcome.displayer;

import com.SD.motiv.user.data.model.User;

public interface WelcomeDisplayer {

    void attach(InteractionListener interactionListener);

    void detach(InteractionListener interactionListener);

    void display(User sender);

    interface InteractionListener {
        void onGetStartedClicked();
    }
}
