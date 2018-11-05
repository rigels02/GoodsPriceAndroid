package org.rb.goodspriceandroid;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import org.rb.goodspriceandroid.good.GoodContent;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link GoodContent.GoodItem} and makes a call to the
 * specified {@link GoodFragment.OnListFragmentInteractionListener}.
 * FOR INFO: the items in the list ar counted from 0.
 */
public class GoodRecyclerViewAdapter extends RecyclerView.Adapter<GoodRecyclerViewAdapter.ViewHolder> {

    private int selectedPos = 0;
    private final List<GoodContent.GoodItem> mValues;
    private final GoodFragment.OnListFragmentInteractionListener mListener;

    public GoodRecyclerViewAdapter(List<GoodContent.GoodItem> items, GoodFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_good, parent, false);
        return new ViewHolder(view);
    }

    public void setSelectedPos(int position){
        this.selectedPos=position;
    }
    public void setSelectedPositionAndHighLight(int position){
        int oldSelcted= selectedPos;
        selectedPos = position;
        //notifi old item changed, reset color
        notifyItemChanged(oldSelcted);
        //notify new item changed, set color
        notifyItemChanged(selectedPos);
    }
    public int getSelectedPosition(){
        return selectedPos;
    }

    public GoodContent.GoodItem getSelectedItem(){
        if(selectedPos>=0) {
           return mValues.get(selectedPos);
        }
        return null;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);

        //--for item selection highlight
        if(selectedPos == position){
            // Here I am just highlighting the background
            holder.itemView.setBackgroundColor(Color.GREEN);
        }else{
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    //--for item selection highlight
                    // Updating old as well as new positions
                    notifyItemChanged(selectedPos);
                    selectedPos = position;
                    notifyItemChanged(selectedPos);
                    //--for

                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(selectedPos);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public GoodContent.GoodItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.id);
            mContentView = view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
