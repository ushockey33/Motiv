package com.SD.motiv.channel.database;

import com.SD.motiv.channel.data.model.Channel;
import com.SD.motiv.user.data.model.User;

import java.util.List;

import io.reactivex.Observable;

public interface ChannelsDatabase {

    Observable<List<String>> observePublicChannelIds();

    Observable<List<String>> observePrivateChannelIdsFor(User user);

    Observable<Channel> readChannelFor(String channelName);

    Observable<Channel> writeChannel(Channel newChannel);

    Observable<Channel> writeChannelToPublicChannelIndex(Channel newChannel);

    Observable<Channel> addOwnerToPrivateChannel(User user, Channel channel);

    Observable<Channel> removeOwnerFromPrivateChannel(User user, Channel channel);

    Observable<Channel> addChannelToUserPrivateChannelIndex(User user, Channel channel);

    Observable<Channel> removeChannelFromUserPrivateChannelIndex(User user, Channel channel);

    Observable<List<String>> observeOwnerIdsFor(Channel channel);

}
