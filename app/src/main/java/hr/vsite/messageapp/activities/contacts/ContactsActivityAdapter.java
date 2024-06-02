package hr.vsite.messageapp.activities.contacts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import hr.vsite.messageapp.R;
import hr.vsite.messageapp.activities.message.MessageActivity;
import hr.vsite.messageapp.model.request.ChatParticipantsRequest;
import hr.vsite.messageapp.model.request.ChatRequest;
import hr.vsite.messageapp.model.request.MessageBody;
import hr.vsite.messageapp.model.request.ParticipantsRequest;
import hr.vsite.messageapp.model.request.UserRequest;
import hr.vsite.messageapp.model.response.ChatDto;
import hr.vsite.messageapp.model.response.MessageDto;
import hr.vsite.messageapp.model.response.User;
import hr.vsite.messageapp.model.response.UserChatDto;
import hr.vsite.messageapp.model.response.UserDto;
import hr.vsite.messageapp.model.transferModel.TransferModel;
import hr.vsite.messageapp.utils.AppController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactsActivityAdapter extends RecyclerView.Adapter<ContactsActivityAdapter.ContactsActivityViewHolder> implements Filterable {

    private List<UserDto> userList;
    private List<UserDto> userListFiltered;
    private List<UserDto> userListFull;

    private final UserDto userDto;

    private final Context context;
    private int searchViewLen = 0;

    private TransferModel transferModel;


    public ContactsActivityAdapter(@NonNull Context context, UserDto userDto) {
        this.context = context;
        userList = Collections.emptyList();
        this.userDto = userDto;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    protected class ContactsActivityViewHolder extends RecyclerView.ViewHolder {
        private TextView username, phonenumber, email;


        ContactsActivityViewHolder(View view) {
            super(view);
            username = view.findViewById(R.id.username);
            email = view.findViewById(R.id.email);
            phonenumber = view.findViewById(R.id.phonenumber);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setChat(userList.get(getLayoutPosition()), userDto);

                }
            });
        }
    }

    public void setChat(UserDto participant, UserDto myDto) {
        Set<ParticipantsRequest> participantsRequestSet = new HashSet<>();
        UserRequest userRequest = new UserRequest(
                myDto.getUserId(),
                myDto.getUserName(),
                myDto.getPhoneNumber(),
                myDto.getUserEmail()
        );

        UserRequest userRequest2 = new UserRequest(
                participant.getUserId(),
                participant.getUserName(),
                participant.getPhoneNumber(),
                participant.getUserEmail()
        );

        ChatRequest chatRequest = new ChatRequest(
                null,
                participant.getUserName(),
                userRequest
        );

        ParticipantsRequest participantsRequest = new ParticipantsRequest(
                chatRequest, userRequest);

        ParticipantsRequest participantsRequest2 = new ParticipantsRequest(
                chatRequest, userRequest2);


        participantsRequestSet.add(participantsRequest);
        participantsRequestSet.add(participantsRequest2);


        createChat(myDto, new ChatParticipantsRequest(
                chatRequest,
                participantsRequestSet
        ));
    }

    public void createChat(UserDto myDto, ChatParticipantsRequest messageRequest) {
        Call<ChatDto> call = AppController.userClient.createChat(messageRequest);

        call.enqueue(new Callback<ChatDto>() {
            @Override
            public void onResponse(Call<ChatDto> call, Response<ChatDto> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        UserChatDto u = new UserChatDto(
                                response.body(),
                                myDto);
                        transferModel = new TransferModel(u, userDto);

                        Intent i = new Intent(context, MessageActivity.class);
                        i.putExtra("MessageActivity", transferModel);
                        context.startActivity(i);
                    }

                } else {
                    // Handle API error
                    String a = "";
                }
            }

            @Override
            public void onFailure(Call<ChatDto> call, Throwable t) {
                // Handle network failure
                String a = t.getLocalizedMessage();

            }
        });
    }

    @Override
    public ContactsActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_items, parent, false);
        return new ContactsActivityViewHolder(layoutView);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    searchViewLen = 0;
                    userListFiltered = userList;
                } else {
                    if (searchViewLen > charString.length()) {
                        searchViewLen = charString.length();
                        List<UserDto> filteredList = new ArrayList<>();
                        for (UserDto row : userListFull) {
                            // name match condition. this might differ depending on your requirement
//                            // here we are looking for name or phone number match
                            if (row.getUserId().toString().contains(charString.toUpperCase(Locale.getDefault())) ||
                                    row.getPhoneNumber().toUpperCase(Locale.getDefault()).contains(charString.toUpperCase(Locale.getDefault())) ||
                                    row.getUserEmail().toUpperCase(Locale.getDefault()).contains(charString.toUpperCase(Locale.getDefault()))) {
                                filteredList.add(row);
                            }
                        }
                        userListFiltered = filteredList;

                    } else {
                        searchViewLen = charString.length();

                        List<UserDto> filteredList = new ArrayList<>();
                        for (UserDto row : userListFull) {
                            // name match condition. this might differ depending on your requirement
//                            // here we are looking for name or phone number match
                            if (row.getUserId().toString().contains(charString.toUpperCase(Locale.getDefault())) ||
                                    row.getPhoneNumber().toUpperCase(Locale.getDefault()).contains(charString.toUpperCase(Locale.getDefault())) ||
                                    row.getUserEmail().toUpperCase(Locale.getDefault()).contains(charString.toUpperCase(Locale.getDefault()))) {
                                filteredList.add(row);
                            }
                        }
                        userListFiltered = filteredList;
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = userListFiltered;
                filterResults.count = userListFiltered.size();
                return filterResults;
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                userList = (ArrayList<UserDto>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ContactsActivityViewHolder holder, int position) {
        holder.username.setText(userList.get(position).getUserName());
        holder.email.setText(userList.get(position).getUserEmail());
        holder.phonenumber.setText(userList.get(position).getPhoneNumber());

    }

    public void setOutListItems(List<UserDto> ListItems) {
        this.userList = ListItems;
        userListFull = ListItems;
        userListFiltered = userList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        try {
            return userListFiltered.size();
        } catch (NullPointerException n) {
            return 0;
        }
    }

}