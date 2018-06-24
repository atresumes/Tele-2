package com.trigma.tiktok.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.activity.ScheduleDetail;
import com.trigma.tiktok.model.PateintDataPojoMain;
import com.trigma.tiktok.utils.CircleTransform;
import com.trigma.tiktok.utils.Constants;
import java.util.ArrayList;
import java.util.Arrays;

public class ScheduleDetailAdapter extends Adapter<MyViewHolder> {
    public ArrayList<PateintDataPojoMain> patient = new ArrayList();
    private String[] toTimeArr = new String[]{"00:00", "00:15", "00:30", "00:45", "01:00", "01:15", "01:30", "01:45", "02:00", "02:15", "02:30", "02:45", "03:00", "03:15", "03:30", "03:45", "04:00", "04:15", "04:30", "04:45", "05:00", "05:15", "05:30", "05:45", "06:00", "06:15", "06:30", "06:45", "07:00", "07:15", "07:30", "07:45", "08:00", "08:15", "08:30", "08:45", "09:00", "09:15", "09:30", "09:45", "10:00", "10:15", "10:30", "10:45", "11:00", "11:15", "11:30", "11:45", "12:00", "12:15", "12:30", "12:45", "13:00", "13:15", "13:30", "13:45", "14:00", "14:15", "14:30", "14:45", "15:00", "15:15", "15:30", "15:45", "16:00", "16:15", "16:30", "16:45", "17:00", "17:15", "17:30", "17:45", "18:00", "18:15", "18:30", "18:45", "19:00", "19:15", "19:30", "19:45", "20:00", "20:15", "20:30", "20:45", "21:00", "21:15", "21:30", "21:45", "22:00", "22:15", "22:30", "22:45", "23:00", "23:15", "23:30", "23:45"};
    private String[] toTimeArr12Hr = new String[]{"12:00 AM", "12:15 AM", "12:30 AM", "12:45 AM", "01:00 AM", "01:15 AM", "01:30 AM", "01:45 AM", "02:00 AM", "02:15 AM", "02:30 AM", "02:45 AM", "03:00 AM", "03:15 AM", "03:30 AM", "03:45 AM", "04:00 AM", "04:15 AM", "04:30 AM", "04:45 AM", "05:00 AM", "05:15 AM", "05:30 AM", "05:45 AM", "06:00 AM", "06:15 AM", "06:30 AM", "06:45 AM", "07:00 AM", "07:15 AM", "07:30 AM", "07:45 AM", "08:00 AM", "08:15 AM", "08:30 AM", "08:45 AM", "09:00 AM", "09:15 AM", "09:30 AM", "09:45 AM", "10:00 AM", "10:15 AM", "10:30 AM", "10:45 AM", "11:00 AM", "11:15 AM", "11:30 AM", "11:45 AM", "12:00 PM", "12:15 PM", "12:30 PM", "12:45 PM", "01:00 PM", "01:15 PM", "01:30 PM", "01:45 PM", "02:00 PM", "02:15 PM", "02:30 PM", "02:45 PM", "03:00 PM", "03:15 PM", "03:30 PM", "03:45 PM", "04:00 PM", "04:15 PM", "04:30 PM", "04:45 PM", "05:00 PM", "05:15 PM", "05:30 PM", "05:45 PM", "06:00 PM", "06:15 PM", "06:30 PM", "06:45 PM", "07:00 PM", "07:15 PM", "07:30 PM", "07:45 PM", "08:00 PM", "08:15 PM", "08:30 PM", "08:45 PM", "09:00 PM", "09:15 PM", "09:30 PM", "09:45 PM", "10:00 PM", "10:15 PM", "10:30 PM", "10:45 PM", "11:00 PM", "11:15 PM", "11:30 PM", "11:45 PM"};
    private ArrayList<String> toTimeArr12HrList;
    private ArrayList<String> toTimeArrList;
    private ScheduleDetail view;

    public class MyViewHolder extends ViewHolder {
        public View empty_view;
        public ImageView img_user;
        public CardView parent_view;
        public TextView tv_age;
        public TextView tv_name;
        public TextView tv_time_2;

        public MyViewHolder(View view) {
            super(view);
            this.tv_name = (TextView) view.findViewById(C1020R.id.tv_name);
            this.tv_age = (TextView) view.findViewById(C1020R.id.tv_age);
            this.tv_time_2 = (TextView) view.findViewById(C1020R.id.tv_time_2);
            this.empty_view = view.findViewById(C1020R.id.empty_view);
            this.img_user = (ImageView) view.findViewById(C1020R.id.img_user);
        }
    }

    public ScheduleDetailAdapter(ScheduleDetail view) {
        this.view = view;
        this.toTimeArrList = new ArrayList(Arrays.asList(this.toTimeArr));
        this.toTimeArr12HrList = new ArrayList(Arrays.asList(this.toTimeArr12Hr));
    }

    public void addingList(ArrayList<PateintDataPojoMain> patient) {
        this.patient = patient;
        notifyDataSetChanged();
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(C1020R.layout.schedule_child_item, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (position > 0) {
            holder.empty_view.setVisibility(8);
        } else {
            holder.empty_view.setVisibility(0);
        }
        Object pateintDataPojo = (PateintDataPojoMain) this.patient.get(position);
        holder.tv_name.setText(pateintDataPojo.getData().getPatientName());
        Log.e("pateintDataPojo", "" + new Gson().toJson(pateintDataPojo));
        try {
            holder.tv_age.setText(getMin(gettingDiff(pateintDataPojo.getData().getFrom(), pateintDataPojo.getData().getTo())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            holder.tv_time_2.setText((CharSequence) this.toTimeArr12HrList.get(this.toTimeArrList.indexOf(pateintDataPojo.getData().getFrom())));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        Picasso.with(this.view).load(Constants.HTTP + pateintDataPojo.getProfile()).transform(new CircleTransform()).placeholder((int) C1020R.drawable.profile_circle).error((int) C1020R.drawable.profile_circle).into(holder.img_user);
    }

    private int gettingDiff(String from, String to) {
        try {
            return this.toTimeArrList.indexOf(to) - this.toTimeArrList.indexOf(from);
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    public String getMin(int diff) {
        Log.e("DIFFF", "" + diff);
        String result = "";
        if (diff == 1) {
            return "15 Min";
        }
        if (diff == 2) {
            return "30 Min";
        }
        if (diff == 4) {
            return "60 Min";
        }
        if (diff == 6) {
            return "90 Min";
        }
        return result;
    }

    public int getItemCount() {
        return this.patient.size();
    }
}
