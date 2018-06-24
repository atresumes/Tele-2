package com.trigma.tiktok.adapter;

import android.support.v4.content.ContextCompat;
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
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackWithButtonsInterface;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.Constants;
import com.trigma.tiktok.utils.DialogPopUps;
import com.trigma.tiktok.utils.SharedPreff;
import java.util.ArrayList;
import java.util.Iterator;

public class PendingPatientAdapter extends Adapter<MyViewHolder> implements Filterable {
    String CreateId = "";
    String CreateName = "";
    String CreateUserType = "";
    String Dr_id = "";
    MyPatientsPresenter myPatientsPresenter;
    public ArrayList<PatientPendingData> pendingList = new ArrayList();
    private UserFilter userFilter;
    MyPatients view;

    public class MyViewHolder extends ViewHolder implements OnClickListener {
        public CardView card_view;
        public View empty_view;
        public View empty_view2;
        public ImageView img_user;
        public LinearLayout lin_parent;
        public TextView tv_age;
        public TextView tv_confirm;
        public TextView tv_delete;
        public TextView tv_name;

        public MyViewHolder(View view) {
            super(view);
            this.img_user = (ImageView) view.findViewById(C1020R.id.img_user);
            this.tv_name = (TextView) view.findViewById(C1020R.id.tv_name);
            this.tv_age = (TextView) view.findViewById(C1020R.id.tv_age);
            this.tv_confirm = (TextView) view.findViewById(C1020R.id.tv_confirm);
            this.tv_delete = (TextView) view.findViewById(C1020R.id.tv_delete);
            this.lin_parent = (LinearLayout) view.findViewById(C1020R.id.lin_parent);
            this.card_view = (CardView) view.findViewById(C1020R.id.card_view);
            this.empty_view = view.findViewById(C1020R.id.empty_view);
            this.empty_view2 = view.findViewById(C1020R.id.empty_view2);
        }

        public void onClick(View v) {
        }
    }

    private class UserFilter extends Filter {
        private final PendingPatientAdapter adapter;
        private final ArrayList<PatientPendingData> filteredList;
        private final ArrayList<PatientPendingData> originalList;

        private UserFilter(PendingPatientAdapter adapter, ArrayList<PatientPendingData> originalList) {
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
            if (this.adapter.pendingList != null) {
                this.adapter.pendingList.clear();
            }
            this.adapter.pendingList.addAll((ArrayList) results.values);
            this.adapter.notifyDataSetChanged();
            if (this.adapter.pendingList.size() <= 0) {
                PendingPatientAdapter.this.view.noSearchFound();
            }
        }
    }

    public PendingPatientAdapter(MyPatientsPresenter myPatientsPresenter, MyPatients view) {
        this.myPatientsPresenter = myPatientsPresenter;
        this.view = view;
        if (SharedPreff.getStaffLoginResponse() != null) {
            this.CreateId = SharedPreff.getStaffLoginResponse().getData().get_id();
            this.CreateName = SharedPreff.getStaffLoginResponse().getData().getFirstName() + " " + SharedPreff.getStaffLoginResponse().getData().getLastName();
            this.CreateUserType = "" + SharedPreff.getStaffLoginResponse().getData().getUserType();
            this.Dr_id = SharedPreff.getLoginResponce().getData().get_id();
            return;
        }
        this.CreateId = SharedPreff.getLoginResponce().getData().get_id();
        this.CreateName = SharedPreff.getLoginResponce().getData().getFirstName() + " " + SharedPreff.getLoginResponce().getData().getLastName();
        this.CreateUserType = "" + SharedPreff.getLoginResponce().getData().getUserType();
        this.Dr_id = SharedPreff.getLoginResponce().getData().get_id();
    }

    public void addingList(ArrayList<PatientPendingData> pendingList) {
        this.pendingList = pendingList;
        notifyDataSetChanged();
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(C1020R.layout.patient_pending, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (position == this.pendingList.size() - 1) {
            holder.empty_view2.setVisibility(0);
        } else {
            holder.empty_view2.setVisibility(8);
        }
        final PatientPendingData patientPendingData = (PatientPendingData) this.pendingList.get(position);
        holder.tv_name.setText(patientPendingData.getName());
        holder.tv_age.setText(CommonUtils.capWordCase(patientPendingData.getGender()) + " - " + patientPendingData.getDOB());
        if (position % 2 == 0) {
            holder.lin_parent.setBackgroundColor(ContextCompat.getColor(this.view.getActivity(), C1020R.color.white));
        } else {
            holder.lin_parent.setBackgroundColor(ContextCompat.getColor(this.view.getActivity(), C1020R.color.light_white));
        }
        Picasso.with(this.view.getActivity()).load(Constants.HTTP + patientPendingData.getProfilePic()).transform(new CircleTransform()).placeholder((int) C1020R.drawable.profile_circle).error((int) C1020R.drawable.profile_circle).into(holder.img_user);
        holder.tv_delete.setOnClickListener(new OnClickListener() {

            class C11201 implements AlertCallBackWithButtonsInterface {
                C11201() {
                }

                public void positiveClick() {
                    String email = "";
                    String id = "";
                    int pos = position;
                    PendingPatientAdapter.this.myPatientsPresenter.deleteRequest(patientPendingData.getEmail(), patientPendingData.getAcceptReject_Id(), pos, patientPendingData.getDeviceToken(), patientPendingData.getName(), PendingPatientAdapter.this.CreateId, PendingPatientAdapter.this.CreateName, PendingPatientAdapter.this.CreateUserType, PendingPatientAdapter.this.Dr_id);
                }

                public void neutralClick() {
                }

                public void negativeClick() {
                }
            }

            public void onClick(View v) {
                DialogPopUps.showAlertWithButtons(PendingPatientAdapter.this.view.getActivity(), PendingPatientAdapter.this.view.getActivity().getResources().getString(C1020R.string.alert), PendingPatientAdapter.this.view.getActivity().getResources().getString(C1020R.string.are_you_sure_you_want_to_decline_the_request), PendingPatientAdapter.this.view.getActivity().getResources().getString(C1020R.string.yes_dialog), PendingPatientAdapter.this.view.getActivity().getResources().getString(C1020R.string.no_dialog), "", false, true, new C11201());
            }
        });
        holder.tv_confirm.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String email = "";
                String id = "";
                int pos = position;
                PendingPatientAdapter.this.myPatientsPresenter.acceptRequest(patientPendingData.getEmail(), patientPendingData.getAcceptReject_Id(), pos, patientPendingData.getDeviceToken(), patientPendingData.getName(), PendingPatientAdapter.this.CreateId, PendingPatientAdapter.this.CreateName, PendingPatientAdapter.this.CreateUserType, PendingPatientAdapter.this.Dr_id);
            }
        });
        holder.card_view.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SharedPreff.addingDocPatientDetail(patientPendingData);
                PendingPatientAdapter.this.view.gotoDetailPage(0);
            }
        });
    }

    public int getItemCount() {
        return this.pendingList.size();
    }

    public void deleted(int pos) {
        if (this.pendingList.size() > 0) {
            this.pendingList.remove(pos);
            notifyDataSetChanged();
        }
    }

    public void accepted(int pos) {
        if (this.pendingList.size() > 0) {
            this.pendingList.remove(pos);
            notifyDataSetChanged();
        }
    }

    public Filter getFilter() {
        if (this.userFilter == null) {
            this.userFilter = new UserFilter(this, this.pendingList);
        }
        return this.userFilter;
    }
}
