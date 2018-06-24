package com.trigma.tiktok.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.model.Upcoming;
import com.trigma.tiktok.presenter.MyAppointmentsPresenter;
import com.trigma.tiktok.utils.CircleTransform;
import com.trigma.tiktok.utils.Constants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ApppointmentUpcoming extends BaseExpandableListAdapter {
    private Context _context;
    private HashMap<String, ArrayList<Upcoming>> _listDataChild;
    private ArrayList<String> _listDataHeader;
    private MyAppointmentsPresenter myAppointmentsPresenter;
    private String[] toTimeArr = new String[]{"00:00", "00:15", "00:30", "00:45", "01:00", "01:15", "01:30", "01:45", "02:00", "02:15", "02:30", "02:45", "03:00", "03:15", "03:30", "03:45", "04:00", "04:15", "04:30", "04:45", "05:00", "05:15", "05:30", "05:45", "06:00", "06:15", "06:30", "06:45", "07:00", "07:15", "07:30", "07:45", "08:00", "08:15", "08:30", "08:45", "09:00", "09:15", "09:30", "09:45", "10:00", "10:15", "10:30", "10:45", "11:00", "11:15", "11:30", "11:45", "12:00", "12:15", "12:30", "12:45", "13:00", "13:15", "13:30", "13:45", "14:00", "14:15", "14:30", "14:45", "15:00", "15:15", "15:30", "15:45", "16:00", "16:15", "16:30", "16:45", "17:00", "17:15", "17:30", "17:45", "18:00", "18:15", "18:30", "18:45", "19:00", "19:15", "19:30", "19:45", "20:00", "20:15", "20:30", "20:45", "21:00", "21:15", "21:30", "21:45", "22:00", "22:15", "22:30", "22:45", "23:00", "23:15", "23:30", "23:45"};
    private String[] toTimeArr12Hr = new String[]{"12:00 AM", "12:15 AM", "12:30 AM", "12:45 AM", "01:00 AM", "01:15 AM", "01:30 AM", "01:45 AM", "02:00 AM", "02:15 AM", "02:30 AM", "02:45 AM", "03:00 AM", "03:15 AM", "03:30 AM", "03:45 AM", "04:00 AM", "04:15 AM", "04:30 AM", "04:45 AM", "05:00 AM", "05:15 AM", "05:30 AM", "05:45 AM", "06:00 AM", "06:15 AM", "06:30 AM", "06:45 AM", "07:00 AM", "07:15 AM", "07:30 AM", "07:45 AM", "08:00 AM", "08:15 AM", "08:30 AM", "08:45 AM", "09:00 AM", "09:15 AM", "09:30 AM", "09:45 AM", "10:00 AM", "10:15 AM", "10:30 AM", "10:45 AM", "11:00 AM", "11:15 AM", "11:30 AM", "11:45 AM", "12:00 PM", "12:15 PM", "12:30 PM", "12:45 PM", "01:00 PM", "01:15 PM", "01:30 PM", "01:45 PM", "02:00 PM", "02:15 PM", "02:30 PM", "02:45 PM", "03:00 PM", "03:15 PM", "03:30 PM", "03:45 PM", "04:00 PM", "04:15 PM", "04:30 PM", "04:45 PM", "05:00 PM", "05:15 PM", "05:30 PM", "05:45 PM", "06:00 PM", "06:15 PM", "06:30 PM", "06:45 PM", "07:00 PM", "07:15 PM", "07:30 PM", "07:45 PM", "08:00 PM", "08:15 PM", "08:30 PM", "08:45 PM", "09:00 PM", "09:15 PM", "09:30 PM", "09:45 PM", "10:00 PM", "10:15 PM", "10:30 PM", "10:45 PM", "11:00 PM", "11:15 PM", "11:30 PM", "11:45 PM"};
    private ArrayList<String> toTimeArr12HrList;
    private ArrayList<String> toTimeArrList;

    public ApppointmentUpcoming(Context context, ArrayList<String> listDataHeader, HashMap<String, ArrayList<Upcoming>> listChildData, MyAppointmentsPresenter myAppointmentsPresenter) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.toTimeArrList = new ArrayList(Arrays.asList(this.toTimeArr));
        this.toTimeArr12HrList = new ArrayList(Arrays.asList(this.toTimeArr12Hr));
        this.myAppointmentsPresenter = myAppointmentsPresenter;
    }

    public Object getChild(int groupPosition, int childPosititon) {
        return ((ArrayList) this._listDataChild.get(this._listDataHeader.get(groupPosition))).get(childPosititon);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return (long) childPosition;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Upcoming upcoming = (Upcoming) ((ArrayList) this._listDataChild.get(this._listDataHeader.get(groupPosition))).get(childPosition);
        if (convertView == null) {
            convertView = ((LayoutInflater) this._context.getSystemService("layout_inflater")).inflate(C1020R.layout.payment_child_item, null);
        }
        View empty_view_bottom = convertView.findViewById(C1020R.id.empty_view_bottom);
        TextView tv_name = (TextView) convertView.findViewById(C1020R.id.tv_name);
        TextView tv_age = (TextView) convertView.findViewById(C1020R.id.tv_age);
        TextView tv_time = (TextView) convertView.findViewById(C1020R.id.tv_time);
        TextView tv_status = (TextView) convertView.findViewById(C1020R.id.tv_status);
        ImageView img_user = (ImageView) convertView.findViewById(C1020R.id.img_user);
        tv_name.setText(upcoming.getPatientId().getAddress());
        tv_status.setVisibility(8);
        tv_name.setText(upcoming.getPatientId().getFirstName() + " " + upcoming.getPatientId().getLastName());
        tv_age.setText(upcoming.getPatientId().getGender() + "- " + upcoming.getAge());
        tv_time.setText((CharSequence) this.toTimeArr12HrList.get(this.toTimeArrList.indexOf(upcoming.getFrom())));
        LinearLayout lin_parent = (LinearLayout) convertView.findViewById(C1020R.id.lin_parent);
        View line = convertView.findViewById(C1020R.id.line);
        Picasso.with(this._context).load(Constants.HTTP + upcoming.getProfile()).transform(new CircleTransform()).placeholder((int) C1020R.drawable.profile_circle).error((int) C1020R.drawable.profile_circle).into(img_user);
        if (isLastChild) {
            line.setVisibility(8);
            empty_view_bottom.setVisibility(0);
            lin_parent.setBackgroundResource(C1020R.drawable.left_right_bottom);
        } else {
            line.setVisibility(0);
            empty_view_bottom.setVisibility(8);
            lin_parent.setBackgroundResource(C1020R.drawable.left_right_border);
        }
        lin_parent.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ApppointmentUpcoming.this.myAppointmentsPresenter.gotoMakeVideoCall(upcoming);
            }
        });
        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return ((ArrayList) this._listDataChild.get(this._listDataHeader.get(groupPosition))).size();
    }

    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    public long getGroupId(int groupPosition) {
        return (long) groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            convertView = ((LayoutInflater) this._context.getSystemService("layout_inflater")).inflate(C1020R.layout.my_payments_header, null);
        }
        TextView tv_view = (TextView) convertView.findViewById(C1020R.id.tv_view);
        TextView tv_view2 = (TextView) convertView.findViewById(C1020R.id.tv_view2);
        RelativeLayout r_parent = (RelativeLayout) convertView.findViewById(C1020R.id.r_parent);
        ImageView img_show_pwd = (ImageView) convertView.findViewById(C1020R.id.img_show_pwd);
        TextView tv_date = (TextView) convertView.findViewById(C1020R.id.tv_date);
        if (groupPosition == 0) {
            tv_view2.setVisibility(0);
        } else {
            tv_view2.setVisibility(8);
        }
        if (isExpanded) {
            tv_view.setVisibility(8);
            r_parent.setBackgroundResource(C1020R.drawable.payement_head_collapse);
            img_show_pwd.setImageResource(C1020R.drawable.arrow_up);
        } else {
            r_parent.setBackgroundResource(C1020R.drawable.normal_blue);
            img_show_pwd.setImageResource(C1020R.drawable.down_arrow);
            tv_view.setVisibility(0);
            if (groupPosition == getGroupCount() - 1) {
                tv_view.setVisibility(8);
            }
        }
        tv_date.setText(headerTitle);
        return convertView;
    }

    public boolean hasStableIds() {
        return false;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
