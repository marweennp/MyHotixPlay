package com.hotix.myhotixplay.helpers;

import com.hotix.myhotixplay.models.Degree;
import com.hotix.myhotixplay.models.ItemPlay;
import com.hotix.myhotixplay.models.Prize;
import com.hotix.myhotixplay.models.RoomDetails;

import java.util.ArrayList;

public class ConstantConfig {

    /********************** *****************( Final )************************  *******************/
    // HNGAPI version
    public static final String API_VERSION = "v0";

    //Hotel Config
    public static final String CONFIG_BASE_URL = "http://41.228.21.123:99/";
    public static final String FINAL_APP_ID = "4";
    public static final String FINAL_HOTEL_CODE = "9999";// 9999 HotixDev

    /***************************************(Non Finol )*******************************************/

    //BASE URL
    public static String BASE_URL = "";

    //Global
    public static RoomDetails GLOBAL_ROOM_DETAILS = new RoomDetails();
    public static Degree GLOBAL_DEGREE= new Degree();
    public static ArrayList<Prize> GLOBAL_PRIZES_LIST= new ArrayList<Prize>();
}
