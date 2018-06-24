package com.trigma.tiktok.adapter.patient;

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
import com.facebook.appevents.AppEventsConstants;
import com.squareup.picasso.Picasso;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.activity.patient.PatientSideStaffList;
import com.trigma.tiktok.model.DrStaffListObject;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.presenter.patient.PatientSideStaffListPresenter;
import com.trigma.tiktok.utils.CircleTransform;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.SharedPreff;
import java.util.ArrayList;
import org.objectweb.asm.Opcodes;

public class PatientSideStaffAdapter extends Adapter<MyViewHolder> {
    public ArrayList<DrStaffListObject> dataList = new ArrayList();
    private LoginDocResponce loginDocResponce;
    PatientSideStaffListPresenter patientSideStaffListPresenter;
    PatientSideStaffList view;

    public class MyViewHolder extends ViewHolder implements OnClickListener {
        public CardView card_view;
        public View empty_view;
        public View empty_view2;
        public ImageView img_chat;
        public ImageView img_user;
        public LinearLayout lin_parent;
        public LinearLayout ll_buttons;
        public TextView tv_designation;
        public TextView tv_gender;
        public TextView tv_name;

        public MyViewHolder(View view) {
            super(view);
            this.img_user = (ImageView) view.findViewById(C1020R.id.img_user);
            this.tv_name = (TextView) view.findViewById(C1020R.id.tv_name);
            this.tv_gender = (TextView) view.findViewById(C1020R.id.tv_gender);
            this.tv_designation = (TextView) view.findViewById(C1020R.id.tv_designation);
            this.lin_parent = (LinearLayout) view.findViewById(C1020R.id.lin_parent);
            this.ll_buttons = (LinearLayout) view.findViewById(C1020R.id.ll_buttons);
            this.img_chat = (ImageView) view.findViewById(C1020R.id.img_chat);
            this.card_view = (CardView) view.findViewById(C1020R.id.card_view);
            this.empty_view = view.findViewById(C1020R.id.empty_view);
            this.empty_view2 = view.findViewById(C1020R.id.empty_view2);
        }

        public void onClick(View v) {
        }
    }

    public PatientSideStaffAdapter(PatientSideStaffListPresenter patientSideStaffListPresenter, PatientSideStaffList view) {
        this.patientSideStaffListPresenter = patientSideStaffListPresenter;
        this.view = view;
        this.loginDocResponce = SharedPreff.getLoginResponce();
    }

    public void addingList(ArrayList<DrStaffListObject> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(C1020R.layout.patient_side_staff_list_item, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        final DrStaffListObject drStaffListObject = (DrStaffListObject) this.dataList.get(position);
        if (position == this.dataList.size() - 1) {
            holder.empty_view2.setVisibility(0);
        } else {
            holder.empty_view2.setVisibility(8);
        }
        holder.tv_name.setText(drStaffListObject.getName());
        if (position % 2 == 0) {
            holder.lin_parent.setBackgroundColor(ContextCompat.getColor(this.view, C1020R.color.white));
        } else {
            holder.lin_parent.setBackgroundColor(ContextCompat.getColor(this.view, C1020R.color.light_white));
        }
        holder.tv_gender.setText(drStaffListObject.getGender());
        holder.tv_designation.setText(drStaffListObject.getDesignation());
        Picasso.with(this.view).load(Constants.HTTP + drStaffListObject.getProfilePic()).resize(Opcodes.FCMPG, Opcodes.FCMPG).transform(new CircleTransform()).placeholder((int) C1020R.drawable.profile_circle).error((int) C1020R.drawable.profile_circle).into(holder.img_user);
        holder.img_chat.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PatientSideStaffAdapter.this.patientSideStaffListPresenter.chatNowApi(drStaffListObject.getStaffId(), drStaffListObject);
            }
        });
        holder.lin_parent.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PatientSideStaffAdapter.this.patientSideStaffListPresenter.gotoStaffDetail(drStaffListObject);
            }
        });
    }

    public int getItemCount() {
        return this.dataList.size();
    }

    public void deleted(int pos) {
        if (this.dataList.size() > 0) {
            this.dataList.remove(pos);
            notifyDataSetChanged();
        }
    }

    public void activateDeactivate(int pos, String activate_status) {
        if (this.dataList.size() <= 0) {
            return;
        }
        DrStaffListObject drStaffListObject;
        if (activate_status.equalsIgnoreCase(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
            drStaffListObject = (DrStaffListObject) this.dataList.get(pos);
            drStaffListObject.setStaffStatus(0);
            this.dataList.set(pos, drStaffListObject);
            notifyDataSetChanged();
        } else if (!activate_status.equalsIgnoreCase("5")) {
            drStaffListObject = (DrStaffListObject) this.dataList.get(pos);
            drStaffListObject.setStaffStatus(1);
            this.dataList.set(pos, drStaffListObject);
            notifyDataSetChanged();
        } else if (this.dataList.size() > 0) {
            this.dataList.remove(pos);
            notifyDataSetChanged();
        }
    }
}
