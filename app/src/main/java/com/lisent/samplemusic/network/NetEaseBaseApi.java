package com.lisent.samplemusic.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetEaseBaseApi {
    private static NetEaseBaseApi api;


    public static final NetEaseBaseApi getInstance(){
        if (api == null){
            api = new NetEaseBaseApi();
        }
        return api;
    }
    public final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://autumnfish.cn")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    public final HotService hotService = retrofit.create(HotService.class);
}
