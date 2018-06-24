package com.trigma.tiktok.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.activity.NotificationFromPush;
import com.trigma.tiktok.model.NotificationMainObject;
import com.trigma.tiktok.presenter.NotificationFromPushPresenter;
import com.trigma.tiktok.utils.CircleTransform;
import com.trigma.tiktok.utils.Constants;
import java.util.ArrayList;

public class NotificationFromPushAdapter extends Adapter<MyViewHolder> {
    public ArrayList<NotificationMainObject> notificationList = new ArrayList();
    NotificationFromPushPresenter notificationPresenter;
    NotificationFromPush view;

    public class MyViewHolder extends ViewHolder implements OnClickListener {
        public CardView card_view;
        public View empty_view;
        public View empty_view2;
        public ImageView img_user;
        public LinearLayout lin_parent;
        public TextView tv_message;
        public TextView tv_min;
        public TextView tv_title;

        public MyViewHolder(View view) {
            super(view);
            this.img_user = (ImageView) view.findViewById(C1020R.id.img_user);
            this.tv_title = (TextView) view.findViewById(C1020R.id.tv_title);
            this.tv_message = (TextView) view.findViewById(C1020R.id.tv_message);
            this.tv_min = (TextView) view.findViewById(C1020R.id.tv_min);
            this.lin_parent = (LinearLayout) view.findViewById(C1020R.id.lin_parent);
            this.card_view = (CardView) view.findViewById(C1020R.id.card_view);
            this.empty_view = view.findViewById(C1020R.id.empty_view);
            this.empty_view2 = view.findViewById(C1020R.id.empty_view2);
        }

        public void onClick(View v) {
        }
    }

    public NotificationFromPushAdapter(NotificationFromPushPresenter notificationPresenter, NotificationFromPush view) {
        this.notificationPresenter = notificationPresenter;
        this.view = view;
    }

    public void addingList(ArrayList<NotificationMainObject> notificationList) {
        this.notificationList = notificationList;
        notifyDataSetChanged();
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(C1020R.layout.notitfication_list_item, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (position == this.notificationList.size() - 1) {
            holder.empty_view2.setVisibility(0);
        } else {
            holder.empty_view2.setVisibility(8);
        }
        NotificationMainObject notificationMainObject = (NotificationMainObject) this.notificationList.get(position);
        holder.tv_title.setText(notificationMainObject.getDrDeatils().getFirstName() + " " + notificationMainObject.getDrDeatils().getLastName());
        holder.tv_message.setText(notificationMainObject.getMessage());
        holder.tv_min.setText(notificationMainObject.getDaysLeft());
        if (position % 2 == 0) {
            holder.lin_parent.setBackgroundColor(ContextCompat.getColor(this.view, C1020R.color.white));
        } else {
            holder.lin_parent.setBackgroundColor(ContextCompat.getColor(this.view, C1020R.color.light_white));
        }
        Picasso.with(this.view).load(Constants.HTTP + notificationMainObject.getProfile()).transform(new CircleTransform()).placeholder((int) C1020R.drawable.profile_circle).error((int) C1020R.drawable.profile_circle).into(holder.img_user);
    }

    public int getItemCount() {
        return this.notificationList.size();
    }
}
