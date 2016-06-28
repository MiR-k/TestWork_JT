package apicore.interfaces;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IGetService {

    @GET("computer/api/xml?tree=computer[displayName,temporarilyOffline]")
    Call<ResponseBody> nodeList();

    @GET("api/xml?tree=jobs[displayName,url,color]")
    Call<ResponseBody> jobList();

    @GET("/job/{nameJob}/api/xml?tree=color")
    Call<ResponseBody> statusJobs(@Path("nameJob") String nameJob);

}
