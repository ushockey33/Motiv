package com.SD.motiv.user.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.SD.motiv.view.CircleCropImageTransformation;
import com.bumptech.glide.Glide;
import com.novoda.notils.caster.Views;

public class UserView extends FrameLayout {

    private TextView name;
    private ImageView image;

    public UserView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), com.SD.motiv.R.layout.merge_user_item_view, this);
        name = Views.findById(this, com.SD.motiv.R.id.user_name);
        image = Views.findById(this, com.SD.motiv.R.id.user_image);
    }

    public void display(UsersView.SelectableUser user) {
        Context context = getContext();
        Glide.with(context)
                .load(user.user.getPhotoUrl())
                .error(com.SD.motiv.R.drawable.ic_person)
                .transform(new CircleCropImageTransformation(context))
                .into(image);
        name.setText(user.user.getName());
        setSelected(user.isSelected);
    }
}
