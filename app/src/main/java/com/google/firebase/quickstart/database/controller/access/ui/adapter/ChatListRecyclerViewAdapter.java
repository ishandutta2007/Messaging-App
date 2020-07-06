package com.google.firebase.quickstart.database.controller.access.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.quickstart.database.R;
import com.google.firebase.quickstart.database.controller.access.ui.fragment.ChatListFragment.OnListFragmentInteractionListener;
import com.google.firebase.quickstart.database.controller.access.ui.fragment.dummy.DummyContent.DummyItem;
import com.google.firebase.quickstart.database.entity.Contact;
import com.google.firebase.quickstart.database.util.Common;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ChatListRecyclerViewAdapter extends RecyclerView.Adapter<ChatListRecyclerViewAdapter.ViewHolder> {

    private final List<Contact> mValues;
    private final OnListFragmentInteractionListener mListener;

    public ChatListRecyclerViewAdapter(List<Contact> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_chatlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).name);

        String lastMessage = holder.mItem.lastMessage;
        String[] lastMessages = lastMessage.split("\n");
        if (lastMessages.length > 1) {
            if (lastMessages[0].length() > 25)
                lastMessage = lastMessages[0].substring(0, 25).concat("...");
            else
                lastMessage = lastMessages[0].concat("...");
        } else if (lastMessage.length() > 25) {
            lastMessage = lastMessage.substring(0, 25).concat("...");
        }
        holder.mContentView.setText(lastMessage);
        String date = Common.getDateString(holder.mItem.lastUpdated);
        String todayDate = Common.getDateString(System.currentTimeMillis());

        if (todayDate.compareTo(date) == 0) {
            holder.typeView.setText(Common.getUserTime(holder.mItem.lastUpdated));
        } else {
            holder.typeView.setText(Common.getUserFriendlyDate(
                    holder.mView.getContext(),
                    holder.mItem.lastUpdated
            ));
        }

        if(holder.mItem.unreadCount==0){
            holder.typeView.setTextColor(holder.typeTextColor);
            holder.countLayout.setVisibility(View.GONE);
        }else {
            int color = holder.typeView.getResources().getColor(R.color.colorEmerland);
            holder.typeView.setTextColor(color);
            holder.countView.setText(String.valueOf(holder.mItem.unreadCount));
            holder.countLayout.setVisibility(View.VISIBLE);
            holder.countImageView.setColorFilter(color);
        }


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
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
        public final TextView typeView;
        private final TextView countView;
        private final View countLayout;
        private final ImageView countImageView;
        public Contact mItem;
        public int typeTextColor;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            typeView = (TextView) view.findViewById(R.id.type);
            countView = (TextView) view.findViewById(R.id.count);
            countImageView = (ImageView) view.findViewById(R.id.imageView_count);
            countLayout = view.findViewById(R.id.layout_count);

            typeTextColor = typeView.getCurrentTextColor();
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
