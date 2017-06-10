package com.list.asus.forsigns.view;

/*
 * Created by ASUS on 2017/5/26.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.list.asus.forsigns.R;
import com.list.asus.forsigns.bean.Schedule;

import java.util.ArrayList;
import java.util.List;

public class CurriculumRecAdapter extends RecyclerView.Adapter<CurriculumRecAdapter.ViewHolder>{

    private List<Schedule> scheduleList = new ArrayList<>();
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView courseDetail;
        public ViewHolder(View itemView) {
            super(itemView);
            courseDetail = (TextView) itemView.findViewById(R.id.re_content);
        }
    }

    public CurriculumRecAdapter(Context context, List<Schedule> sList){
        this.context = context;
        scheduleList = sList;
    }

    @Override
    public CurriculumRecAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_fra_curriculum_recyclerview_item,null);
        ViewHolder holder= new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CurriculumRecAdapter.ViewHolder holder, int position) {
        Schedule s = scheduleList.get(position);
        Log.d("TAG", "onBindViewHolder: "+s.getClassTime());
        holder.courseDetail.setText(s.getCourseName() + " "+ s.getClassTime() + " "+ s.getClassroom());
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }


}
