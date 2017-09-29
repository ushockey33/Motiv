package com.SD.motiv.login.database;

import com.SD.motiv.login.data.model.Authentication;

import io.reactivex.Observable;

public interface AuthDatabase {

    Observable<Authentication> readAuthentication();

    Observable<Authentication> loginWithGoogle(String idToken);

}
