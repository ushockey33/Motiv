package com.novoda.bonfire.profile.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.support.v7.widget.Toolbar;


import com.novoda.bonfire.R;
import com.novoda.bonfire.profile.displayer.ProfileDisplayer;
import com.novoda.notils.caster.Views;

/**
 * Created by ushoc_000 on 7/23/2017.
 */

public class ProfileView extends LinearLayout implements ProfileDisplayer {

    private Toolbar toolbar;


    public ProfileView(Context context) {
        super(context);
        setOrientation(VERTICAL);
    }

    //Constructor that is called when inflating a view from XML.
    public ProfileView(Context context, AttributeSet attrs){
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    //Perform inflation from XML and apply a class-specific base style.
    public ProfileView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs);
        setOrientation(VERTICAL);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_profile_view, this);

        toolbar = Views.findById(this, R.id.toolbar);
        //toolbar.inflateMenu(R.menu.channels_menu);
    }


    @Override
    public void attach(ProfileActionListener actionListener) {

    }

    @Override
    public void detach(ProfileActionListener actionListener) {

    }

    @Override
    public void setTitle(String title) {toolbar.setTitle(title);}
}
