package com.example.zba.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zba.databinding.ItemPasswordBinding;
import com.example.zba.interfaces.OnPasswordClickListener;
import com.example.zba.models.PasswordItem;

import java.util.List;

public class PasswordAdapter extends RecyclerView.Adapter<PasswordAdapter.PasswordViewHolder> {
    private List<PasswordItem> passwords;
    private OnPasswordClickListener listener;

    public PasswordAdapter(List<PasswordItem> passwords, OnPasswordClickListener listener) {
        this.passwords = passwords;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PasswordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPasswordBinding passwordBinding = ItemPasswordBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new PasswordViewHolder(passwordBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PasswordViewHolder holder, int position) {
        PasswordItem item = passwords.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return passwords.size();
    }

    public static class PasswordViewHolder extends RecyclerView.ViewHolder {
        private final ItemPasswordBinding passwordBinding;
        public PasswordViewHolder(@NonNull ItemPasswordBinding passBinding) {
            super(passBinding.getRoot());
            this.passwordBinding = passBinding;
        }

        public void bind(PasswordItem item, OnPasswordClickListener listener) {
            passwordBinding.tvAppName.setText(item.getAppName());
            passwordBinding.tvUsername.setText(item.getUserName());

            //click listener
            passwordBinding.getRoot().setOnClickListener(v -> {
                if(listener != null){
                    listener.onPasswordClick(item);
                }
            });

        }
    }

}
