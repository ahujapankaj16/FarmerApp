package com.example.pankaj.farmguide;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

/**
 * Created by PANKAJ on 2/2/2019.
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListHolder> {
        List<chat_list_item> list;

public ChatListAdapter(List<chat_list_item> list)
        {
        this.list = list;
        }

@Override
public void onBindViewHolder(@NonNull ChatListHolder holder, final int position) {
    holder.name.setText(list.get(position).getSeller_id());
    holder.cardView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String s = list.get(position).getSeller_id();
            Intent intent = new Intent(ChatList.Ccontext,ChatActivity.class);
            intent.putExtra("message_id",s);
            ChatList.Ccontext.startActivity(intent);
        }
    });
}
@Override
public int getItemCount() {
        return list.size();
        }

@Override
public ChatListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_list_item,viewGroup,false);
        return new ChatListHolder(v);
        }

class ChatListHolder extends RecyclerView.ViewHolder{
    TextView name;
    CardView cardView;
    public ChatListHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.clcvname);
        cardView = itemView.findViewById(R.id.clcv);

    }
}
}
