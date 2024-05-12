package com.dmv.idopontfoglalas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.ArrayList;

public class AppointmentItemAdapter extends RecyclerView.Adapter<AppointmentItemAdapter.ViewHolder> {
    private ArrayList<AppointmentItem> mAppointmentItemsData;
    private ArrayList<AppointmentItem> mAppointmentItemsDataAll;
    private Context mContext;
    private int lastPosition = -1;

    AppointmentItemAdapter(Context context, ArrayList<AppointmentItem> itemsData) {
        this.mAppointmentItemsData = itemsData;
        this.mAppointmentItemsDataAll = itemsData;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(AppointmentItemAdapter.ViewHolder holder, int position) {
        AppointmentItem currentItem = mAppointmentItemsData.get(position);

        holder.bindTo(currentItem);

        Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);
    }


    @Override
    public int getItemCount() {
        return mAppointmentItemsData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitleText;
        private TextView mDateText;
        public ViewHolder(View itemView) {
            super(itemView);

            mTitleText = itemView.findViewById(R.id.itemTitle);
            mDateText = itemView.findViewById(R.id.itemDate);

        }

        public void bindTo(AppointmentItem currentItem) {
            mTitleText.setText(currentItem.getTitle());
            mDateText.setText(DateFormat.getDateTimeInstance().format(currentItem.getDate()));

            itemView.findViewById(R.id.buttonDeleteAppointment).setOnClickListener(view -> ((BookingsListActivity)mContext).deleteAppointment(currentItem));
        }
    }
}

