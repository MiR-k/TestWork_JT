import retrofit2.http.*;


public interface SomeRestApi {
    @POST("user/list")
    Call<Users> loadUsers();
}

    public void doSomething() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.nuuneoi.com/base/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService service = retrofit.create(APIService.class);
    }