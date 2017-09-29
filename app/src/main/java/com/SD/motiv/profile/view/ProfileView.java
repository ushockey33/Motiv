package com.SD.motiv.profile.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.support.v7.widget.Toolbar;


import com.SD.motiv.profile.displayer.ProfileDisplayer;
import com.SD.motiv.view.CircleCropImageTransformation;
import com.bumptech.glide.Glide;
import com.novoda.notils.caster.Views;

/**
 * Created by ushoc_000 on 7/23/2017.
 */

public class ProfileView extends LinearLayout implements ProfileDisplayer {

    private Toolbar toolbar;
    private ImageView picture;


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
        View.inflate(getContext(), com.SD.motiv.R.layout.merge_profile_view, this);
        toolbar = Views.findById(this, com.SD.motiv.R.id.toolbar);

        this.picture = Views.findById(this, com.SD.motiv.R.id.profile_image);
    }


    @Override
    public void attach(ProfileActionListener actionListener) {

    }

    @Override
    public void detach(ProfileActionListener actionListener) {

    }

    @Override
    public void setTitle(String title) {toolbar.setTitle(title);}

    public void setProfilePicture(String picture) {
        Context context = getContext();
        Glide.with(context)
                .load(picture)
                .error(com.SD.motiv.R.drawable.ic_person)
                .transform(new CircleCropImageTransformation(context))
                .into(this.picture);
    }
}
