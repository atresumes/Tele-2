package com.trigma.tiktok.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.model.ChatMessage;
import com.trigma.tiktok.utils.CommonUtils;
import java.util.ArrayList;

public class ChatAdapter extends Adapter {
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private Activity chatScreen;
    private ArrayList<ChatMessage> messageList = new ArrayList();
    private int sdp_100 = 100;
    private int userType;
    private int width = 0;

    private class ReceivedMessageHolder extends ViewHolder {
        public View empty_view;
        public View empty_view2;
        ImageView img_link;
        RelativeLayout lin_img_view;
        LinearLayout lin_msg_view;
        ProgressBar progress_bar;
        TextView tv_message;
        TextView tv_name;
        TextView tv_name_2;

        class C11061 implements Target {
            C11061() {
            }

            public void onBitmapLoaded(Bitmap bitmap, LoadedFrom from) {
                ReceivedMessageHolder.this.img_link.setImageBitmap(bitmap);
                ReceivedMessageHolder.this.progress_bar.setVisibility(8);
            }

            public void onBitmapFailed(Drawable errorDrawable) {
                ReceivedMessageHolder.this.img_link.setImageResource(C1020R.drawable.image_placeholder);
            }

            public void onPrepareLoad(Drawable placeHolderDrawable) {
                ReceivedMessageHolder.this.img_link.setImageResource(C1020R.drawable.image_placeholder);
                ReceivedMessageHolder.this.progress_bar.setVisibility(0);
            }
        }

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            this.tv_name = (TextView) itemView.findViewById(C1020R.id.tv_name);
            this.tv_message = (TextView) itemView.findViewById(C1020R.id.tv_message);
            this.tv_name_2 = (TextView) itemView.findViewById(C1020R.id.tv_name_2);
            this.img_link = (ImageView) itemView.findViewById(C1020R.id.img_link);
            this.empty_view = itemView.findViewById(C1020R.id.empty_view);
            this.empty_view2 = itemView.findViewById(C1020R.id.empty_view2);
            this.lin_msg_view = (LinearLayout) itemView.findViewById(C1020R.id.lin_msg_view);
            this.lin_img_view = (RelativeLayout) itemView.findViewById(C1020R.id.lin_img_view);
            this.progress_bar = (ProgressBar) itemView.findViewById(C1020R.id.progress_bar);
        }

