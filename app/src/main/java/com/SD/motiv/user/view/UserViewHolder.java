package com.SD.motiv.user.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.SD.motiv.user.displayer.UsersDisplayer;

class UserViewHolder extends RecyclerView.ViewHolder{

    private final UserView userView;

    public UserViewHolder(UserView itemView) {
        super(itemView);
        this.userView = itemView;
    }

    public void bind(final UsersView.SelectableUser selectableUser, final UsersDisplayer.SelectionListener listener) {
        userView.display(selectableUser);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectableUser.isSelected) {
                    listener.onUserDeselected(selectableUser.user);
                } else {
                    listener.onUserSelected(selectableUser.user);
                }
            }
        });
    }
}
