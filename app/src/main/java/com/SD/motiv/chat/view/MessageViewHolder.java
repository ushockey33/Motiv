package com.SD.motiv.chat.view;

import android.support.v7.widget.RecyclerView;

import com.SD.motiv.chat.data.model.Message;

class MessageViewHolder extends RecyclerView.ViewHolder {

    private final MessageView messageView;

    public MessageViewHolder(MessageView messageView) {
        super(messageView);
        this.messageView = messageView;
    }

    public void bind(Message message) {
        messageView.display(message);
    }
}
