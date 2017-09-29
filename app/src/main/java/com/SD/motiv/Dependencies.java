package com.SD.motiv;

import android.content.Context;

import com.SD.motiv.chat.database.FirebaseChatDatabase;
import com.SD.motiv.chat.service.ChatService;
import com.SD.motiv.user.service.UserService;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.SD.motiv.analytics.Analytics;
import com.SD.motiv.analytics.ErrorLogger;
import com.SD.motiv.analytics.FirebaseAnalyticsAnalytics;
import com.SD.motiv.analytics.FirebaseErrorLogger;
import com.SD.motiv.channel.database.FirebaseChannelsDatabase;
import com.SD.motiv.channel.service.ChannelService;
import com.SD.motiv.channel.service.PersistedChannelService;
import com.SD.motiv.chat.service.PersistedChatService;
import com.SD.motiv.login.database.FirebaseAuthDatabase;
import com.SD.motiv.login.service.FirebaseLoginService;
import com.SD.motiv.login.service.LoginService;
import com.SD.motiv.rx.FirebaseObservableListeners;
import com.SD.motiv.user.database.FirebaseUserDatabase;
import com.SD.motiv.user.service.PersistedUserService;

public enum Dependencies {
    INSTANCE;

    private Analytics analytics;
    private ErrorLogger errorLogger;

    private LoginService loginService;
    private ChatService chatService;
    private ChannelService channelService;
    private UserService userService;
    private Config config;

    public void init(Context context) {
        if (needsInitialisation()) {
            Context appContext = context.getApplicationContext();
            FirebaseApp firebaseApp = FirebaseApp.initializeApp(appContext, FirebaseOptions.fromResource(appContext), "Bonfire");
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(firebaseApp);
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(firebaseApp);
            firebaseDatabase.setPersistenceEnabled(true);
            FirebaseObservableListeners firebaseObservableListeners = new FirebaseObservableListeners();
            FirebaseUserDatabase userDatabase = new FirebaseUserDatabase(firebaseDatabase, firebaseObservableListeners);

            analytics = new FirebaseAnalyticsAnalytics(FirebaseAnalytics.getInstance(appContext));
            errorLogger = new FirebaseErrorLogger();
            loginService = new FirebaseLoginService(new FirebaseAuthDatabase(firebaseAuth), userDatabase);
            chatService = new PersistedChatService(new FirebaseChatDatabase(firebaseDatabase, firebaseObservableListeners));
            channelService = new PersistedChannelService(new FirebaseChannelsDatabase(firebaseDatabase, firebaseObservableListeners), userDatabase);
            userService = new PersistedUserService(userDatabase);
            config = FirebaseConfig.newInstance().init(errorLogger);
        }
    }

    private boolean needsInitialisation() {
        return loginService == null || chatService == null || channelService == null
                || userService == null || analytics == null || errorLogger == null;
    }

    public Analytics getAnalytics() {
        return analytics;
    }

    public LoginService getLoginService() {
        return loginService;
    }

    public ChatService getChatService() {
        return chatService;
    }

    public ChannelService getChannelService() {
        return channelService;
    }

    public UserService getUserService() {
        return userService;
    }

    public ErrorLogger getErrorLogger() {
        return errorLogger;
    }

    public Config getConfig() {
        return config;
    }
}
