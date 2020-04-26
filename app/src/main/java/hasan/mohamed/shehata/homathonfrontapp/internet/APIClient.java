package hasan.mohamed.shehata.homathonfrontapp.internet;

import java.net.URL;
import java.util.List;

import hasan.mohamed.shehata.homathonfrontapp.pojo.Clinic;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    public static String base_url = "http://192.168.1.7:2020/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

//        if(retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
//        }


        return retrofit;
    }

}
