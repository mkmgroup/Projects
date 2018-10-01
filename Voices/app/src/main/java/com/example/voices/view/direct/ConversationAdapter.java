package com.example.voices.view.direct;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.voices.R;
import com.example.voices.controller.UserController;
import com.example.voices.model.Conversation;
import com.example.voices.model.User;
import com.example.voices.util.ResultListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Juan on 22/06/2018.
 */

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder>{
    private ConversationInteractionListener conversationInteractionListener;
    private List<Conversation> conversations;
    private Context context;

    public ConversationAdapter(ConversationInteractionListener conversationInteractionListener, List<Conversation> conversations, Context context) {
        this.conversationInteractionListener = conversationInteractionListener;
        this.conversations = conversations;
        this.context = context;
    }

    public void setConversations(List<Conversation> conversations) {
        this.conversations = conversations;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.conversation_cell, parent, false);

        ConversationViewHolder conversationViewHolder = new ConversationViewHolder(itemView);
        return conversationViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {

        Conversation conversation = conversations.get(position);
        ConversationViewHolder conversationViewHolder = (ConversationViewHolder) holder;
        conversationViewHolder.bindPeople(conversation, context);

    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }


    public class ConversationViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewUserName;
        private TextView textViewlastMessage;
        private CircleImageView imageView;
        private ImageButton micButton;
        private View view;

        public ConversationViewHolder(View itemView) {
            super(itemView);
            textViewUserName = itemView.findViewById(R.id.conversation_cell_username);
            textViewlastMessage = itemView.findViewById(R.id.conversation_cell_last_message);
            imageView = itemView.findViewById(R.id.image_view_conversation);
            micButton = itemView.findViewById(R.id.button_conversation_cell);
            view = itemView;
        }

        public void bindPeople(final Conversation conversation, final Context context){
            UserController userController = new UserController();
            Conversation convWithOtherUser = conversation;
            convWithOtherUser.getUsers().remove(FirebaseAuth.getInstance().getUid());



            if (convWithOtherUser.getUsers().size() > 1){

            }
            else {
                for (Map.Entry<String, Boolean> userID : convWithOtherUser.getUsers().entrySet()) {
                    userController.getUser(userID.getKey(), new ResultListener<User>() {
                        @Override
                        public void finish(User resultado) {
                            textViewUserName.setText(resultado.getNickname());
                            Glide.with(context).load(resultado.getAvatarUrl()).into(imageView);
                        }
                    });
                }
            }
            if (conversation.getLastMessage().getText() == null){
                String durFormated = String.format("%02d: %02d",
                        TimeUnit.MILLISECONDS.toMinutes(conversation.getLastMessage().getAudioDurLong()),
                        TimeUnit.MILLISECONDS.toSeconds(conversation.getLastMessage().getAudioDurLong()) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(conversation.getLastMessage().getAudioDurLong()))
                );
                textViewlastMessage.setText("Audio " + durFormated);
            }else {
                textViewlastMessage.setText(conversation.getLastMessage().getText());
            }

            micButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    conversationInteractionListener.onMicClick();
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    conversationInteractionListener.onConversationClick(conversation);
                }
            });

        }
    }
    public interface ConversationInteractionListener {
        // TODO: Update argument type and name
        void onMicClick();
        void onConversationClick(Conversation conversation);
    }
}
