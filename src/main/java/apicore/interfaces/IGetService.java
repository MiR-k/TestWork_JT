package apicore.interfaces;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;

public interface IGetService {

    @GET("api/xml?tree=jobs[name,color,url]")
    Call<Response> listJobs();

    @GET("api/xml")
    Call<Response> listView();

}
