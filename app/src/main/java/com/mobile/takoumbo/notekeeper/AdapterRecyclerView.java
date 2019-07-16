package com.mobile.takoumbo.notekeeper;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.util.List;

public class AdapterRecyclerView extends RecyclerView.Adapter<AdapterRecyclerView.Viewholder> {

    private List<NoteInfo> noteInfoList;
    private OnNoteListener onNoteListener;

    public AdapterRecyclerView(List<NoteInfo> noteInfos, OnNoteListener onNoteListener)
    {
        this.noteInfoList = noteInfos;
        this.onNoteListener = onNoteListener;
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.note_item_layout, viewGroup, false);

        return new Viewholder(view, onNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int i) {

        String course = noteInfoList.get(i).getCourse().getTitle();
        String title = noteInfoList.get(i).getTitle();
        String body = noteInfoList.get(i).getText();

        viewholder.setData2(course, title,body);
    }

    @Override
    public int getItemCount() {
        return noteInfoList.size();
    }

    // the viewHolder is in charge of filling the different values in the item_layout
    static class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView courseView;
        private TextView title;
        private TextView body;
        private OnNoteListener onNoteListener;

        public Viewholder(@NonNull  View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            courseView = itemView.findViewById(R.id.textViewCourse);
            title = itemView.findViewById(R.id.textViewTitle);
            body = itemView.findViewById(R.id.textViewText);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }

        private void setData2(String courseName, String titleText, String bodyText)
        {
            courseView.setText(courseName);
            title.setText(titleText);
            body.setText(bodyText);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener
    {
        void onNoteClick(int position);
    }
}