        void bind(final ChatMessage message) {
            this.tv_name.setText(message.getName() + ":-");
            this.tv_name_2.setText(message.getName());
            this.tv_message.setText(CommonUtils.getChatSubString(message.getMessageText()));
            if (message.getType().equalsIgnoreCase("link")) {
                this.lin_msg_view.setVisibility(8);
                this.lin_img_view.setVisibility(0);
                if (CommonUtils.isImage(message.getLink())) {
                    Picasso.with(ChatAdapter.this.chatScreen).load(message.getLink()).resize((ChatAdapter.this.width * 70) / 100, ChatAdapter.this.sdp_100).placeholder((int) C1020R.drawable.image_placeholder).into(new C11061());
                } else if (CommonUtils.isMedia(message.getLink())) {
                    this.img_link.setImageResource(C1020R.drawable.video_placeholder);
                } else {
                    this.img_link.setImageResource(C1020R.drawable.document_placeholder);
                }
                this.img_link.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        CommonUtils.openLink(ChatAdapter.this.chatScreen, message.getLink());
                    }
                });
                return;
            }
            this.lin_msg_view.setVisibility(0);
            this.lin_img_view.setVisibility(8);
        }
    }

    private class SentMessageHolder extends ViewHolder {
        public View empty_view;
        public View empty_view2;
        ImageView img_link;
        RelativeLayout lin_img_view;
        LinearLayout lin_msg_view;
        ProgressBar progress_bar;
        TextView tv_message;
        TextView tv_name;
        TextView tv_name_2;

        class C11081 implements Target {
            C11081() {
            }

            public void onBitmapLoaded(Bitmap bitmap, LoadedFrom from) {
                SentMessageHolder.this.img_link.setImageBitmap(bitmap);
                SentMessageHolder.this.progress_bar.setVisibility(8);
            }

            public void onBitmapFailed(Drawable errorDrawable) {
                SentMessageHolder.this.img_link.setImageResource(C1020R.drawable.image_placeholder);
            }

            public void onPrepareLoad(Drawable placeHolderDrawable) {
                SentMessageHolder.this.img_link.setImageResource(C1020R.drawable.image_placeholder);
                SentMessageHolder.this.progress_bar.setVisibility(0);
            }
        }

        SentMessageHolder(View itemView) {
            super(itemView);
            this.tv_name = (TextView) itemView.findViewById(C1020R.id.tv_name);
            this.tv_message = (TextView) itemView.findViewById(C1020R.id.tv_message);
            this.tv_name_2 = (TextView) itemView.findViewById(C1020R.id.tv_name_2);
            this.img_link = (ImageView) itemView.findViewById(C1020R.id.img_link);
            this.empty_view = itemView.findViewById(C1020R.id.empty_view);
            this.empty_view2 = itemView.findViewById(C1020R.id.empty_view2);
            this.lin_msg_view = (LinearLayout) itemView.findViewById(C1020R.id.lin_msg_view);
            this.lin_img_view = (RelativeLayout) itemView.findViewById(C1020R.id.lin_img_view);
            this.progress_bar = (ProgressBar) itemView.findViewById(C1020R.id.progress_bar);
        }

        void bind(final ChatMessage message) {
            this.tv_name.setText(message.getName() + ":-");
            this.tv_name_2.setText(message.getName());
            this.tv_message.setText(CommonUtils.getChatSubString(message.getMessageText()));
            if (message.getType().equalsIgnoreCase("link")) {
                this.lin_msg_view.setVisibility(8);
                this.lin_img_view.setVisibility(0);
                if (CommonUtils.isImage(message.getLink())) {
                    Picasso.with(ChatAdapter.this.chatScreen).load(message.getLink()).resize((ChatAdapter.this.width * 70) / 100, ChatAdapter.this.sdp_100).placeholder((int) C1020R.drawable.image_placeholder).into(new C11081());
                } else if (CommonUtils.isMedia(message.getLink())) {
                    this.img_link.setImageResource(C1020R.drawable.video_placeholder);
                } else {
                    this.img_link.setImageResource(C1020R.drawable.document_placeholder);
                }
                this.img_link.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        CommonUtils.openLink(ChatAdapter.this.chatScreen, message.getLink());
                    }
                });
                return;
            }
            this.lin_msg_view.setVisibility(0);
            this.lin_img_view.setVisibility(8);
        }
    }

    public ChatAdapter(Activity chatScreen, int userType, int width) {
        this.chatScreen = chatScreen;
        this.userType = userType;
        this.width = width;
        this.sdp_100 = (int) chatScreen.getResources().getDimension(C1020R.dimen._100sdp);
    }

    public void addingMessage(ChatMessage chatMessage) {
        this.messageList.add(chatMessage);
        notifyDataSetChanged();
    }

    public void addingWholeList(ArrayList<ChatMessage> messageList) {
        this.messageList = messageList;
        notifyDataSetChanged();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new SentMessageHolder(LayoutInflater.from(parent.getContext()).inflate(C1020R.layout.publisher_list_item, parent, false));
        }
        if (viewType == 2) {
            return new ReceivedMessageHolder(LayoutInflater.from(parent.getContext()).inflate(C1020R.layout.subscriber_list_item, parent, false));
        }
        return null;
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        ChatMessage message = (ChatMessage) this.messageList.get(position);
        switch (holder.getItemViewType()) {
            case 1:
                if (position == this.messageList.size() - 1) {
                    ((SentMessageHolder) holder).empty_view2.setVisibility(0);
                } else {
                    ((SentMessageHolder) holder).empty_view2.setVisibility(8);
                }
                ((SentMessageHolder) holder).bind(message);
                return;
            case 2:
                if (position == this.messageList.size() - 1) {
                    ((ReceivedMessageHolder) holder).empty_view2.setVisibility(0);
                } else {
                    ((ReceivedMessageHolder) holder).empty_view2.setVisibility(8);
                }
                ((ReceivedMessageHolder) holder).bind(message);
                return;
            default:
                return;
        }
    }

    public int getItemCount() {
        return this.messageList.size();
    }

    public int getItemViewType(int position) {
        if (((ChatMessage) this.messageList.get(position)).isSameUser()) {
            return 1;
        }
        return 2;
    }
}
