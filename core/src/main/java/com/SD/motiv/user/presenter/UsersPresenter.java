package com.SD.motiv.user.presenter;

import com.SD.motiv.user.service.UserService;
import com.SD.motiv.analytics.Analytics;
import com.SD.motiv.analytics.ErrorLogger;
import com.SD.motiv.channel.data.model.Channel;
import com.SD.motiv.channel.service.ChannelService;
import com.SD.motiv.database.DatabaseResult;
import com.SD.motiv.navigation.Navigator;
import com.SD.motiv.user.data.model.User;
import com.SD.motiv.user.data.model.Users;
import com.SD.motiv.user.displayer.UsersDisplayer;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class UsersPresenter {
    private final UserService userService;
    private final ChannelService channelService;
    private final UsersDisplayer usersDisplayer;
    private final Channel channel;
    private final Navigator navigator;
    private final ErrorLogger errorLogger;
    private final Analytics analytics;
    private CompositeDisposable subscriptions = new CompositeDisposable();

    public UsersPresenter(UserService userService,
                          ChannelService channelService,
                          UsersDisplayer usersDisplayer,
                          Channel channel,
                          Navigator navigator,
                          ErrorLogger errorLogger,
                          Analytics analytics) {
        this.userService = userService;
        this.channelService = channelService;
        this.usersDisplayer = usersDisplayer;
        this.channel = channel;
        this.navigator = navigator;
        this.errorLogger = errorLogger;
        this.analytics = analytics;
    }

    public void startPresenting() {
        usersDisplayer.attach(selectionListener);

        subscriptions.add(
                userService.getAllUsers().subscribe(new Consumer<Users>() {
                    @Override
                    public void accept(@NonNull Users users) throws Exception {
                        usersDisplayer.display(users);
                    }
                })
        );
        subscriptions.add(
                channelService.getOwnersOfChannel(channel)
                        .subscribe(new Consumer<DatabaseResult<Users>>() {
                            @Override
                            public void accept(@NonNull DatabaseResult<Users> databaseResult) throws Exception {
                                if (databaseResult.isSuccess()) {
                                    usersDisplayer.displaySelectedUsers(databaseResult.getData());
                                } else {
                                    errorLogger.reportError(databaseResult.getFailure(), "Cannot fetch channel owners");
                                    usersDisplayer.showFailure();
                                }
                            }
                        })
        );
    }

    public void stopPresenting() {
        usersDisplayer.detach(selectionListener);
        subscriptions.clear();
        subscriptions = new CompositeDisposable();
    }

    private UsersDisplayer.SelectionListener selectionListener = new UsersDisplayer.SelectionListener() {
        @Override
        public void onUserSelected(final User user) {
            analytics.trackAddChannelOwner(channel.getName(), user.getId());
            channelService.addOwnerToPrivateChannel(channel, user)
                    .subscribe(updateOnActionResult());
        }

        @Override
        public void onUserDeselected(User user) {
            analytics.trackRemoveChannelOwner(channel.getName(), user.getId());
            channelService.removeOwnerFromPrivateChannel(channel, user)
                    .subscribe(updateOnActionResult());
        }

        @Override
        public void onCompleteClicked() {
            navigator.toParent();
        }
    };

    private Consumer<DatabaseResult<User>> updateOnActionResult() {
        return new Consumer<DatabaseResult<User>>() {
            @Override
            public void accept(@NonNull DatabaseResult<User> userDatabaseResult) throws Exception {
                if (!userDatabaseResult.isSuccess()) {
                    errorLogger.reportError(userDatabaseResult.getFailure(), "Cannot update channel owners");
                    usersDisplayer.showFailure();
                }
            }
        };
    }

}
