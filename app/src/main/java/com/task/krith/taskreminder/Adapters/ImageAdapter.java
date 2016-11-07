package com.task.krith.taskreminder.Adapters;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.task.krith.taskreminder.R;

import java.io.File;
import java.util.List;

/**
 * Created by krith on 06/11/16.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private List<String> imageList;

    public ImageAdapter(List<String> imageList) {
        this.imageList = imageList;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_row, parent, false);
        return new ImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.image.setImageURI(getImageUri(imageList.get(position)));
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;

        public ImageViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.image);
        }
    }

    public Uri getImageUri(String path) {
        Uri uriFromPath = Uri.fromFile(new File(path));
        return uriFromPath;
    }
}

