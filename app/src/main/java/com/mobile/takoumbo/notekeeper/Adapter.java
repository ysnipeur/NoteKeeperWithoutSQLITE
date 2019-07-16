package com.mobile.takoumbo.notekeeper;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.Viewholder> {

    private List<ModelClass> modelClassList;
    private OnNoteListener onNoteListener;

    public Adapter(List<ModelClass> modelClassList, OnNoteListener onNoteListener)
    {
        this.modelClassList = modelClassList;
        this.onNoteListener = onNoteListener;
    }

    // this fonction enables me to get the view layout of an item and passes it to the viewholder to fill in the values
    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout, viewGroup, false);

        return new Viewholder(view, onNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int position) {

        int resource = modelClassList.get(position).getImageResource();
        String title = modelClassList.get(position).getTitle();
        String body = modelClassList.get(position).getBody();

        viewholder.setData(resource, title,body);
    }

    @Override
    public int getItemCount() {
        return modelClassList.size();
    }

    // the viewHolder is in charge of filling the different values in the item_layout
    static class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private TextView title;
        private TextView body;
        private OnNoteListener onNoteListener;

        public Viewholder(@NonNull  View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            title = itemView.findViewById(R.id.title);
            body = itemView.findViewById(R.id.body);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }

        private void setData(int iResource, String titleText, String bodyText)
        {
            imageView.setImageResource(iResource);
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
