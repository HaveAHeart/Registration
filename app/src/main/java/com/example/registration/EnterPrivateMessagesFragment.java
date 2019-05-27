
package com.example.registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.registration.Models.Chatroom;
import com.example.registration.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EnterPrivateMessagesFragment extends Fragment {

    FirebaseUser firebaseUser;
    DatabaseReference ref;
    EditText user;

    String uid1;
    String uid2;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enter_private_messages, container, false);

        final Button button = view.findViewById(R.id.enter_pm);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                ref = FirebaseDatabase.getInstance().getReference();

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText user = view.findViewById(R.id.pm_userna);
                        final String username = user.getText().toString();

                        if (true) {
                            Toast.makeText(getActivity(), "Please enter text in Username" + user, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        ref.child("/users/" + firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final User currentUser = dataSnapshot.getValue(User.class);
                                final String currentUsername = currentUser.getUsername();
                                uid2 = currentUser.getUid();

                                Query usernameQuery = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("username").equalTo(username);
                                usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getChildrenCount() > 0) {
                                            if(currentUsername.equals(username)){
                                                final String privateChatroomName = "@" + currentUsername;
                                                Query privateChatroomNameQuery = FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("chatroomName").equalTo(privateChatroomName);
                                                privateChatroomNameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.getChildrenCount() == 0) {
                                                            ref.child("/chatrooms/" + "@" + currentUsername).setValue(new Chatroom(privateChatroomName));
                                                        }
                                                        Intent intent = new Intent(getActivity(), Main_chat_activity.class);
                                                        intent.putExtra("chatroomName", privateChatroomName);
                                                        startActivity(intent);
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                            } else {
                                                final String chatroomName = "@" + currentUsername + "@" + username;
                                                final String otherChatroomName = "@" + username + "@" + currentUsername;
                                                Query chatroomNameQuery = FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("chatroomName").equalTo(chatroomName);
                                                chatroomNameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.getChildrenCount() > 0) {
                                                            Intent intent = new Intent(getActivity(), Main_chat_activity.class);
                                                            intent.putExtra("chatroomName", chatroomName);
                                                            startActivity(intent);
                                                        } else {
                                                            Query otherChatroomNameQuery = FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("chatroomName").equalTo(otherChatroomName);
                                                            otherChatroomNameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    if (dataSnapshot.getChildrenCount() > 0) {
                                                                        Intent intent = new Intent(getActivity(), Main_chat_activity.class);
                                                                        intent.putExtra("chatroomName", otherChatroomName);
                                                                        startActivity(intent);
                                                                    } else {

                                                                        Query query = ref.child("/users/").orderByChild("username").equalTo(username);
                                                                        query.addValueEventListener(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                if(dataSnapshot.exists()){
                                                                                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                                                                        uid1 = snapshot.getKey();
                                                                                        ref.child("/chatrooms/" + chatroomName).setValue(new Chatroom(chatroomName, uid1 , uid2));
                                                                                        Intent intent = new Intent(getActivity(), Main_chat_activity.class);
                                                                                        intent.putExtra("chatroomName", chatroomName);
                                                                                        startActivity(intent);
                                                                                    }

                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                            }
                                                                        });
                                                                    }
                                                                }

                                                                @Override

                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });
                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                        } else {
                                            Toast.makeText(getActivity(), "Such user doesn't exist", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                });
            }
        });
        return view;
    }
}
