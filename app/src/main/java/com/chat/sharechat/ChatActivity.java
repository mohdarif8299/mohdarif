package com.chat.sharechat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.javiersantos.bottomdialogs.BottomDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends AppCompatActivity implements InternetConnectivityReciever.ConnectivityReceiverListener {
    LinearLayoutManager mLinearLayoutManager;

    @OnClick(R.id.open_dialog)
    void openDialog() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.my_custom_view, null);
        BottomDialog.Builder bottomDialog = new BottomDialog.Builder(this);
        bottomDialog.setCustomView(customView);
        bottomDialog.show();
    }

    public void sendMessage(View view) {
        if (checkConnection()) {
            chatMessages.add(new ChatMessage("", "Hello Dear", true));
            chatMessages.add(new ChatMessage("https://www.pluralsight.com/content/pluralsight/en/resource-center/webinars/migrating-beyond-java-8/thank-you/jcr:content/image-res/file.transform/share-image/image.img.142c7751-88b2-424b-8318-1860a870db25.jpg", "Hello..", false));
            chatAdapter.notifyDataSetChanged();
        } else {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.no_internet_dialog);
            TextView textView = dialog.findViewById(R.id.try_again);
            ProgressBar progressBar = dialog.findViewById(R.id.progress);
            textView.setOnClickListener(v -> {
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
    }

    ImageView imageView;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    @OnClick(R.id.back)
    void backPresses() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        chatMessages = new ArrayList<>();
        for(int i=0;i<30;i++) {
            if (i%2==0)
            chatMessages.add(new ChatMessage("https://www.pluralsight.com/content/pluralsight/en/resource-center/webinars/migrating-beyond-java-8/thank-you/jcr:content/image-res/file.transform/share-image/image.img.142c7751-88b2-424b-8318-1860a870db25.jpg", "Hiii", false));
            chatMessages.add(new ChatMessage("", "Hiii", false));
            chatMessages.add(new ChatMessage("", "Hello", true));
        }
        chatAdapter = new ChatAdapter(chatMessages, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setAdapter(chatAdapter);
        recyclerView.setOnClickListener(v -> {
            Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
        });
        recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
    }

    private boolean checkConnection() {
        return InternetConnectivityReciever.isConnected();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(ChatActivity.this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
    }
}
