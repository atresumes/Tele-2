package com.trigma.tiktok.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filter.FilterResults;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.fragments.MyPatients;
import com.trigma.tiktok.model.PatientPendingData;
import com.trigma.tiktok.presenter.MyPatientsPresenter;
import com.trigma.tiktok.utils.CircleTransform;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.SharedPreff;
import java.util.ArrayList;
import java.util.Iterator;

public class ActivePatientAdapter extends Adapter<MyViewHolder> implements Filterable {
    public ArrayList<PatientPendingData> activeList = new ArrayList();
    MyPatientsPresenter myPatientsPresenter;
    private UserFilter userFilter;
    MyPatients view;

    public class MyViewHolder extends ViewHolder implements OnClickListener {
        public CardView card_view;
        public View empty_view;
        public View empty_view2;
        public ImageView img_chat;
        public ImageView img_user;
        public LinearLayout lin_parent;
        public TextView tv_address;
        public TextView tv_age;
        public TextView tv_gender;
        public TextView tv_name;

        public MyViewHolder(View view) {
            super(view);
            this.img_user = (ImageView) view.findViewById(C1020R.id.img_user);
            this.img_chat = (ImageView) view.findViewById(C1020R.id.img_chat);
            this.tv_name = (TextView) view.findViewById(C1020R.id.tv_name);
            this.tv_age = (TextView) view.findViewById(C1020R.id.tv_age);
            this.tv_gender = (TextView) view.findViewById(C1020R.id.tv_gender);
            this.tv_address = (TextView) view.findViewById(C1020R.id.tv_address);
            this.lin_parent = (LinearLayout) view.findViewById(C1020R.id.lin_parent);
            this.card_view = (CardView) view.findViewById(C1020R.id.card_view);
            this.empty_view = view.findViewById(C1020R.id.empty_view);
            this.empty_view2 = view.findViewById(C1020R.id.empty_view2);
        }

        public void onClick(View v) {
        }
    }

    private class UserFilter extends Filter {
        private final ActivePatientAdapter adapter;
        private final ArrayList<PatientPendingData> filteredList;
        private final ArrayList<PatientPendingData> originalList;

        private UserFilter(ActivePatientAdapter adapter, ArrayList<PatientPendingData> originalList) {
            this.adapter = adapter;
            this.originalList = new ArrayList(originalList);
            this.filteredList = new ArrayList();
            Log.e("originalList", originalList.size() + "");
            Log.e("filteredList", this.filteredList.size() + "");
        }

        protected FilterResults performFiltering(CharSequence constraint) {
            this.filteredList.clear();
            FilterResults results = new FilterResults();
            Log.e("constraint", constraint + "");
            if (constraint.length() == 0) {
                this.filteredList.addAll(this.originalList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                Iterator it = this.originalList.iterator();
                while (it.hasNext()) {
                    PatientPendingData user = (PatientPendingData) it.next();
                    if (user.getName().trim().toLowerCase().contains(filterPattern)) {
                        this.filteredList.add(user);
                    }
                }
            }
            results.values = this.filteredList;
            results.count = this.filteredList.size();
            return results;
        }

        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (this.adapter.activeList != null) {
                this.adapter.activeList.clear();
            }
            this.adapter.activeList.addAll((ArrayList) results.values);
            this.adapter.notifyDataSetChanged();
            if (this.adapter.activeList.size() <= 0) {
                ActivePatientAdapter.this.view.noSearchFound();
            }
        }
    }

    public ActivePatientAdapter(MyPatientsPresenter myPatientsPresenter, MyPatients view) {
        this.myPatientsPresenter = myPatientsPresenter;
        this.view = view;
    }

    public void addingList(ArrayList<PatientPendingData> activeList) {
        this.activeList = activeList;
        notifyDataSetChanged();
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(C1020R.layout.patients_child_item, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (position == this.activeList.size() - 1) {
            holder.empty_view2.setVisibility(0);
        } else {
            holder.empty_view2.setVisibility(8);
        }
        final PatientPendingData patientPendingData = (PatientPendingData) this.activeList.get(position);
        holder.tv_name.setText(patientPendingData.getName());
        holder.tv_age.setText(patientPendingData.getDOB());
        holder.tv_gender.setText(patientPendingData.getGender());
        holder.tv_address.setText(patientPendingData.getAddress());
        Picasso.with(this.view.getActivity()).load(Constants.HTTP + patientPendingData.getProfilePic()).transform(new CircleTransform()).placeholder((int) C1020R.drawable.profile_circle).error((int) C1020R.drawable.profile_circle).into(holder.img_user);
        holder.card_view.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SharedPreff.addingDocPatientDetail(patientPendingData);
                ActivePatientAdapter.this.view.gotoDetailPage(1);
            }
        });
        if (SharedPreff.getStaffLoginResponse() != null) {
            holder.img_chat.setVisibility(0);
        } else {
            holder.img_chat.setVisibility(0);
        }
        holder.img_chat.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ActivePatientAdapter.this.myPatientsPresenter.chatNowApi(patientPendingData.getPatient_id(), patientPendingData);
            }
        });
    }

    public int getItemCount() {
        return this.activeList.size();
    }

    public Filter getFilter() {
        if (this.userFilter == null) {
            this.userFilter = new UserFilter(this, this.activeList);
        }
        return this.userFilter;
    }
}
