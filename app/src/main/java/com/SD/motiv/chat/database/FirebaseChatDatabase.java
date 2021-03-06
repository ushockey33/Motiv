package com.SD.motiv.chat.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.SD.motiv.channel.data.model.Channel;
import com.SD.motiv.chat.data.model.Chat;
import com.SD.motiv.chat.data.model.Message;
import com.SD.motiv.rx.FirebaseObservableListeners;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public class FirebaseChatDatabase implements ChatDatabase {

    private static final int DEFAULT_LIMIT = 1000;

    private final DatabaseReference messagesDB;
    private final FirebaseObservableListeners firebaseObservableListeners;

    public FirebaseChatDatabase(FirebaseDatabase firebaseDatabase, FirebaseObservableListeners firebaseObservableListeners) {
        messagesDB = firebaseDatabase.getReference("messages");
        this.firebaseObservableListeners = firebaseObservableListeners;
    }

    @Override
    public Observable<Chat> observeChat(Channel channel) {
        return firebaseObservableListeners.listenToValueEvents(messagesInChannel(channel).limitToLast(DEFAULT_LIMIT), toChat());
    }

    @Override
    public void sendMessage(Channel channel, Message message) {
        messagesInChannel(channel).push().setValue(message); //TODO handle errors
    }

    private DatabaseReference messagesInChannel(Channel channel) {
        return messagesDB.child(channel.getName());
    }

    private Function<DataSnapshot, Chat> toChat() {
        return new Function<DataSnapshot, Chat>() {
            @Override
            public Chat apply(@NonNull DataSnapshot dataSnapshot) throws Exception {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                List<Message> messages = new ArrayList<>();
                for (DataSnapshot child : children) {
                    Message message = child.getValue(Message.class);
                    messages.add(message);
                }
                return new Chat(messages);
            }
        };
    }

}
