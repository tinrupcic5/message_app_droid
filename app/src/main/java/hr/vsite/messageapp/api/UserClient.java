package hr.vsite.messageapp.api;


import java.util.List;
import java.util.Set;

import hr.vsite.messageapp.model.request.ChatParticipantsRequest;
import hr.vsite.messageapp.model.request.MessageBody;
import hr.vsite.messageapp.model.request.MessageRequest;
import hr.vsite.messageapp.model.request.UserRegisterRequest;
import hr.vsite.messageapp.model.response.ChatDto;
import hr.vsite.messageapp.model.response.MessageDto;
import hr.vsite.messageapp.model.response.MessageResponse;
import hr.vsite.messageapp.model.response.UserChatDto;
import hr.vsite.messageapp.model.response.UserDto;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * REQUEST MAPPINGS
 *
 * @RequestMapping("/api/message")
 * @RequestMapping("/api/user")
 * @RequestMapping("/api/auth")
 */
public interface UserClient {

    //region AUTH

    /**
     * login with credentials
     *
     * @return Authenticate object
     */
    @GET("user/login")
    Call<UserDto> login(
            @Query("username") String username,
            @Query("password") String password,
            @Query("firebaseToken") String token );

    /**
     * register new user
     *
     * @return 200 - OK
     */
    @POST("user/register")
    Call<MessageBody> registerUser(@Body UserRegisterRequest user);

    //endregion AUTH

    //region USERS

    /**
     * list of all users in table users except me
     * GET http://localhost:8080/api/user/{{id}}
     *
     * @return List of users
     */
    @GET("user/{id}")
    Observable<Set<UserDto>> listAllUsers(@Path(value = "id", encoded = true) Integer id);


    //endregion USERS


    //region MESSAGE

    /**
     * inbox/outbox messages from particular user
     *
     * @param id_from
     * @return list of messages with particular user
     */
    @GET("message/getMessagesBySenderIdAndReceiverId/{receiverId}/{senderId}")
    Observable<List<MessageDto>> getMessagesBySenderIdAndReceiverId(@Path(value = "receiverId", encoded = true) Integer id_from, @Path(value = "senderId", encoded = true) Integer senderId);


    /**
     * get all chats for me
     *
     * @param userId
     * @return list of chats where my user is
     */
    @GET("chat/find/{userId}")
    Call<Set<UserChatDto>> getChats(
            @Path(value = "userId", encoded = true) Integer userId,
            @Header("Authorization") String authToken);

    /**
     * create chat before sending message
     *
     * @param messageRequest
     * @return 200 - Ok
     */
    @POST("chat")
    Call<ChatDto> createChat(@Body ChatParticipantsRequest messageRequest);

    //receiverId
    @PUT("message/read/{chatId}/{userId}")
    Call<Boolean> readMessage(@Path(value = "chatId", encoded = true) Integer chatId,@Path(value = "userId", encoded = true) Integer userId);

    /**
     * get all chats for me
     *
     * @return list of chats where my user is
     */
    @GET("message/{chatId}")
    Call<Set<MessageDto>> findMessagesByChatId(@Path(value = "chatId", encoded = true) Integer chatId);


    /**
     * get all messages for me
     *
     * @param receiverId
     * @return list of messages from users for my id
     */
    @GET("message/getMessages/{receiverId}")
    Observable<List<MessageDto>> getMessages(@Path(value = "receiverId", encoded = true) Integer receiverId);

    /**
     * send message to particular user
     *
     * @param messageRequest
     * @return 200 - Ok
     * @Message userIdFrom, userIdTo
     */
    @POST("message")
    Call<MessageBody> sendMessage(@Body MessageRequest messageRequest);

    /**
     * set message  is_read to true
     *
     * @param authToken
     * @param receiverId
     * @return 200 - OK
     */
    @PUT("message/setMessageIsReadToTrue/{receiverId}")
    Call<MessageResponse> setMessageIsReadToTrue(@Header("Authorization") String authToken, @Path(value = "receiverId", encoded = true) Long receiverId);
    //endregion MESSAGE
}
