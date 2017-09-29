package com.SD.motiv.chat.displayer;

import com.SD.motiv.chat.data.model.Chat;
import com.SD.motiv.user.data.model.User;

public interface ChatDisplayer {

    void attach(ChatActionListener actionListener);

    void detach(ChatActionListener actionListener);

    void setTitle(String title);

    void showAddMembersButton();

    void display(Chat chat, User user);

    void enableInteraction();

    void disableInteraction();

    interface ChatActionListener {

        void onUpPressed();

        void onMessageLengthChanged(int messageLength);

        void onSubmitMessage(String message);

        void onManageOwnersClicked();

    }

}
