package hr.vsite.messageapp.activities.starters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import hr.vsite.messageapp.R;
import hr.vsite.messageapp.activities.message.MessageActivity;
import hr.vsite.messageapp.model.response.UserChatDto;
import hr.vsite.messageapp.model.response.UserDto;
import hr.vsite.messageapp.model.transferModel.TransferModel;
import hr.vsite.messageapp.utils.AppController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirstPageActivityAdapter extends RecyclerView.Adapter<FirstPageActivityAdapter.MainActivityViewHolder> implements Filterable {

    private List<UserChatDto> chatSet;
    private List<UserChatDto> chatSetFiltered;
    private List<UserChatDto> chatSetFull;

    private final UserDto userDto;

    private final Context context;
    private int searchViewLen = 0;


    public FirstPageActivityAdapter(@NonNull Context context, UserDto userID) {
        this.context = context;
        chatSet = Collections.emptyList();
        this.userDto = userID;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    protected class MainActivityViewHolder extends RecyclerView.ViewHolder {
        private TextView userName, message, messageReceived;
        private LinearLayout linear;


        MainActivityViewHolder(View view) {
            super(view);
            linear = view.findViewById(R.id.linear);
            userName = view.findViewById(R.id.chat_name);
            messageReceived = view.findViewById(R.id.messageReceived);
            message = view.findViewById(R.id.message);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<UserChatDto> list = new ArrayList<>(chatSet);
                    readMessage(
                            list.get(getLayoutPosition()).getChatDto().getChatId(),
                            userDto.getUserId(),
                            chatSet.get(getLayoutPosition())
                    );
                }
            });
        }
    }

    public void readMessage(int chatId, int userId, UserChatDto userChatDto) {
        Call<Boolean> call = AppController.userClient.readMessage(chatId, userId);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Intent i = new Intent(context, MessageActivity.class);
                        i.putExtra("MessageActivity", new TransferModel(userChatDto, userDto));
                        context.startActivity(i);
                    } else {
                        // Handle API error
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                // Handle network failure
            }
        });
    }

    @Override
    public MainActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.receipt_items, parent, false);
        return new MainActivityViewHolder(layoutView);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    searchViewLen = 0;
                    chatSetFiltered = chatSet;
                } else {
                    if (searchViewLen > charString.length()) {
                        searchViewLen = charString.length();
                        List<UserChatDto> filteredList = new ArrayList<>();
                        for (UserChatDto row : chatSetFull) {
                            // name match condition. this might differ depending on your requirement
//                            // here we are looking for name or phone number match
                            if (row.getChatDto().getChatName().toUpperCase(Locale.getDefault()).contains(charString.toUpperCase(Locale.getDefault())) ||
                                    row.getChatDto().getUserCreator().getUserName().toUpperCase(Locale.getDefault()).contains(charString.toUpperCase(Locale.getDefault())) ||
                                    row.getChatDto().getCreatedAt().toUpperCase(Locale.getDefault()).contains(charString.toUpperCase(Locale.getDefault()))) {
                                filteredList.add(row);
                            }
                        }
                        chatSetFiltered = filteredList;

                    } else {
                        searchViewLen = charString.length();

                        List<UserChatDto> filteredList = new ArrayList<>();
                        for (UserChatDto row : chatSetFull) {
                            // name match condition. this might differ depending on your requirement
//                            // here we are looking for name or phone number match
                            if (row.getChatDto().getChatName().toUpperCase(Locale.getDefault()).contains(charString.toUpperCase(Locale.getDefault())) ||
                                    row.getChatDto().getUserCreator().getUserName().toUpperCase(Locale.getDefault()).contains(charString.toUpperCase(Locale.getDefault())) ||
                                    row.getChatDto().getCreatedAt().toUpperCase(Locale.getDefault()).contains(charString.toUpperCase(Locale.getDefault()))) {
                                filteredList.add(row);
                            }
                        }
                        chatSetFiltered = filteredList;
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = chatSetFiltered;
                filterResults.count = chatSetFiltered.size();
                return filterResults;
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                chatSet = (List<UserChatDto>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MainActivityViewHolder holder, int position) {
        try {
            holder.userName.setText(chatSet.get(position).getUserDto().getUserName());
            if (chatSet.get(position).getChatDto().getMessageDto().isEmpty()) {
                holder.message.setText("");
                holder.messageReceived.setText("");
            } else {
                holder.messageReceived.setText(timeToString(chatSet.get(position).getChatDto().getMessageDto().get(0).getCreatedAt()));
                if (chatSet.get(position).getChatDto().getMessageDto().get(0).isRead()) {
                    holder.message.setTypeface(null, Typeface.NORMAL);
                } else {
                    holder.message.setTypeface(null, Typeface.BOLD);
                }
                holder.message.setText(chatSet.get(position).getChatDto().getMessageDto().get(0).getMessageText());

            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    public void setOutListItems(List<UserChatDto> listItems) {
        this.chatSet = listItems;
        this.chatSetFull = new ArrayList<>(listItems);
        this.chatSetFiltered = listItems;
        notifyDataSetChanged(); // Notify the adapter of data change
    }


    @Override
    public int getItemCount() {
        try {
            return chatSetFiltered.size();
        } catch (NullPointerException n) {
            return 0;
        }
    }

    private String timeToString(String dateTimeString) throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        Date dateTime = inputFormat.parse(dateTimeString);


        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String time = timeFormat.format(dateTime);

        return time;
    }

    private String dateToString(String dateTimeString) throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        Date dateTime = inputFormat.parse(dateTimeString);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = dateFormat.format(dateTime);

        return date;
    }
}