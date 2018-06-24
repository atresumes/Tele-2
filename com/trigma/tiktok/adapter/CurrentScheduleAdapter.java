package com.trigma.tiktok.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.fragments.CurrentSchedule;
import com.trigma.tiktok.model.BookingPojo;
import com.trigma.tiktok.model.SchedulePojo;
import com.trigma.tiktok.model.TimeSlots;
import com.trigma.tiktok.presenter.CurrentSchedulePresenter;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackWithButtonsInterface;
import com.trigma.tiktok.utils.DialogPopUps;
import java.util.ArrayList;
import java.util.Arrays;

public class CurrentScheduleAdapter extends Adapter<MyViewHolder> {
    private String DrScheduleId;
    private String Dr_id;
    private String From;
    private String ScheduleId;
    private String To;
    public ArrayList<BookingPojo> bookingDetailToSend = new ArrayList();
    private CurrentSchedulePresenter currentSchedulePresenter;
    private String schedule;
    public ArrayList<SchedulePojo> scheduleList = new ArrayList();
    private String[] toTimeArr = new String[]{"00:00", "00:15", "00:30", "00:45", "01:00", "01:15", "01:30", "01:45", "02:00", "02:15", "02:30", "02:45", "03:00", "03:15", "03:30", "03:45", "04:00", "04:15", "04:30", "04:45", "05:00", "05:15", "05:30", "05:45", "06:00", "06:15", "06:30", "06:45", "07:00", "07:15", "07:30", "07:45", "08:00", "08:15", "08:30", "08:45", "09:00", "09:15", "09:30", "09:45", "10:00", "10:15", "10:30", "10:45", "11:00", "11:15", "11:30", "11:45", "12:00", "12:15", "12:30", "12:45", "13:00", "13:15", "13:30", "13:45", "14:00", "14:15", "14:30", "14:45", "15:00", "15:15", "15:30", "15:45", "16:00", "16:15", "16:30", "16:45", "17:00", "17:15", "17:30", "17:45", "18:00", "18:15", "18:30", "18:45", "19:00", "19:15", "19:30", "19:45", "20:00", "20:15", "20:30", "20:45", "21:00", "21:15", "21:30", "21:45", "22:00", "22:15", "22:30", "22:45", "23:00", "23:15", "23:30", "23:45"};
    private String[] toTimeArr12Hr = new String[]{"12:00 AM", "12:15 AM", "12:30 AM", "12:45 AM", "01:00 AM", "01:15 AM", "01:30 AM", "01:45 AM", "02:00 AM", "02:15 AM", "02:30 AM", "02:45 AM", "03:00 AM", "03:15 AM", "03:30 AM", "03:45 AM", "04:00 AM", "04:15 AM", "04:30 AM", "04:45 AM", "05:00 AM", "05:15 AM", "05:30 AM", "05:45 AM", "06:00 AM", "06:15 AM", "06:30 AM", "06:45 AM", "07:00 AM", "07:15 AM", "07:30 AM", "07:45 AM", "08:00 AM", "08:15 AM", "08:30 AM", "08:45 AM", "09:00 AM", "09:15 AM", "09:30 AM", "09:45 AM", "10:00 AM", "10:15 AM", "10:30 AM", "10:45 AM", "11:00 AM", "11:15 AM", "11:30 AM", "11:45 AM", "12:00 PM", "12:15 PM", "12:30 PM", "12:45 PM", "01:00 PM", "01:15 PM", "01:30 PM", "01:45 PM", "02:00 PM", "02:15 PM", "02:30 PM", "02:45 PM", "03:00 PM", "03:15 PM", "03:30 PM", "03:45 PM", "04:00 PM", "04:15 PM", "04:30 PM", "04:45 PM", "05:00 PM", "05:15 PM", "05:30 PM", "05:45 PM", "06:00 PM", "06:15 PM", "06:30 PM", "06:45 PM", "07:00 PM", "07:15 PM", "07:30 PM", "07:45 PM", "08:00 PM", "08:15 PM", "08:30 PM", "08:45 PM", "09:00 PM", "09:15 PM", "09:30 PM", "09:45 PM", "10:00 PM", "10:15 PM", "10:30 PM", "10:45 PM", "11:00 PM", "11:15 PM", "11:30 PM", "11:45 PM"};
    private ArrayList<String> toTimeArr12HrList = new ArrayList(Arrays.asList(this.toTimeArr12Hr));
    private ArrayList<String> toTimeArrList = new ArrayList(Arrays.asList(this.toTimeArr));
    private CurrentSchedule view;

