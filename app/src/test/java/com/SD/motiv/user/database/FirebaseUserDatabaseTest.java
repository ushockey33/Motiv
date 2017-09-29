package com.SD.motiv.user.database;

import com.SD.motiv.helpers.FirebaseTestHelpers;
import com.SD.motiv.user.data.model.User;
import com.SD.motiv.user.data.model.Users;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.SD.motiv.rx.FirebaseObservableListeners;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Observable;

import static org.mockito.Mockito.verify;

public class FirebaseUserDatabaseTest {
    private static final String USER_ID = "test user id";
    private static final String ANOTHER_USER_ID = "another user id";

    private final User user = new User(USER_ID, "test username", "http://test.photo/url");
    private final User anotherUser = new User(ANOTHER_USER_ID, "another username", "http://another.url");

    private final Users users = new Users(Arrays.asList(user, anotherUser));

    @Mock
    FirebaseDatabase mockFirebaseDatabase;
    @Mock
    DatabaseReference mockUsersDatabase;
    @Mock
    FirebaseObservableListeners mockListeners;

    FirebaseUserDatabase firebaseUserDatabase;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        FirebaseTestHelpers.setupDatabaseStubsFor("users", mockUsersDatabase, mockFirebaseDatabase);

        FirebaseTestHelpers.setupValueEventListenerFor(mockListeners, mockUsersDatabase, users);

        FirebaseTestHelpers.setupSingleValueEventListenerFor(mockListeners, mockUsersDatabase, user);

        firebaseUserDatabase = new FirebaseUserDatabase(mockFirebaseDatabase, mockListeners);
    }

    @Test
    public void canObserveUsers() {
        Observable<Users> usersObservable = firebaseUserDatabase.observeUsers();
        FirebaseTestHelpers.assertValueReceivedOnNext(usersObservable, users);
    }

    @Test
    public void whenUsersCannotBeObservedOnErrorIsCalled() {
        Throwable testThrowable = new Throwable("test error");
        FirebaseTestHelpers.setupErroringValueEventListenerFor(mockListeners, mockUsersDatabase, testThrowable);

        Observable<Users> usersObservable = firebaseUserDatabase.observeUsers();

        FirebaseTestHelpers.assertThrowableReceivedOnError(usersObservable, testThrowable);
    }

    @Test
    public void canRetrieveUserObjectFromId() {
        Observable<User> userObservable = firebaseUserDatabase.readUserFrom(USER_ID);
        FirebaseTestHelpers.assertValueReceivedOnNext(userObservable, user);
    }

    @Test
    public void canObserveUserObjectFromId() {
        FirebaseTestHelpers.setupValueEventListenerFor(mockListeners, mockUsersDatabase, user);
        Observable<User> userObservable = firebaseUserDatabase.observeUser(USER_ID);
        FirebaseTestHelpers.assertValueReceivedOnNext(userObservable, user);
    }

    @Test
    public void canSetNewUserValue() {
        firebaseUserDatabase.writeCurrentUser(anotherUser);
        verify(mockUsersDatabase).setValue(anotherUser);
    }
}
