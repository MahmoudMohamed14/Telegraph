package com.example.telegraph.data;

import com.example.telegraph.notification.MyResponse;
import com.example.telegraph.notification.NotificationSender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key = AAAALIEiSyI:APA91bF1bWEhu3yevRL1KOk8MlSOozYMldxnnzUL4NTIfyviR8_nGf0-XWy4G_yXUxF-eAEuxesbvlxx_9GM6_IXAEVNgF68SjGJhRtEc-JxmnQnRSqZRsgF8XZkWRUVq22bqoIovOYM"
    })
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body NotificationSender body );
}
