package com.chat.sharechat;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> implements InternetConnectivityReciever.ConnectivityReceiverListener {
    private List<ChatMessage> chatMessages;
    private Context context;

    public ChatAdapter(List<ChatMessage> chatMessages, Context context) {
        this.chatMessages = chatMessages;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.chat_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ChatMessage chatMessage = chatMessages.get(position);
        holder.header.setOnClickListener(v -> {
            if (checkConnection()) {
                chatMessages.add(new ChatMessage("", "Hello Dear", true));
                chatMessages.add(new ChatMessage("https://www.pluralsight.com/content/pluralsight/en/resource-center/webinars/migrating-beyond-java-8/thank-you/jcr:content/image-res/file.transform/share-image/image.img.142c7751-88b2-424b-8318-1860a870db25.jpg", "Hello..", false));
                this.notifyDataSetChanged();
            } else {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.no_internet_dialog);
                TextView textView = dialog.findViewById(R.id.try_again);
                ProgressBar progressBar = dialog.findViewById(R.id.progress);
                textView.setOnClickListener(v1 -> {
                    progressBar.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(() -> {
                        if (checkConnection()) {
                            progressBar.setVisibility(View.GONE);
                            dialog.dismiss();
                        } else {
                            progressBar.setVisibility(View.GONE);
                        }
                    }, 2000);
                });
                dialog.show();
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });
        holder.textView.setText(chatMessage.getMessage());
        if (chatMessage.isStatus()) {
            holder.image.setVisibility(View.VISIBLE);
            if (chatMessage.getImage().length() > 0) {
                holder.imageLayout.setVisibility(View.VISIBLE);
                Glide.with(context).load(chatMessage.getImage()).into(holder.image);
            } else {
                holder.imageLayout.setVisibility(View.GONE);
            }
            Log.d("IMAGE", "" + chatMessage.getImage());
            holder.imageView.setVisibility(View.GONE);
            layoutParams.gravity = Gravity.RIGHT;
            holder.linearLayout.setLayoutParams(layoutParams);
            holder.messageLayout.setBackground(context.getDrawable(R.drawable.outgoing_message));
            holder.textView.setTextColor(Color.WHITE);
        } else {
            holder.image.setVisibility(View.VISIBLE);
            Log.d("IMAGE", "" + chatMessage.getImage());
            if (chatMessage.getImage().length() > 0) {
                holder.imageLayout.setVisibility(View.VISIBLE);
                Glide.with(context).load(chatMessage.getImage()).into(holder.image);
            } else {
                holder.imageLayout.setVisibility(View.GONE);
            }
            holder.imageView.setVisibility(View.VISIBLE);
            layoutParams.gravity = Gravity.LEFT;
            holder.linearLayout.setLayoutParams(layoutParams);
            holder.messageLayout.setBackground(context.getDrawable(R.drawable.incoming_message));
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout, messageLayout, imageLayout, header;
        ImageView imageView, image;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.chatlayout);
            imageView = itemView.findViewById(R.id.profile);
            textView = itemView.findViewById(R.id.message);
            image = itemView.findViewById(R.id.image);
            messageLayout = itemView.findViewById(R.id.message_layout);
            imageLayout = itemView.findViewById(R.id.image_layout);
            header = itemView.findViewById(R.id.header);
        }
    }

    private boolean checkConnection() {
        return InternetConnectivityReciever.isConnected();
    }
}
