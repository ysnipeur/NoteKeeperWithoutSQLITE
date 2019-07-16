package com.mobile.takoumbo.notekeeper;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CourseAdapterRecyclerView extends RecyclerView.Adapter<CourseAdapterRecyclerView.Viewholder> {

    private List<CourseInfo> courseList;
    private onCourseListener onCourseListener;

    public CourseAdapterRecyclerView(List<CourseInfo> courseInfos, onCourseListener onCourseListener)
    {
        this.courseList = courseInfos;
        this.onCourseListener = onCourseListener;
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.course_item_layout, viewGroup, false);

        return new Viewholder(view, onCourseListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int i) {

        String course = courseList.get(i).getTitle();

        viewholder.setData2(course);
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    // the viewHolder is in charge of filling the different values in the item_layout
    static class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView courseView;
        private onCourseListener onCourseListener;

        public Viewholder(@NonNull  View itemView, onCourseListener onCourseListener) {
            super(itemView);

            courseView = itemView.findViewById(R.id.textViewCourse);
            this.onCourseListener = onCourseListener;
            itemView.setOnClickListener(this);
        }

        private void setData2(String courseName)
        {
            courseView.setText(courseName);
        }

        @Override
        public void onClick(View v) {
            onCourseListener.onCourseClick(getAdapterPosition());
        }
    }

    public interface onCourseListener
    {
        void onCourseClick(int position);
    }


}
