package com.SD.motiv.chat.database;

import com.SD.motiv.chat.data.model.Chat;
import com.SD.motiv.user.data.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.SD.motiv.channel.data.model.Channel;
import com.SD.motiv.chat.data.model.Message;
import com.SD.motiv.rx.FirebaseObservableListeners;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Observable;

import static org.mockito.Mockito.verify;

public class FirebaseChatDatabaseTest {

    private final Channel channel = new Channel("a channel", Channel.Access.PUBLIC);
    private final User user = new User("user id", "user", "http://photo");
    private final User anotherUser = new User("another user id", "another user", "http://photo");
    private final Chat chatForChannel = new Chat(Arrays.asList(new Message(user, "hey!"), new Message(anotherUser, "hey yourself")));

    @Mock
    FirebaseObservableListeners mockListeners;
    @Mock
    FirebaseDatabase mockFirebaseDatabase;
    @Mock
    DatabaseReference mockMessagesDatabase;

    FirebaseChatDatabase firebaseChatDatabase;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        setupDatabaseStubsFor("messages", mockMessagesDatabase, mockFirebaseDatabase);
        setupValueEventListenerFor(mockListeners, mockMessagesDatabase, chatForChannel);

        firebaseChatDatabase = new FirebaseChatDatabase(mockFirebaseDatabase, mockListeners);
    }

    @Test
    public void subscriberReceivesChatWhenObserving() {
        Observable<Chat> chatObservable = firebaseChatDatabase.observeChat(channel);
        assertValueReceivedOnNext(chatObservable, chatForChannel);
    }

    @Test
    public void subscriberReceivesErrorWhenChatCannotBeObserved() {
        Throwable testThrowable = new Throwable("test throwable");
        setupErroringValueEventListenerFor(mockListeners, mockMessagesDatabase, testThrowable);

        Observable<Chat> observable = firebaseChatDatabase.observeChat(channel);

        assertThrowableReceivedOnError(observable, testThrowable);
    }

    @Test
    public void newMessagesArePushedThenSet() {
        Message message = new Message(anotherUser, "another message");

        firebaseChatDatabase.sendMessage(channel, message);

        verify(mockMessagesDatabase).push();
        verify(mockMessagesDatabase).setValue(message);
    }

}
