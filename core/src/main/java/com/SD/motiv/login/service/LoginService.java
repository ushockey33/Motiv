package com.SD.motiv.login.service;

import com.SD.motiv.login.data.model.Authentication;

import io.reactivex.Observable;

public interface LoginService {

    Observable<Authentication> getAuthentication();

    void loginWithGoogle(String idToken);

}
