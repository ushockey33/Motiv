package com.SD.motiv.link;

import com.SD.motiv.user.data.model.User;

import java.net.URI;

public interface LinkFactory {

    URI inviteLinkFrom(User user);

}
