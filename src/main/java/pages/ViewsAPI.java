package pages;

import apicore.RestApiServer;
import apicore.interfaces.IPostService;
import okhttp3.*;
import retrofit2.*;

import java.io.IOException;

import static apicore.RestApiServer.getBaseUrl;

public class ViewsAPI extends BaseAPI{

    private final String  messageOneView = "Запрос - %s View: %s";
    private final String  query = "createView?name=%s";

    private final String resourceName = "XMLFile/XMLViewCreate";

    /**
     * Create View by Name
     * and check query status
     * @param nameEntity
     */
    public void create(String nameEntity){

        String urlCreate = String.format(getBaseUrl()+query,nameEntity);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/xml; charset=utf-8"), jobXMLString(resourceName));

        Request request = new Request.Builder()
                .url(urlCreate)
                .post(body)
                .build();

        info(String.format(messageOneView,"создание", nameEntity));
        info(mesageExpexted);

        checkCreateStatus(request, client);
    }

    /**
     *
     * @param name String
     */
    public void delete(String name){
        RestApiServer restApiServer = new RestApiServer();
        Retrofit retrofit = restApiServer.getRetrofit();

        IPostService service = retrofit.create(IPostService.class);

        retrofit2.Call<Void> call = service.delete(name,Entity.View.toString());

        info(String.format(messageOneView,"удаление", name));
        info(mesageExpexted);
        checkStatus(call);
    }

    protected String formatLogMsg(String message) {
        return message;
    }
}
