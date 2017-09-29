package com.SD.motiv.user.service;

import com.SD.motiv.user.data.model.User;
import com.SD.motiv.user.data.model.Users;

import io.reactivex.Observable;

public interface UserService {

    Observable<Users> getAllUsers();

    Observable<User> getUser(String userId);

}
