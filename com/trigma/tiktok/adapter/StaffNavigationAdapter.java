package com.trigma.tiktok.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.facebook.appevents.AppEventsConstants;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.activity.StaffHomeScreen;
import com.trigma.tiktok.model.LoginDocData;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.model.StaffDoctorListObject;
import com.trigma.tiktok.presenter.StaffHomePresenter;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.SharedPreff;
import java.util.ArrayList;

public class StaffNavigationAdapter extends Adapter<MyViewHolder> {
    StaffHomePresenter notificationPresenter;
    int selected_pos = 0;
    public ArrayList<StaffDoctorListObject> staffDoctorList = new ArrayList();
    StaffHomeScreen view;

    public class MyViewHolder extends ViewHolder implements OnClickListener {
        public RelativeLayout parent;
        public TextView tv_name;
        public TextView tv_not_cout;
        public TextView tv_speciality;

        public MyViewHolder(View view) {
            super(view);
            this.tv_name = (TextView) view.findViewById(C1020R.id.tv_name);
            this.tv_speciality = (TextView) view.findViewById(C1020R.id.tv_speciality);
            this.tv_not_cout = (TextView) view.findViewById(C1020R.id.tv_not_cout);
            this.parent = (RelativeLayout) view.findViewById(C1020R.id.parent);
        }

        public void onClick(View v) {
        }
    }

    public StaffNavigationAdapter(StaffHomePresenter notificationPresenter, StaffHomeScreen view) {
        this.notificationPresenter = notificationPresenter;
        this.view = view;
    }

    public void addingList(ArrayList<StaffDoctorListObject> staffDoctorList, boolean isFirstTime) {
        this.staffDoctorList = staffDoctorList;
        if (isFirstTime) {
            this.selected_pos = 0;
        }
        notifyDataSetChanged();
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(C1020R.layout.staff_navigation_adapter_item, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final StaffDoctorListObject staffDoctorListObject = (StaffDoctorListObject) this.staffDoctorList.get(position);
        if (staffDoctorListObject.getName().contains(this.view.getResources().getString(C1020R.string.dr))) {
            holder.tv_name.setText(staffDoctorListObject.getName());
        } else {
            holder.tv_name.setText(this.view.getResources().getString(C1020R.string.dr) + " " + staffDoctorListObject.getName());
        }
        holder.tv_speciality.setText(staffDoctorListObject.getSpeciality());
        if (staffDoctorListObject.getShowDot() == 1) {
            holder.tv_not_cout.setVisibility(0);
        } else {
            holder.tv_not_cout.setVisibility(8);
        }
        if (this.selected_pos == position) {
            holder.parent.setBackgroundColor(ContextCompat.getColor(this.view, C1020R.color.home_screen_tabs));
        } else {
            holder.parent.setBackgroundColor(ContextCompat.getColor(this.view, C1020R.color.white));
        }
        holder.parent.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                StaffNavigationAdapter.this.selected_pos = position;
                LoginDocResponce loginDocResponce = new LoginDocResponce();
                loginDocResponce.setStatus(Constants.STATUS_OK);
                LoginDocData loginDocData = new LoginDocData();
                loginDocData.setDrRequest(1);
                loginDocData.setDrCode(null);
                loginDocData.setEmail(staffDoctorListObject.getEmail());
                loginDocData.set_id(staffDoctorListObject.getDrId());
                loginDocData.setAddress("");
                loginDocData.setBio("");
                loginDocData.setCity("");
                loginDocData.setUserType(1);
                loginDocData.setAdminStatus(1);
                loginDocData.setCode(Integer.parseInt(staffDoctorListObject.getCode()));
                loginDocData.setDeviceType(Constants.DEVICE_TYPE);
                try {
                    loginDocData.setFirstName(CommonUtils.getFirstNameAndLastName(staffDoctorListObject.getName(), true));
                    loginDocData.setLastName(CommonUtils.getFirstNameAndLastName(staffDoctorListObject.getName(), false));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                loginDocData.setQualification("");
                loginDocData.setState("");
                loginDocData.setMobile(staffDoctorListObject.getMobile());
                loginDocData.setGender(staffDoctorListObject.getGender());
                loginDocData.setMediaType("M");
                loginDocData.setLanguages("");
                loginDocData.setLat(SharedPreff.getLat());
                loginDocData.setLong(SharedPreff.getLng());
                loginDocData.setZipcode("");
                loginDocData.setPharmacyName("");
                loginDocData.setLoginAllready(AppEventsConstants.EVENT_PARAM_VALUE_YES);
                loginDocData.setProfilePic(staffDoctorListObject.getProfilePic());
                loginDocData.setSpeciality(staffDoctorListObject.getSpeciality());
                loginDocResponce.setData(loginDocData);
                SharedPreff.saveLoginResponce(loginDocResponce);
                StaffNavigationAdapter.this.view.navigationItemClicked(position, null);
            }
        });
    }

    public int getItemCount() {
        return this.staffDoctorList.size();
    }
}
