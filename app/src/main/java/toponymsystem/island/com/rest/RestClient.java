package toponymsystem.island.com.rest;


import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import toponymsystem.island.com.model.IslandData;
import toponymsystem.island.com.model.IslandInformationData;
import toponymsystem.island.com.model.LoginData;
import toponymsystem.island.com.model.RegisterData;
import toponymsystem.island.com.model.SearchData;
import toponymsystem.island.com.model.SlideNewsDetailsData;
import toponymsystem.island.com.model.VersionInfoData;


public interface RestClient {
    /**
     * 注册接口
     *
     * @param fields 请求参数-用户名、密码
     * @return
     */
    @FormUrlEncoded
    @POST("register.action")
    Call<RegisterData> getRegister(@FieldMap Map<String, String> fields);

    /**
     * 登录接口
     *
     * @param fields 请求参数-用户名、密码
     * @return
     */
    @FormUrlEncoded
    @POST("login.action")
    Call<LoginData> getLogin(@FieldMap Map<String, String> fields);

    /**
     * 获取海岛详情
     *
     * @param fields 请求参数-海岛ID
     * @return
     */
    @FormUrlEncoded
    @POST("getIslandDetail.action")
    Call<IslandInformationData> getIslandDetail(@FieldMap Map<String, String> fields);

    /**
     * 获取轮播新闻详情
     *
     * @param fields 请求参数-新闻ID
     * @return SlideNewsDetailsData
     */
    @FormUrlEncoded
    @POST("slideDetail.action")
    Call<SlideNewsDetailsData> getSlideNewsDetail(@FieldMap Map<String, String> fields);

    /**
     * 获取获取查询数据
     *
     * @param fields 请求参数-关键字 以及 分页数
     * @return SearchData<List<IslandData>>
     */
    @FormUrlEncoded
    @POST("search.action")
    Call<SearchData<List<IslandData>>> getSearchData(@FieldMap Map<String, String> fields);

    /**
     * 分页数
     *
     * @param fields 请求参数-关键字 以及 分页数
     * @return RestBean<List<IslandData>>
     */
    @FormUrlEncoded
    @POST("search.action")
    Call<SearchData<List<IslandData>>> getPageCount(@FieldMap Map<String, String> fields);

    /**
     * 获取推荐用途数据
     *
     * @param fields 请求参数-关键字 以及 分页数
     * @return List<IslandData>
     */
    @FormUrlEncoded
    @POST("search.action")
    Call<SearchData<List<IslandData>>> getRecommendData(@FieldMap Map<String, String> fields);

    /**
     * 获取获取列表查询数据
     *
     * @param fields 请求参数-关键字 以及 分页数
     * @return List<IslandData>
     */
    @FormUrlEncoded
    @POST("search.action")
    Call<SearchData<List<IslandData>>> getTableStatisticsData(@FieldMap Map<String, String> fields);

    /**
     * 获取获取版本数据
     *
     * @param fields 请求参数-关键字 以及 分页数
     * @return VersionInfoData
     */
    @FormUrlEncoded
    @POST("checkVersion.action")
    Call<VersionInfoData> getVersionData(@FieldMap Map<String, String> fields);
}
