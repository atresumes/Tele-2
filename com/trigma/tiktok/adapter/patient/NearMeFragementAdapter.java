package com.trigma.tiktok.adapter.patient;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.fragments.patient.NearMeFragement;
import com.trigma.tiktok.model.DrSearchNameObject;
import com.trigma.tiktok.presenter.patient.NearMePresenter;
import com.trigma.tiktok.utils.CircleTransform;
import com.trigma.tiktok.utils.Constants;
import java.util.ArrayList;

public class NearMeFragementAdapter extends Adapter<MyViewHolder> {
    private ArrayList<DrSearchNameObject> dataList = new ArrayList();
    private NearMePresenter nameZipPresenter;
    NearMeFragement view;

    public class MyViewHolder extends ViewHolder implements OnClickListener {
        public CardView card_view;
        public View empty_view;
        public View empty_view2;
        public ImageView img_user;
        public TextView tv_address;
        public TextView tv_mile;
        public TextView tv_name;
        public TextView tv_speciality;

        public MyViewHolder(View view) {
            super(view);
            this.img_user = (ImageView) view.findViewById(C1020R.id.img_user);
            this.tv_name = (TextView) view.findViewById(C1020R.id.tv_name);
            this.tv_speciality = (TextView) view.findViewById(C1020R.id.tv_speciality);
            this.tv_address = (TextView) view.findViewById(C1020R.id.tv_address);
            this.tv_mile = (TextView) view.findViewById(C1020R.id.tv_mile);
            this.empty_view = view.findViewById(C1020R.id.empty_view);
            this.empty_view2 = view.findViewById(C1020R.id.empty_view2);
            this.card_view = (CardView) view.findViewById(C1020R.id.card_view);
        }

        public void onClick(View v) {
        }
    }

    public NearMeFragementAdapter(NearMeFragement view, NearMePresenter nameZipPresenter) {
        this.view = view;
        this.nameZipPresenter = nameZipPresenter;
    }

    public void addingList(ArrayList<DrSearchNameObject> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(C1020R.layout.search_list_item, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        final DrSearchNameObject drSearchNameObject = (DrSearchNameObject) this.dataList.get(position);
        if (position == this.dataList.size() - 1) {
            holder.empty_view2.setVisibility(0);
        } else {
            holder.empty_view2.setVisibility(8);
        }
        holder.tv_name.setText(this.view.getActivity().getResources().getString(C1020R.string.dr) + " " + drSearchNameObject.getName());
        holder.tv_address.setText(drSearchNameObject.getAddress());
        holder.tv_speciality.setText(drSearchNameObject.getSpeciality());
        holder.tv_mile.setText(drSearchNameObject.getDistance() + "\n" + this.view.getActivity().getResources().getString(C1020R.string.miles));
        Picasso.with(this.view.getActivity()).load(Constants.HTTP + drSearchNameObject.getProfilePic()).transform(new CircleTransform()).placeholder((int) C1020R.drawable.profile_circle).error((int) C1020R.drawable.profile_circle).into(holder.img_user);
        holder.card_view.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                NearMeFragementAdapter.this.nameZipPresenter.goToDetail(drSearchNameObject);
            }
        });
    }

    public int getItemCount() {
        return this.dataList.size();
    }
}
