package com.SD.motiv.profile.displayer;

/**
 * Created by ushoc_000 on 7/23/2017.
 */

public interface ProfileDisplayer {

    void attach(ProfileActionListener actionListener);

    void detach(ProfileActionListener actionListener);

    void setTitle(String title); //TODO Should not set title in this way, these methods should be for actions only not static.

    void setProfilePicture(String picture);

    interface ProfileActionListener{

        void onOwnsClicked();

    }
}