    public class MyViewHolder extends ViewHolder implements OnClickListener {
        public View empty_view;
        public CardView parent_view;
        public TextView tv_delete;
        public TextView tv_minute;
        public TextView tv_time;

        public MyViewHolder(View view) {
            super(view);
            this.tv_time = (TextView) view.findViewById(C1020R.id.tv_time);
            this.tv_minute = (TextView) view.findViewById(C1020R.id.tv_minute);
            this.tv_delete = (TextView) view.findViewById(C1020R.id.tv_delete);
            this.parent_view = (CardView) view.findViewById(C1020R.id.parent_view);
            this.empty_view = view.findViewById(C1020R.id.empty_view);
        }

        public void onClick(View v) {
        }
    }

    public CurrentScheduleAdapter(CurrentSchedulePresenter currentSchedulePresenter, CurrentSchedule view) {
        this.currentSchedulePresenter = currentSchedulePresenter;
        this.view = view;
    }

    public void addingList(ArrayList<SchedulePojo> scheduleList, ArrayList<BookingPojo> arrayList, String Dr_id, String DrScheduleId, String schedule) {
        this.scheduleList = scheduleList;
        this.Dr_id = Dr_id;
        this.DrScheduleId = DrScheduleId;
        this.schedule = schedule;
        notifyDataSetChanged();
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(C1020R.layout.current_schedule_item, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (position > 0) {
            holder.empty_view.setVisibility(8);
        } else {
            holder.empty_view.setVisibility(0);
        }
        try {
            final SchedulePojo schedulePojo = (SchedulePojo) this.scheduleList.get(position);
            TimeSlots timeSlots = schedulePojo.getTime();
            holder.tv_time.setText(((String) this.toTimeArr12HrList.get(this.toTimeArrList.indexOf(timeSlots.getFrom()))) + "-" + ((String) this.toTimeArr12HrList.get(this.toTimeArrList.indexOf(timeSlots.getTo()))));
            holder.tv_minute.setText(schedulePojo.getMeetingSlot());
            holder.tv_delete.setOnClickListener(new OnClickListener() {

                class C11101 implements AlertCallBackWithButtonsInterface {
                    C11101() {
                    }

                    public void positiveClick() {
                        CurrentScheduleAdapter.this.ScheduleId = schedulePojo.get_id();
                        CurrentScheduleAdapter.this.From = schedulePojo.getTime().getFrom();
                        CurrentScheduleAdapter.this.To = schedulePojo.getTime().getTo();
                        CurrentScheduleAdapter.this.currentSchedulePresenter.deleteDataFromMain(position, (SchedulePojo) CurrentScheduleAdapter.this.scheduleList.get(position), CurrentScheduleAdapter.this.Dr_id, CurrentScheduleAdapter.this.DrScheduleId, CurrentScheduleAdapter.this.ScheduleId, CurrentScheduleAdapter.this.From, CurrentScheduleAdapter.this.To, CurrentScheduleAdapter.this.schedule);
                    }

                    public void neutralClick() {
                    }

                    public void negativeClick() {
                    }
                }

                public void onClick(View v) {
                    DialogPopUps.showAlertWithButtons(CurrentScheduleAdapter.this.view.getActivity(), CurrentScheduleAdapter.this.view.getActivity().getResources().getString(C1020R.string.alert), CurrentScheduleAdapter.this.view.getActivity().getResources().getString(C1020R.string.are_you_sure_you_want_to_delete), CurrentScheduleAdapter.this.view.getActivity().getResources().getString(C1020R.string.yes_dialog), CurrentScheduleAdapter.this.view.getActivity().getResources().getString(C1020R.string.no_dialog), "", false, true, new C11101());
                }
            });
            holder.parent_view.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    CurrentScheduleAdapter.this.ScheduleId = schedulePojo.get_id();
                    CurrentScheduleAdapter.this.From = schedulePojo.getTime().getFrom();
                    CurrentScheduleAdapter.this.To = schedulePojo.getTime().getTo();
                    CurrentScheduleAdapter.this.view.gotoDetailScreen(CurrentScheduleAdapter.this.Dr_id, CurrentScheduleAdapter.this.DrScheduleId, CurrentScheduleAdapter.this.ScheduleId, CurrentScheduleAdapter.this.From, CurrentScheduleAdapter.this.To);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getItemCount() {
        return this.scheduleList.size();
    }

    public void deleteItem(int pos) {
        this.scheduleList.remove(pos);
        notifyDataSetChanged();
    }
}
