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
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.gms.maps.model.LatLng;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.activity.patient.SelectPharmacy;
import com.trigma.tiktok.model.SelectPharmacyObject;
import com.trigma.tiktok.presenter.patient.SelectPharmacyPresenter;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.SharedPreff;
import java.util.ArrayList;

public class SelectPharmacyAdapter extends Adapter<MyViewHolder> {
    private ArrayList<SelectPharmacyObject> dataList = new ArrayList();
    private SelectPharmacyPresenter selectPharmacyPresenter;
    SelectPharmacy view;

    public class MyViewHolder extends ViewHolder implements OnClickListener {
        public CardView card_view;
        public View empty_view;
        public View empty_view2;
        public ImageView img_icon;
        public RelativeLayout lin_parent;
        public TextView tv_address;
        public TextView tv_address_2;
        public TextView tv_address_3;
        public TextView tv_mile;

        public MyViewHolder(View view) {
            super(view);
            this.img_icon = (ImageView) view.findViewById(C1020R.id.img_icon);
            this.tv_address = (TextView) view.findViewById(C1020R.id.tv_address);
            this.tv_address_2 = (TextView) view.findViewById(C1020R.id.tv_address_2);
            this.tv_address_3 = (TextView) view.findViewById(C1020R.id.tv_address_3);
            this.tv_mile = (TextView) view.findViewById(C1020R.id.tv_mile);
            this.empty_view = view.findViewById(C1020R.id.empty_view);
            this.empty_view2 = view.findViewById(C1020R.id.empty_view2);
            this.card_view = (CardView) view.findViewById(C1020R.id.card_view);
            this.lin_parent = (RelativeLayout) view.findViewById(C1020R.id.lin_parent);
        }

        public void onClick(View v) {
        }
    }

    public SelectPharmacyAdapter(SelectPharmacy view, SelectPharmacyPresenter selectPharmacyPresenter) {
        this.view = view;
        this.selectPharmacyPresenter = selectPharmacyPresenter;
    }

    public void addingList(ArrayList<SelectPharmacyObject> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(C1020R.layout.select_pharmacy_list_item, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        final SelectPharmacyObject selectPharmacyObject = (SelectPharmacyObject) this.dataList.get(position);
        if (position == this.dataList.size() - 1) {
            holder.empty_view2.setVisibility(0);
        } else {
            holder.empty_view2.setVisibility(8);
        }
        if (position == 0) {
            holder.empty_view.setVisibility(8);
        } else {
            holder.empty_view.setVisibility(0);
        }
        if (position % 2 == 0) {
            holder.lin_parent.setBackgroundColor(ContextCompat.getColor(this.view, C1020R.color.white));
        } else {
            holder.lin_parent.setBackgroundColor(ContextCompat.getColor(this.view, C1020R.color.light_white));
        }
        holder.tv_address.setText(selectPharmacyObject.getData().getStoreName());
        holder.tv_address_2.setText(selectPharmacyObject.getData().getAddress());
        holder.tv_address_3.setText(selectPharmacyObject.getData().getCity() + "," + selectPharmacyObject.getData().getState());
        try {
            holder.tv_mile.setText(CommonUtils.getDistance(new LatLng(Double.parseDouble(SharedPreff.getLat()), Double.parseDouble(SharedPreff.getLng())), new LatLng(Double.parseDouble(selectPharmacyObject.getLat()), Double.parseDouble(selectPharmacyObject.getLong()))));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        holder.card_view.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SelectPharmacyAdapter.this.selectPharmacyPresenter.gotoPharmacyDetail(selectPharmacyObject);
            }
        });
    }

    public int getItemCount() {
        return this.dataList.size();
    }
}
