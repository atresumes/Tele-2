package com.trigma.tiktok.adapter.patient;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.facebook.internal.AnalyticsEvents;
import com.squareup.picasso.Picasso;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.fragments.patient.MyDoctors;
import com.trigma.tiktok.model.DrSearchNameObject;
import com.trigma.tiktok.model.MyDoctorsObject;
import com.trigma.tiktok.presenter.patient.MyDoctorsPresenter;
import com.trigma.tiktok.utils.CircleTransform;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackWithButtonsInterface;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import io.techery.properratingbar.ProperRatingBar;
import java.util.ArrayList;

public class MyDoctorsAdapter extends Adapter<MyViewHolder> {
    private ArrayList<MyDoctorsObject> dataList = new ArrayList();
    private String drID = "";
    MyDoctorsPresenter myDoctorsPresenter;
    MyDoctors view;

    public class MyViewHolder extends ViewHolder implements OnClickListener {
        public View empty_view2;
        public ImageView img_user;
        public LinearLayout lin_parent;
        public ProperRatingBar rating_bar;
        public LinearLayout status_linear;
        public TextView tv_name;
        public TextView tv_qualification;
        public TextView tv_speciality;
        public TextView tv_status;

        public MyViewHolder(View view) {
            super(view);
            this.img_user = (ImageView) view.findViewById(C1020R.id.img_user);
            this.tv_name = (TextView) view.findViewById(C1020R.id.tv_name);
            this.tv_speciality = (TextView) view.findViewById(C1020R.id.tv_speciality);
            this.tv_status = (TextView) view.findViewById(C1020R.id.tv_status);
            this.tv_qualification = (TextView) view.findViewById(C1020R.id.tv_qualification);
            this.lin_parent = (LinearLayout) view.findViewById(C1020R.id.lin_parent);
            this.status_linear = (LinearLayout) view.findViewById(C1020R.id.status_linear);
            this.empty_view2 = view.findViewById(C1020R.id.empty_view2);
            this.rating_bar = (ProperRatingBar) view.findViewById(C1020R.id.rating_bar);
        }

        public void onClick(View v) {
        }
    }

    public MyDoctorsAdapter(MyDoctorsPresenter myDoctorsPresenter, MyDoctors view) {
        this.myDoctorsPresenter = myDoctorsPresenter;
        this.view = view;
    }

