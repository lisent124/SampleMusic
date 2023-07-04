package com.lisent.samplemusic.network;

import com.lisent.samplemusic.entity.ComUrl;
import com.lisent.samplemusic.entity.Data;
import com.lisent.samplemusic.entity.HotSearch;
import com.lisent.samplemusic.entity.HotSingerBaseData;
import com.lisent.samplemusic.entity.Mv;
import com.lisent.samplemusic.entity.SearchBaseData;
import com.lisent.samplemusic.entity.Singer;
import com.lisent.samplemusic.entity.SingerDetailBaseData;
import com.lisent.samplemusic.ui.item.SongActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HotService {
    /**
     * 返回热门歌手信息
     * @param page
     * @param limit
     * @return
     */
    @GET("/top/artists")
    Call<HotSingerBaseData> getHotSinger(
            @Query("offset") int page,
            @Query("limit") int limit);

    /**
     * 返回歌手信息以及其热门的50首歌
     * @param id
     * @return
     */
    @GET("/artists")
    Call<SingerDetailBaseData> getSongOfSinger(
            @Query("id") int id);

    /**
     * 返回热搜关键字
     * @return
     */
    @GET("/search/hot/detail")
    Call<Data<List<HotSearch>>> getHotSearch();


    /**
     * 返回内地mv排行榜
     * @param page
     * @param limit
     * @return
     */
    @GET("/top/mv?area=内地")
    Call<Data<List<Mv>>> getHotMv(
            @Query("offset") int page,
            @Query("limit") int limit);
    /**
     * 返回搜索结果
     * @param keywords
     * @return
     */
    @GET("/search")
    Call<SearchBaseData> getSearchInfo(
            @Query("keywords") String keywords
    );

    /**
     * 返回mvUrl地址 即播放网络视频
     * @param id
     * @return
     */
    @GET("/mv/url")
    Call<Data<ComUrl>> getMvUrl(
            @Query("id") int id);

    /**
     * 返回SongUrl 地址，即播放网络歌曲
     * @param id
     * @return
     */
    @GET("/song/url")
    Call<Data<List<ComUrl>>> getSongUrl(
            @Query("id")int id);

    /**
     * 返回歌曲详细信息
     * @param id
     * @return
     */
    @GET("/song/detail")
    Call<SongActivity.songDetail> getSongDetail(
            @Query("ids")int id
    );

    @FormUrlEncoded
    @POST("login/cellphone")
    Call<Object> login(@Field("phone") String phone, @Field("password") String password);

    // https://www.wanandroid.com/article/list/0/json
    // https://www.wanandroid.com/article/list/json?page=0

    @GET("article/list/{page}/json")
    Call<List<Singer>> getArticle(@Path("page") int page);

}
