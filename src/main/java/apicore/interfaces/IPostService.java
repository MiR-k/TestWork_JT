package apicore.interfaces;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IPostService {

    @POST("createItem")
    Call<Void> copyJob(@Query("from") String fromJob, @Query("mode")String mode, @Query("name") String toJob);

    @POST("{entity}/{jobName}/{nameDisable}")
    Call<Void> disable(@Path("entity")String entity, @Path("jobName") String name, @Path("nameDisable") String disableName);

    @POST("job/{jobName}/build?")
    Call<Void> buildJob(@Path("jobName") String name,@Query("delay") String sec);

    @POST("job/{jobName}/doRename")
    Call<Void>  renameJod(@Path("jobName") String name, @Query("newName") String newName);

    @POST("/{entity}/{name}/doDelete")
    Call<Void> delete(@Path("name") String name,@Path("entity") String entity);

}