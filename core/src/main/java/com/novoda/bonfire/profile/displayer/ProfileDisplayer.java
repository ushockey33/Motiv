package com.novoda.bonfire.profile.displayer;

/**
 * Created by ushoc_000 on 7/23/2017.
 */

public interface ProfileDisplayer {

    void attach(ProfileActionListener actionListener);

    void detach(ProfileActionListener actionListener);

    void setTitle(String title);

    interface ProfileActionListener{

        void onOwnsClicked();

    }
}
