package com.SD.motiv.chat.presenter;

import com.SD.motiv.chat.data.model.Chat;
import com.SD.motiv.chat.displayer.ChatDisplayer;
import com.SD.motiv.chat.service.ChatService;
import com.SD.motiv.user.data.model.User;
import com.SD.motiv.analytics.Analytics;
import com.SD.motiv.analytics.ErrorLogger;
import com.SD.motiv.channel.data.model.Channel;
import com.SD.motiv.chat.data.model.Message;
import com.SD.motiv.database.DatabaseResult;
import com.SD.motiv.login.data.model.Authentication;
import com.SD.motiv.login.service.LoginService;
import com.SD.motiv.navigation.Navigator;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

import static com.SD.motiv.chat.presenter.ChatPresenter.Pair.asPair;

public class ChatPresenter {

    private final LoginService loginService;
    private final ChatService chatService;
    private final ChatDisplayer chatDisplayer;
    private final Analytics analytics;
    private final Channel channel;
    private final Navigator navigator;
    private final ErrorLogger errorLogger;

    private CompositeDisposable subscriptions = new CompositeDisposable();
    private User user;

    public ChatPresenter(
            LoginService loginService,
            ChatService chatService,
            ChatDisplayer chatDisplayer,
            Channel channel,
            Analytics analytics,
            Navigator navigator,
            ErrorLogger errorLogger
    ) {
        this.loginService = loginService;
        this.chatService = chatService;
        this.chatDisplayer = chatDisplayer;
        this.analytics = analytics;
        this.channel = channel;
        this.navigator = navigator;
        this.errorLogger = errorLogger;
    }

    public void startPresenting() {
        chatDisplayer.setTitle(channel.getName());
        if (channel.isPrivate()) {
            chatDisplayer.showAddMembersButton();
        }
        chatDisplayer.attach(actionListener);
        chatDisplayer.disableInteraction();
        subscriptions.add(
                Observable.combineLatest(chatService.getChat(channel), loginService.getAuthentication(), asPair())
                        .subscribe(new Consumer<Pair>() {
                            @Override
                            public void accept(@NonNull Pair pair) throws Exception {
                                if (pair.auth.isSuccess()) {
                                    user = pair.auth.getUser();
                                    displayChat(pair);
                                } else {
                                    errorLogger.reportError(pair.auth.getFailure(), "Not logged in when opening chat");
                                    navigator.toLogin();
                                }
                            }
                        })
        );
    }

    private void displayChat(ChatPresenter.Pair pair) {
        if (pair.chatResult.isSuccess()) {
            chatDisplayer.display(pair.chatResult.getData(), user);
        } else {
            errorLogger.reportError(pair.chatResult.getFailure(), "Cannot open chat");
            navigator.toChannels();
        }
    }

    public void stopPresenting() {
        chatDisplayer.detach(actionListener);
        subscriptions.clear(); //TODO sort out checks
        subscriptions = new CompositeDisposable();
    }

    private boolean userIsAuthenticated() {
        return user != null;
    }

    private final ChatDisplayer.ChatActionListener actionListener = new ChatDisplayer.ChatActionListener() {
        @Override
        public void onUpPressed() {
            navigator.toParent();
        }

        @Override
        public void onMessageLengthChanged(int messageLength) {
            if (userIsAuthenticated() && messageLength > 0) {
                chatDisplayer.enableInteraction();
            } else {
                chatDisplayer.disableInteraction();
            }
        }

        @Override
        public void onSubmitMessage(String message) {
            chatService.sendMessage(channel, new Message(user, message));
            analytics.trackMessageLength(message.length(), user.getId(), channel.getName());
        }

        @Override
        public void onManageOwnersClicked() {
            analytics.trackManageOwners(user.getId(), channel.getName());
            navigator.toMembersOf(channel);
        }
    };

    static class Pair {

        public final DatabaseResult<Chat> chatResult;
        public final Authentication auth;

        private Pair(DatabaseResult<Chat> chatResult, Authentication auth) {
            this.chatResult = chatResult;
            this.auth = auth;
        }

        static BiFunction<DatabaseResult<Chat>, Authentication, Pair> asPair() {
            return new BiFunction<DatabaseResult<Chat>, Authentication, ChatPresenter.Pair>() {
                @Override
                public ChatPresenter.Pair apply(DatabaseResult<Chat> chatDatabaseResult, Authentication authentication) {
                    return new ChatPresenter.Pair(chatDatabaseResult, authentication);
                }
            };
        }

    }

}
