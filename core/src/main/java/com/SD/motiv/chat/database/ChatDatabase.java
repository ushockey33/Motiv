package com.SD.motiv.chat.database;

import com.SD.motiv.channel.data.model.Channel;
import com.SD.motiv.chat.data.model.Chat;
import com.SD.motiv.chat.data.model.Message;

import io.reactivex.Observable;

public interface ChatDatabase {

    Observable<Chat> observeChat(Channel channel);

    void sendMessage(Channel channel, Message message);

}
