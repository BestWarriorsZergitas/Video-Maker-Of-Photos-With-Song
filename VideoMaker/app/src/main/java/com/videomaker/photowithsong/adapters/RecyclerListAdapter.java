/*
 * Copyright (C) 2015 Paul Burke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.videomaker.photowithsong.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.videomaker.photowithsong.R;
import com.videomaker.photowithsong.helper.ItemTouchHelperAdapter;
import com.videomaker.photowithsong.helper.ItemTouchHelperViewHolder;
import com.videomaker.photowithsong.helper.OnStartDragListener;
import com.videomaker.photowithsong.objects.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;


/**
 * Simple RecyclerView.Adapter that implements {@link ItemTouchHelperAdapter} to respond to move and
 * dismiss events from a {@link android.support.v7.widget.helper.ItemTouchHelper}.
 *
 * @author Paul Burke (ipaulpro)
 */
public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

    private final OnStartDragListener mDragStartListener;
    private ArrayList<Image> mItems;
    private Context mContext;
    private OnClickImageEdit onClickImageEdit;

    public ArrayList<Image> getmItems() {
        return mItems;
    }

    public RecyclerListAdapter(Context context, OnStartDragListener dragStartListener, ArrayList<Image> images, OnClickImageEdit onClickImageEdit) {
        mDragStartListener = dragStartListener;
        mItems = images;
        mContext = context;
        this.onClickImageEdit = onClickImageEdit;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_edit_swap, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        Glide.with(mContext).load(new File(mItems.get(position).getPath())).into(holder.thumbnail);
        // Start a drag whenever the handle view it touched
        holder.thumbnail.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mDragStartListener.onStartDrag(holder);
                return false;
            }
        });
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickImageEdit.onClickImageEdit(position);
            }
        });
    }

    @Override
    public void onItemDismiss(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public interface OnClickImageEdit {
        void onClickImageEdit(int position);
    }

    /**
     * Simple example of a view holder that implements {@link ItemTouchHelperViewHolder} and has a
     * "handle" view that initiates a drag event when touched.
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        public ImageView thumbnail;

        public ItemViewHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
        }

        @Override
        public void onItemSelected() {
        }

        @Override
        public void onItemClear() {

        }
    }
}
