package com.trigma.tiktok.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.activity.DoctorStaff;
import com.trigma.tiktok.activity.MainActivity;
import com.trigma.tiktok.fragments.ContactUs;
import com.trigma.tiktok.fragments.CreateSchedule;
import com.trigma.tiktok.fragments.MyAppointments;
import com.trigma.tiktok.fragments.MyPatients;
import com.trigma.tiktok.fragments.MyProfile;
import com.trigma.tiktok.fragments.Notification;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.presenter.MainActivityPresenter;
import com.trigma.tiktok.presenter.MainActivityView;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.SharedPreff;
import de.hdodenhof.circleimageview.CircleImageView;
import java.io.File;
import org.objectweb.asm.Opcodes;

public class NavigationAdapter extends Adapter<ViewHolder> {
    Context context;
    TypedArray icons;
    File imageFile;
    private LoginDocResponce loginDocResponce = SharedPreff.getLoginResponce();
    public MainActivityPresenter mainActivityPresenter;
    String[] titles;

    class C11191 implements OnClickListener {
        C11191() {
        }

        public void onClick(View v) {
            MainActivity mainActivity = NavigationAdapter.this.context;
            MainActivity.drawerLayout.closeDrawers();
            FragmentTransaction fragmentTransaction = mainActivity.getSupportFragmentManager().beginTransaction();
            MyProfile myProfile = new MyProfile();
            Bundle bundle = new Bundle();
            bundle.putString("title", NavigationAdapter.this.context.getResources().getString(C1020R.string.edit_profile));
            myProfile.setArguments(bundle);
            fragmentTransaction.replace(C1020R.id.containerView, myProfile);
            fragmentTransaction.commit();
        }
    }

    public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder implements OnClickListener {
        Context context;
        TextView doc_name;
        ImageView img_edit;
        ImageView navIcon;
        TextView navTitle;
        CircleImageView profile_image;
        TextView speciality;
        View v_line;

        public ViewHolder(View drawerItem, int itemType, Context context) {
            super(drawerItem);
            this.context = context;
            drawerItem.setOnClickListener(this);
            if (itemType == 1) {
                this.navTitle = (TextView) this.itemView.findViewById(C1020R.id.tv_NavTitle);
                this.v_line = this.itemView.findViewById(C1020R.id.v_line);
                this.navIcon = (ImageView) this.itemView.findViewById(C1020R.id.iv_NavIcon);
            } else if (itemType == 0) {
                this.doc_name = (TextView) this.itemView.findViewById(C1020R.id.doc_name);
                this.speciality = (TextView) this.itemView.findViewById(C1020R.id.speciality);
                this.profile_image = (CircleImageView) this.itemView.findViewById(C1020R.id.profile_image);
                this.img_edit = (ImageView) this.itemView.findViewById(C1020R.id.img_edit);
            }
        }

        public void onClick(View v) {
            MainActivity mainActivity = this.context;
            MainActivity.drawerLayout.closeDrawers();
            FragmentTransaction fragmentTransaction = mainActivity.getSupportFragmentManager().beginTransaction();
            Log.e("getAdapterPosition", "" + getAdapterPosition());
            switch (getAdapterPosition()) {
                case 1:
                    fragmentTransaction.replace(C1020R.id.containerView, new MyAppointments());
                    fragmentTransaction.commit();
                    return;
                case 2:
                    fragmentTransaction.replace(C1020R.id.containerView, new CreateSchedule());
                    fragmentTransaction.commit();
                    return;
                case 3:
                    fragmentTransaction.replace(C1020R.id.containerView, new MyPatients());
                    fragmentTransaction.commit();
                    return;
                case 4:
                    NavigationAdapter.this.mainActivityPresenter.callPrescriptionApi();
                    return;
                case 5:
                    fragmentTransaction.replace(C1020R.id.containerView, new MyProfile());
                    fragmentTransaction.commit();
                    return;
                case 6:
                    LoginDocResponce loginDocResponce = SharedPreff.getLoginResponce();
                    if (loginDocResponce != null) {
                        Intent signUp = new Intent(this.context, DoctorStaff.class);
                        signUp.putExtra(Constants.SHOW_BACK, true);
                        signUp.putExtra(Constants.HIDE_DEACTIVATE, false);
                        signUp.putExtra(Constants.DOCTOR_ID, loginDocResponce.getData().get_id());
                        signUp.putExtra(Constants.DOCTOR_NAME, this.context.getResources().getString(C1020R.string.dr) + " " + loginDocResponce.getData().getFirstName() + " " + loginDocResponce.getData().getLastName());
                        this.context.startActivity(signUp);
                        return;
                    }
                    return;
                case 7:
                    fragmentTransaction.replace(C1020R.id.containerView, new Notification());
                    fragmentTransaction.commit();
                    return;
                case 8:
                    fragmentTransaction.replace(C1020R.id.containerView, new ContactUs());
                    fragmentTransaction.commit();
                    return;
                case 9:
                    ((MainActivityView) this.context).logoutClicked();
                    return;
                default:
                    return;
            }
        }
    }

    public NavigationAdapter(String[] titles, TypedArray icons, Context context, File imageFile, MainActivityPresenter mainActivityPresenter) {
        this.titles = titles;
        this.icons = icons;
        this.context = context;
        this.imageFile = imageFile;
        this.mainActivityPresenter = mainActivityPresenter;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService("layout_inflater");
        if (viewType == 1) {
            return new ViewHolder(layoutInflater.inflate(C1020R.layout.drawer_item_layout, parent, false), viewType, this.context);
        }
        if (viewType == 0) {
            return new ViewHolder(layoutInflater.inflate(C1020R.layout.header_layout, parent, false), viewType, this.context);
        }
        return null;
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position != 0) {
            holder.navTitle.setText(this.titles[position - 1]);
            holder.navIcon.setImageResource(this.icons.getResourceId(position - 1, -1));
        } else if (position == 0) {
            holder.doc_name.setText(this.loginDocResponce.getData().getFirstName() + " " + this.loginDocResponce.getData().getLastName() + "," + this.loginDocResponce.getData().getQualification());
            holder.speciality.setText(this.loginDocResponce.getData().getSpeciality());
            if (this.imageFile != null) {
                Picasso.with(this.context).load(this.imageFile).into(holder.profile_image);
            } else if (this.loginDocResponce.getData().getProfilePic() != null) {
                if (this.loginDocResponce.getData().getProfilePic().contains("http") || this.loginDocResponce.getData().getProfilePic().contains("https")) {
                    Picasso.with(this.context).load(this.loginDocResponce.getData().getProfilePic()).resize(Opcodes.FCMPG, Opcodes.FCMPG).placeholder((int) C1020R.drawable.profile_circle).error((int) C1020R.drawable.profile_circle).into(holder.profile_image);
                    Log.e("contains http", "called");
                } else {
                    Picasso.with(this.context).load(Constants.HTTP + this.loginDocResponce.getData().getProfilePic()).resize(Opcodes.FCMPG, Opcodes.FCMPG).placeholder((int) C1020R.drawable.profile_circle).error((int) C1020R.drawable.profile_circle).into(holder.profile_image);
                    Log.e("contains not", "called");
                }
            }
            holder.img_edit.setOnClickListener(new C11191());
        }
    }

    public int getItemCount() {
        return this.titles.length + 1;
    }

    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return 1;
    }
}
