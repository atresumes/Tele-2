package com.trigma.tiktok.adapter;

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
import com.trigma.tiktok.fragments.MessagesScreen;
import com.trigma.tiktok.model.MessageUserObject;
import com.trigma.tiktok.presenter.MessagesScreenPres;
import com.trigma.tiktok.utils.CircleTransform;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.SharedPreff;
import java.util.ArrayList;
import org.objectweb.asm.Opcodes;

public class MessagesAdapter extends Adapter<MyViewHolder> {
    private int isDoc = 0;
    String looggedin_user_id = "";
    MessagesScreenPres messagesScreenPres;
    private ArrayList<MessageUserObject> user = new ArrayList();
    MessagesScreen view;

    public class MyViewHolder extends ViewHolder {
        public View empty_view;
        public View empty_view2;
        public ImageView img_doc_sign;
        public ImageView img_user;
        public LinearLayout lin_parent;
        public TextView tv_age;
        public TextView tv_gender;
        public TextView tv_name;
        public TextView tv_not_cout;

        public MyViewHolder(View view) {
            super(view);
            this.img_user = (ImageView) view.findViewById(C1020R.id.img_user);
            this.img_doc_sign = (ImageView) view.findViewById(C1020R.id.img_doc_sign);
            this.tv_name = (TextView) view.findViewById(C1020R.id.tv_name);
            this.tv_age = (TextView) view.findViewById(C1020R.id.tv_age);
            this.tv_gender = (TextView) view.findViewById(C1020R.id.tv_gender);
            this.tv_not_cout = (TextView) view.findViewById(C1020R.id.tv_not_cout);
            this.lin_parent = (LinearLayout) view.findViewById(C1020R.id.lin_parent);
            this.empty_view = view.findViewById(C1020R.id.empty_view);
            this.empty_view2 = view.findViewById(C1020R.id.empty_view2);
        }
    }

    public MessagesAdapter(MessagesScreenPres messagesScreenPres, MessagesScreen view, int isDoc) {
        this.messagesScreenPres = messagesScreenPres;
        this.view = view;
        this.isDoc = isDoc;
        if (SharedPreff.getStaffLoginResponse() != null) {
            this.looggedin_user_id = SharedPreff.getStaffLoginResponse().getData().get_id();
        } else {
            this.looggedin_user_id = SharedPreff.getLoginResponce().getData().get_id();
        }
    }

    public void addingList(ArrayList<MessageUserObject> user) {
        this.user = user;
        notifyDataSetChanged();
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(C1020R.layout.message_list_item, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (position == this.user.size() - 1) {
            holder.empty_view2.setVisibility(0);
        } else {
            holder.empty_view2.setVisibility(8);
        }
        final MessageUserObject messageUserObject = (MessageUserObject) this.user.get(position);
        if (messageUserObject.getUserType() == 1) {
            holder.img_doc_sign.setVisibility(0);
            if (messageUserObject.getName() != null) {
                if (messageUserObject.getName().contains(this.view.getActivity().getResources().getString(C1020R.string.dr))) {
                    holder.tv_name.setText(messageUserObject.getName());
                } else {
                    holder.tv_name.setText(this.view.getActivity().getResources().getString(C1020R.string.dr) + " " + messageUserObject.getName());
                }
            }
            holder.tv_age.setText(messageUserObject.getUser().getSpeciality());
        } else if (messageUserObject.getUserType() == 3) {
            holder.img_doc_sign.setVisibility(4);
            holder.tv_name.setText(messageUserObject.getName());
            holder.tv_age.setText(messageUserObject.getUser().getDesignation());
        } else {
            holder.img_doc_sign.setVisibility(4);
            holder.tv_name.setText(messageUserObject.getName());
            holder.tv_age.setText(messageUserObject.getDOB());
        }
        holder.tv_gender.setText(messageUserObject.getGender());
        if (messageUserObject.getMessagedotshow() == 0) {
            holder.tv_not_cout.setVisibility(0);
        } else {
            holder.tv_not_cout.setVisibility(8);
        }
        Picasso.with(this.view.getActivity()).load(Constants.HTTP + messageUserObject.getProfilePic()).transform(new CircleTransform()).resize(Opcodes.FCMPG, Opcodes.FCMPG).placeholder((int) C1020R.drawable.profile_circle).error((int) C1020R.drawable.profile_circle).into(holder.img_user);
        holder.lin_parent.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                boolean isFetch;
                if (messageUserObject.getMessagedotshow() == 0) {
                    isFetch = true;
                } else {
                    isFetch = false;
                }
                MessagesAdapter.this.messagesScreenPres.goToChatScreen(messageUserObject, messageUserObject.getId(), isFetch);
            }
        });
    }

    public int getItemCount() {
        return this.user.size();
    }
}
