package hasan.mohamed.shehata.homathonfrontapp.internet;

import java.util.List;

import hasan.mohamed.shehata.homathonfrontapp.pojo.*;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("clinics/clinics")
    Call<List<Clinic>> doGetListClinics();

    @POST("clinics/clinic")
    Call<Void> createNewClinic(@Body Clinic clinic);

    @DELETE("clinics/clinic/{clinicid}")
    Call<Void> deleteClinic(@Path("clinicid") String clinicid);

    @PUT("clinics/clinic/{clinicid}")
    Call<Void> updateClinic(@Path("clinicid") String clinicid,@Body Clinic clinic);
}
