package hr.vsite.messageapp.activities.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import hr.vsite.messageapp.R;
import hr.vsite.messageapp.model.response.MessageDto;
import hr.vsite.messageapp.model.response.UserDto;

public class MessageActivityAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context mContext;
    private List<MessageDto> mMessageList;

    private UserDto userDto;


    public MessageActivityAdapter(Context context, UserDto userDto) {
        this.mContext = context;
        mMessageList = Collections.emptyList();
        this.userDto = userDto;
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        MessageDto message = mMessageList.get(position);

        if (message.getUserSender().getUserId().equals(userDto.getUserId())) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    public void setOutListItems(List<MessageDto> ListItems) {
        this.mMessageList = ListItems;
        notifyDataSetChanged();
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_me, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_other, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MessageDto message = mMessageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, dateText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_gchat_message_me);
            dateText = (TextView) itemView.findViewById(R.id.text_gchat_date_me);
            timeText = (TextView) itemView.findViewById(R.id.text_gchat_timestamp_me);
        }

        void bind(MessageDto message) {
            try {

                messageText.setText(message.getMessageText());
                dateText.setText(dateToString(message.getCreatedAt()));
                timeText.setText(timeToString(message.getCreatedAt()));
            } catch (ParseException e) {
                e.getMessage();
            }
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, dateText, timeText, nameText;


        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_gchat_message_other);
            dateText = (TextView) itemView.findViewById(R.id.text_gchat_date_other);
            timeText = (TextView) itemView.findViewById(R.id.text_gchat_timestamp_other);
            nameText = (TextView) itemView.findViewById(R.id.text_gchat_user_other);
        }

        void bind(MessageDto message) {

            try {
                messageText.setText(message.getMessageText());
                dateText.setText(dateToString(message.getCreatedAt()));
                timeText.setText(timeToString(message.getCreatedAt()));
                nameText.setText(message.getUserSender().getUserName());
            } catch (ParseException e) {
                e.getMessage();
            }
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