    public void addingList(ArrayList<MyDoctorsObject> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(C1020R.layout.my_doctor_item, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (position == this.dataList.size() - 1) {
            holder.empty_view2.setVisibility(0);
        } else {
            holder.empty_view2.setVisibility(8);
        }
        final MyDoctorsObject myDoctorsObject = (MyDoctorsObject) this.dataList.get(position);
        holder.tv_name.setText(this.view.getActivity().getResources().getString(C1020R.string.dr) + " " + myDoctorsObject.getName());
        holder.tv_speciality.setText(myDoctorsObject.getSpeciality());
        holder.tv_qualification.setText(myDoctorsObject.getQualification());
        if (position % 2 == 0) {
            holder.lin_parent.setBackgroundColor(ContextCompat.getColor(this.view.getActivity(), C1020R.color.white));
        } else {
            holder.lin_parent.setBackgroundColor(ContextCompat.getColor(this.view.getActivity(), C1020R.color.light_white));
        }
        holder.tv_status.setText(" " + getStatusValue(myDoctorsObject.getStatus()));
        holder.rating_bar.setRating(Math.round((float) ((MyDoctorsObject) this.dataList.get(position)).getRating()));
        Picasso.with(this.view.getActivity()).load(Constants.HTTP + myDoctorsObject.getProfilePic()).transform(new CircleTransform()).placeholder((int) C1020R.drawable.profile_circle).error((int) C1020R.drawable.profile_circle).into(holder.img_user);
        holder.lin_parent.setOnClickListener(new OnClickListener() {

            class C11281 implements AlertCallBackWithButtonsInterface {
                C11281() {
                }

                public void positiveClick() {
                    MyDoctorsAdapter.this.myDoctorsPresenter.sendRequestToDoc(myDoctorsObject.getDrId(), myDoctorsObject.getEmail(), myDoctorsObject.getName(), myDoctorsObject.getDeviceToken(), myDoctorsObject.getName(), myDoctorsObject.getDeviceType(), position);
                }

                public void neutralClick() {
                }

                public void negativeClick() {
                }
            }

            class C11292 implements AlertCallBackWithButtonsInterface {
                C11292() {
                }

                public void positiveClick() {
                    CommonUtils.call(MyDoctorsAdapter.this.view.getActivity(), myDoctorsObject.getCode() + "" + myDoctorsObject.getMobile());
                }

                public void neutralClick() {
                }

                public void negativeClick() {
                    CommonUtils.sendEmailTo(MyDoctorsAdapter.this.view.getActivity(), "", myDoctorsObject.getEmail());
                }
            }

            public void onClick(View v) {
                if (myDoctorsObject.getStatus() == 1) {
                    DrSearchNameObject drSearchNameObject = new DrSearchNameObject();
                    drSearchNameObject.setProfilePic(myDoctorsObject.getProfilePic());
                    drSearchNameObject.setProfile(myDoctorsObject.getProfilePic());
                    drSearchNameObject.setZipcode("");
                    drSearchNameObject.setSpeciality(myDoctorsObject.getSpeciality());
                    drSearchNameObject.setQualification(myDoctorsObject.getQualification());
                    drSearchNameObject.setAddress(myDoctorsObject.getAddress());
                    drSearchNameObject.setBio(myDoctorsObject.getBio());
                    drSearchNameObject.setCode(myDoctorsObject.getCode());
                    drSearchNameObject.setMobile(myDoctorsObject.getMobile());
                    drSearchNameObject.setDeviceToken(myDoctorsObject.getDeviceToken());
                    drSearchNameObject.setEmail(myDoctorsObject.getEmail());
                    drSearchNameObject.setLanguages(myDoctorsObject.getLanguages());
                    drSearchNameObject.setDrId(myDoctorsObject.getDrId());
                    drSearchNameObject.setName(myDoctorsObject.getName());
                    MyDoctorsAdapter.this.myDoctorsPresenter.gotoDocBio(drSearchNameObject);
                } else if (myDoctorsObject.getStatus() == 5) {
                } else {
                    if (myDoctorsObject.getStatus() == 2 || myDoctorsObject.getStatus() == 4) {
                        DialogPopUps.showAlertWithButtons(MyDoctorsAdapter.this.view.getActivity(), MyDoctorsAdapter.this.view.getActivity().getResources().getString(C1020R.string.send_request), MyDoctorsAdapter.this.view.getActivity().getResources().getString(C1020R.string.are_you_sure_you_want_to_send_request), MyDoctorsAdapter.this.view.getActivity().getResources().getString(C1020R.string.yes_dialog), MyDoctorsAdapter.this.view.getActivity().getResources().getString(C1020R.string.no_dialog), "", true, true, new C11281());
                        return;
                    }
                    DialogPopUps.showStatusAlert(MyDoctorsAdapter.this.view.getActivity(), MyDoctorsAdapter.this.view.getActivity().getResources().getString(C1020R.string.verification_pending), MyDoctorsAdapter.this.view.getActivity().getResources().getString(C1020R.string.dr) + " " + myDoctorsObject.getName() + " " + MyDoctorsAdapter.this.view.getActivity().getResources().getString(C1020R.string.has_not_accepted_yout_request), MyDoctorsAdapter.this.view.getActivity().getResources().getString(C1020R.string.call), MyDoctorsAdapter.this.view.getActivity().getResources().getString(C1020R.string.email), "", false, new C11292());
                }
            }
        });
        holder.status_linear.setOnClickListener(new OnClickListener() {

            class C11311 implements AlertCallBackWithButtonsInterface {
                C11311() {
                }

                public void positiveClick() {
                    MyDoctorsAdapter.this.myDoctorsPresenter.sendRequestToDoc(myDoctorsObject.getDrId(), myDoctorsObject.getEmail(), myDoctorsObject.getName(), myDoctorsObject.getDeviceToken(), myDoctorsObject.getName(), Constants.DEVICE_TYPE, position);
                }

                public void neutralClick() {
                }

                public void negativeClick() {
                }
            }

            class C11322 implements AlertCallBackWithButtonsInterface {
                C11322() {
                }

                public void positiveClick() {
                    CommonUtils.call(MyDoctorsAdapter.this.view.getActivity(), myDoctorsObject.getCode() + "" + myDoctorsObject.getMobile());
                }

                public void neutralClick() {
                }

                public void negativeClick() {
                    CommonUtils.sendEmailTo(MyDoctorsAdapter.this.view.getActivity(), "", myDoctorsObject.getEmail());
                }
            }

            public void onClick(View v) {
                if (myDoctorsObject.getStatus() == 1) {
                    DrSearchNameObject drSearchNameObject = new DrSearchNameObject();
                    drSearchNameObject.setProfilePic(myDoctorsObject.getProfilePic());
                    drSearchNameObject.setProfile(myDoctorsObject.getProfilePic());
                    drSearchNameObject.setZipcode("");
                    drSearchNameObject.setSpeciality(myDoctorsObject.getSpeciality());
                    drSearchNameObject.setQualification(myDoctorsObject.getQualification());
                    drSearchNameObject.setAddress(myDoctorsObject.getAddress());
                    drSearchNameObject.setBio(myDoctorsObject.getBio());
                    drSearchNameObject.setCode(myDoctorsObject.getCode());
                    drSearchNameObject.setMobile(myDoctorsObject.getMobile());
                    drSearchNameObject.setDeviceToken(myDoctorsObject.getDeviceToken());
                    drSearchNameObject.setEmail(myDoctorsObject.getEmail());
                    drSearchNameObject.setLanguages(myDoctorsObject.getLanguages());
                    drSearchNameObject.setDrId(myDoctorsObject.getDrId());
                    drSearchNameObject.setName(myDoctorsObject.getName());
                    MyDoctorsAdapter.this.myDoctorsPresenter.gotoDocBio(drSearchNameObject);
                } else if (myDoctorsObject.getStatus() == 5) {
                } else {
                    if (myDoctorsObject.getStatus() == 2 || myDoctorsObject.getStatus() == 4) {
                        DialogPopUps.showAlertWithButtons(MyDoctorsAdapter.this.view.getActivity(), MyDoctorsAdapter.this.view.getActivity().getResources().getString(C1020R.string.send_request), MyDoctorsAdapter.this.view.getActivity().getResources().getString(C1020R.string.are_you_sure_you_want_to_send_request), MyDoctorsAdapter.this.view.getActivity().getResources().getString(C1020R.string.yes_dialog), MyDoctorsAdapter.this.view.getActivity().getResources().getString(C1020R.string.no_dialog), "", true, true, new C11311());
                        return;
                    }
                    DialogPopUps.showStatusAlert(MyDoctorsAdapter.this.view.getActivity(), MyDoctorsAdapter.this.view.getActivity().getResources().getString(C1020R.string.verification_pending), MyDoctorsAdapter.this.view.getActivity().getResources().getString(C1020R.string.dr) + " " + myDoctorsObject.getName() + " " + MyDoctorsAdapter.this.view.getActivity().getResources().getString(C1020R.string.has_not_accepted_yout_request), MyDoctorsAdapter.this.view.getActivity().getResources().getString(C1020R.string.call), MyDoctorsAdapter.this.view.getActivity().getResources().getString(C1020R.string.email), "", false, new C11322());
                }
            }
        });
    }

    public int getItemCount() {
        return this.dataList.size();
    }

    public String getStatusValue(int val) {
        String s = "";
        if (val == 0) {
            return "Pending";
        }
        if (val == 2) {
            return "Rejected";
        }
        if (val == 1) {
            return "Verified";
        }
        if (val == 4) {
            return AnalyticsEvents.PARAMETER_DIALOG_OUTCOME_VALUE_CANCELLED;
        }
        if (val == 5) {
            return "Doctor Deactivated";
        }
        return s;
    }

    public void docRequestSent(int pos) {
        MyDoctorsObject myDoctorsObject = (MyDoctorsObject) this.dataList.get(pos);
        myDoctorsObject.setStatus(0);
        this.dataList.set(pos, myDoctorsObject);
        notifyItemChanged(pos);
    }
}
