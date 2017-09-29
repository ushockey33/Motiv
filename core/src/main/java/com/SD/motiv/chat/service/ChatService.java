package com.SD.motiv.chat.service;

import com.SD.motiv.chat.data.model.Chat;
import com.SD.motiv.channel.data.model.Channel;
import com.SD.motiv.chat.data.model.Message;
import com.SD.motiv.database.DatabaseResult;

import io.reactivex.Observable;

public interface ChatService {

    Observable<DatabaseResult<Chat>> getChat(Channel channel);

    void sendMessage(Channel channel, Message message);

}
