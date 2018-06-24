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
import com.facebook.appevents.AppEventsConstants;
import com.squareup.picasso.Picasso;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.activity.DoctorStaff;
import com.trigma.tiktok.model.DrStaffListObject;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.presenter.DoctorStaffPresenter;
import com.trigma.tiktok.utils.CircleTransform;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackWithButtonsInterface;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import java.util.ArrayList;
import org.objectweb.asm.Opcodes;

public class DoctorStaffAdapter extends Adapter<MyViewHolder> {
    private static String DEACTIVATE_ACTIVATE = AppEventsConstants.EVENT_PARAM_VALUE_NO;
    private static final String DELETE = "5";
    public ArrayList<DrStaffListObject> dataList = new ArrayList();
    private String doctorID = "";
    DoctorStaffPresenter doctorStaffPresenter;
    private boolean hide_deleteButton = false;
    private LoginDocResponce loginDocResponce;
    DoctorStaff view;

    public class MyViewHolder extends ViewHolder implements OnClickListener {
        public CardView card_view;
        public View empty_view;
        public View empty_view2;
        public ImageView img_chat;
        public ImageView img_user;
        public LinearLayout lin_parent;
        public LinearLayout ll_buttons;
        public TextView tv_confirm;
        public TextView tv_delete;
        public TextView tv_designation;
        public TextView tv_gender;
        public TextView tv_name;

        public MyViewHolder(View view) {
            super(view);
            this.img_user = (ImageView) view.findViewById(C1020R.id.img_user);
            this.tv_name = (TextView) view.findViewById(C1020R.id.tv_name);
            this.tv_gender = (TextView) view.findViewById(C1020R.id.tv_gender);
            this.tv_designation = (TextView) view.findViewById(C1020R.id.tv_designation);
            this.tv_confirm = (TextView) view.findViewById(C1020R.id.tv_confirm);
            this.tv_delete = (TextView) view.findViewById(C1020R.id.tv_delete);
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

    public DoctorStaffAdapter(DoctorStaffPresenter doctorStaffPresenter, DoctorStaff view, String doctorID) {
        this.doctorStaffPresenter = doctorStaffPresenter;
        this.view = view;
        this.doctorID = doctorID;
        this.loginDocResponce = SharedPreff.getLoginResponce();
        if (this.loginDocResponce == null) {
            return;
        }
        if (this.loginDocResponce.getData().get_id().equalsIgnoreCase(doctorID)) {
            this.hide_deleteButton = false;
        } else {
            this.hide_deleteButton = true;
        }
    }

    public void addingList(ArrayList<DrStaffListObject> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(C1020R.layout.doctor_staff_item_list, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, final int position) {
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
        if (drStaffListObject.getStaffStatus() == 1) {
            holder.tv_confirm.setText(this.view.getResources().getString(C1020R.string.deactivate));
            holder.img_chat.setVisibility(0);
        } else {
            holder.tv_confirm.setText(this.view.getResources().getString(C1020R.string.activate));
            holder.img_chat.setVisibility(8);
        }
        if (SharedPreff.getStaffLoginResponse() != null) {
            holder.ll_buttons.setVisibility(8);
        } else {
            holder.ll_buttons.setVisibility(0);
        }
        holder.tv_delete.setOnClickListener(new OnClickListener() {

            class C11131 implements AlertCallBackWithButtonsInterface {
                C11131() {
                }

                public void positiveClick() {
                    DoctorStaffAdapter.this.doctorStaffPresenter.deactivateRequest(DoctorStaffAdapter.DELETE, DoctorStaffAdapter.this.doctorID, position, drStaffListObject.getStaffId(), DoctorStaffAdapter.this.view.getResources().getString(C1020R.string.deleted));
                }

                public void neutralClick() {
                }

                public void negativeClick() {
                }
            }

            public void onClick(View v) {
                DialogPopUps.showAlertWithButtons(DoctorStaffAdapter.this.view, DoctorStaffAdapter.this.view.getResources().getString(C1020R.string.alert), DoctorStaffAdapter.this.view.getResources().getString(C1020R.string.delete_deactivate_text) + " " + drStaffListObject.getName() + "?", DoctorStaffAdapter.this.view.getResources().getString(C1020R.string.yes_dialog), DoctorStaffAdapter.this.view.getResources().getString(C1020R.string.no_dialog), "", false, true, new C11131());
            }
        });
        holder.tv_confirm.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String buttonText = "";
                if (drStaffListObject.getStaffStatus() == 1) {
                    DoctorStaffAdapter.DEACTIVATE_ACTIVATE = AppEventsConstants.EVENT_PARAM_VALUE_NO;
                    buttonText = DoctorStaffAdapter.this.view.getResources().getString(C1020R.string.deactivated);
                } else {
                    DoctorStaffAdapter.DEACTIVATE_ACTIVATE = AppEventsConstants.EVENT_PARAM_VALUE_YES;
                    buttonText = DoctorStaffAdapter.this.view.getResources().getString(C1020R.string.activated);
                }
                DoctorStaffAdapter.this.doctorStaffPresenter.deactivateRequest(DoctorStaffAdapter.DEACTIVATE_ACTIVATE, DoctorStaffAdapter.this.doctorID, position, drStaffListObject.getStaffId(), buttonText);
            }
        });
        holder.img_chat.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DoctorStaffAdapter.this.doctorStaffPresenter.chatNowApi(drStaffListObject.getStaffId(), drStaffListObject);
            }
        });
        holder.lin_parent.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DoctorStaffAdapter.this.doctorStaffPresenter.gotoStaffDetail(drStaffListObject);
            }
        });
        if (this.hide_deleteButton) {
            holder.tv_confirm.setVisibility(8);
            holder.tv_delete.setVisibility(8);
            return;
        }
        holder.tv_confirm.setVisibility(0);
        holder.tv_delete.setVisibility(0);
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
        } else if (!activate_status.equalsIgnoreCase(DELETE)) {
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
