package com.hotix.myhotixplay.retrofit;

import com.hotix.myhotixplay.models.Degree;
import com.hotix.myhotixplay.models.HotelSettings;
import com.hotix.myhotixplay.models.PlayData;
import com.hotix.myhotixplay.models.Prize;
import com.hotix.myhotixplay.models.RoomDetails;
import com.hotix.myhotixplay.models.Success;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

import static com.hotix.myhotixplay.helpers.ConstantConfig.API_VERSION;

public interface RetrofitInterface {

    /**
     * GET********************************************************************************************
     **/

    // Get Play Data service call
    @GET("/HNGAPI/v0/api/MyHotixPlay/GetPlayData")
    Call<PlayData> getPlayDataQuery();

    // Get All Prizes service call
    @GET("/HNGAPI/v0/api/MyHotixPlay/GetAllPrizes")
    Call<ArrayList<Prize>> getPrizesListQuery();

    // Get Random Degree call
    @GET("/HNGAPI/v0/api/MyHotixPlay/GetRandomDegree")
    Call<Degree> getRandomDegreeQuery();

    // Get Details Resa By Room service call
    @GET("/HNGAPI/v0/api/MyHotixPlay/GetDetailsResaByRoom?")
    Call<RoomDetails> getRoomDetailsQuery(@Query("RoomNum") String RoomNum);

    /**
     * POST********************************************************************************************
     **/

    //Post Update Client service call
    @POST("/HNGAPI/v0/api/MyHotixPlay/UpdateClient")
    Call<Success> updateClientQuery(@Header("Content-Type") String content_type,
                                    @Body RoomDetails data);

    /**
     * Ping Serveur*********************************************************************************
     **/

    //Is Connected service call
    @GET("/HNGAPI/" + API_VERSION + "/api/MyHotixguest/isconnected")
    Call<ResponseBody> isConnectedQuery();

    /**
     * Hotel Config*********************************************************************************
     **/

    //Get Infos service call
    @GET("/hotixsupport/api/myhotix/GetInfos?")
    Call<HotelSettings> getInfosQuery(@Query("codehotel") String codehotel,
                                      @Query("applicationId") String applicationId);


}
