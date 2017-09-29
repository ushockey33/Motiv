package com.SD.motiv.channel.service;

import com.SD.motiv.channel.data.model.Channels;
import com.SD.motiv.database.DatabaseResult;
import com.SD.motiv.user.data.model.User;
import com.SD.motiv.user.data.model.Users;
import com.SD.motiv.channel.data.model.Channel;

import io.reactivex.Observable;

public interface ChannelService {

    Observable<Channels> getChannelsFor(User user);

    Observable<DatabaseResult<Channel>> createPublicChannel(Channel newChannel);

    Observable<DatabaseResult<Channel>> createPrivateChannel(Channel newChannel, User owner);

    Observable<DatabaseResult<User>> addOwnerToPrivateChannel(Channel channel, User newOwner);

    Observable<DatabaseResult<User>> removeOwnerFromPrivateChannel(Channel channel, User removedOwner);

    Observable<DatabaseResult<Users>> getOwnersOfChannel(Channel channel);
}
