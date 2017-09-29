package com.SD.motiv.user.database;

import com.SD.motiv.user.data.model.User;
import com.SD.motiv.user.data.model.Users;

import io.reactivex.Observable;

public interface UserDatabase {

    Observable<Users> observeUsers();

    Observable<User> readUserFrom(String userId);

    void writeCurrentUser(User user);

    Observable<User> observeUser(String userId);

}
