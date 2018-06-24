package com.trigma.tiktok.adapter.patient;

import android.content.Context;
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
import com.trigma.tiktok.activity.patient.PatientMainActivity;
import com.trigma.tiktok.fragments.ContactUs;
import com.trigma.tiktok.fragments.Notification;
import com.trigma.tiktok.fragments.patient.MyDoctors;
import com.trigma.tiktok.fragments.patient.MyInfo;
import com.trigma.tiktok.fragments.patient.PatientMyAppointments;
import com.trigma.tiktok.model.LoginDocResponce;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.SharedPreff;
import de.hdodenhof.circleimageview.CircleImageView;
import java.io.File;
import org.objectweb.asm.Opcodes;

public class NavigationPatientAdapter extends Adapter<ViewHolder> {
    Context context;
    TypedArray icons;
    File imageFile;
    private LoginDocResponce loginDocResponce = SharedPreff.getLoginResponce();
    String[] titles;

    class C11351 implements OnClickListener {
        C11351() {
        }

        public void onClick(View v) {
            PatientMainActivity mainActivity = NavigationPatientAdapter.this.context;
            PatientMainActivity.drawerLayout.closeDrawers();
            FragmentTransaction fragmentTransaction = mainActivity.getSupportFragmentManager().beginTransaction();
            MyInfo myprofile = new MyInfo();
            fragmentTransaction.replace(C1020R.id.containerView, myprofile);
            Bundle bundle = new Bundle();
            bundle.putString("title", NavigationPatientAdapter.this.context.getResources().getString(C1020R.string.my_profile));
            myprofile.setArguments(bundle);
            fragmentTransaction.commit();
        }
    }

    class C11362 implements OnClickListener {
        C11362() {
        }

        public void onClick(View v) {
            PatientMainActivity mainActivity = NavigationPatientAdapter.this.context;
            PatientMainActivity.drawerLayout.closeDrawers();
            ((PatientMainActivity) NavigationPatientAdapter.this.context).searchForDoc();
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
        TextView tv_confirm;
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
            if (itemType == 2) {
                this.tv_confirm = (TextView) this.itemView.findViewById(C1020R.id.tv_confirm);
            }
        }

        public void onClick(View v) {
            PatientMainActivity mainActivity = this.context;
            PatientMainActivity.drawerLayout.closeDrawers();
            FragmentTransaction fragmentTransaction = mainActivity.getSupportFragmentManager().beginTransaction();
            Log.e("getAdapterPosition", "" + getAdapterPosition());
            switch (getAdapterPosition()) {
                case 1:
                    fragmentTransaction.replace(C1020R.id.containerView, new MyInfo());
                    fragmentTransaction.commit();
                    return;
                case 2:
                    fragmentTransaction.replace(C1020R.id.containerView, new PatientMyAppointments());
                    fragmentTransaction.commit();
                    return;
                case 3:
                    fragmentTransaction.replace(C1020R.id.containerView, new MyDoctors());
                    fragmentTransaction.commit();
                    return;
                case 4:
                    fragmentTransaction.replace(C1020R.id.containerView, new Notification());
                    fragmentTransaction.commit();
                    return;
                case 5:
                    fragmentTransaction.replace(C1020R.id.containerView, new ContactUs());
                    fragmentTransaction.commit();
                    return;
                case 6:
                    ((PatientMainActivity) this.context).logoutClicked();
                    return;
                default:
                    return;
            }
        }
    }

    public NavigationPatientAdapter(String[] titles, TypedArray icons, Context context, File imageFile) {
        this.titles = titles;
        this.icons = icons;
        this.context = context;
        this.imageFile = imageFile;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService("layout_inflater");
        if (viewType == 1) {
            return new ViewHolder(layoutInflater.inflate(C1020R.layout.drawer_item_layout, parent, false), viewType, this.context);
        }
        if (viewType == 0) {
            return new ViewHolder(layoutInflater.inflate(C1020R.layout.patient_header_layout, parent, false), viewType, this.context);
        }
        if (viewType == 2) {
            return new ViewHolder(layoutInflater.inflate(C1020R.layout.patient_footer_view, parent, false), viewType, this.context);
        }
        return null;
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == 0) {
            holder.doc_name.setText(this.loginDocResponce.getData().getFirstName() + " " + this.loginDocResponce.getData().getLastName());
            holder.speciality.setText(this.loginDocResponce.getData().getAddress());
            Log.e("PIC", "" + this.loginDocResponce.getData().getProfilePic());
            if (this.imageFile != null) {
                Log.e("else part", "called");
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
            holder.img_edit.setOnClickListener(new C11351());
        } else if (position == this.titles.length + 1) {
            holder.tv_confirm.setOnClickListener(new C11362());
        } else if (position != 0) {
            holder.navTitle.setText(this.titles[position - 1]);
            holder.navIcon.setImageResource(this.icons.getResourceId(position - 1, -1));
            if (position == this.titles.length) {
                holder.v_line.setVisibility(8);
            } else {
                holder.v_line.setVisibility(0);
            }
        }
    }

    public int getItemCount() {
        return this.titles.length + 2;
    }

    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        if (position == this.titles.length + 1) {
            return 2;
        }
        return 1;
    }
}
