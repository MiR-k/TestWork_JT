package pages;

import apicore.RestApiServer;
import apicore.interfaces.IPostService;
import okhttp3.*;
import okhttp3.Response;
import retrofit2.*;

import java.io.IOException;

import static apicore.RestApiServer.getBaseUrl;

public class NodeAPI extends BaseAPI {

    private final String  messageOne = "Запрос - %s Node: %s";
    private final String  query = "computer/doCreateItem?name=%s&remoteFS=C:\\\\Test1&mode=NORMAL&type=hudson.slaves.DumbSlave&json={\"launcher\":{\"stapler-class\":\"hudson.plugins.sshslaves.SSHLauncher\",\"credentialsId\":\"\"},\"retentionStrategy\":{\"stapler-class\":\"hudson.slaves.RetentionStrategy$Always\"},\"nodeProperties\":{\"stapler-class-bag\":\"true\"}}";

    /**
     * Create View by Name
     * and check query status
     * @param nameEntity
     */
    public void create(String nameEntity) throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request requestTo = new Request.Builder()
                .url(String.format(getBaseUrl()+ query,nameEntity))
                .build();

        Response respons = client.newCall(requestTo).execute();

        info(String.format(messageOne,"создание", nameEntity));
        info(mesageExpexted);

        checkCreateStatus(requestTo, client);
    }

    /**
     * Check status query before create Entity
     * @param request Request.okhttp3
     * @param client OkHttpClient.okhttp3
     */
    protected void checkCreateStatus(Request request, OkHttpClient client){
        try {
            Response response = client.newCall(request).execute();
            doAssert( 400 == response.code(),messageSucces,String.format(messageFail,response.code()));
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(String nodeName){
        RestApiServer restApiServer = new RestApiServer();
        Retrofit retrofit = restApiServer.getRetrofit();

        IPostService service = retrofit.create(IPostService.class);

        retrofit2.Call<Void> call = service.delete(nodeName,Entity.Node.toString());

        info(String.format(messageOne,"удаление", nodeName));
        info(mesageExpexted);
        checkStatus(call);
    }


    public void disable(String name){
        disable(Entity.Node.toString(),name,"toggleOffline");
    }
    //edit qeury = configSubmit

    protected String formatLogMsg(String message) {
        return message;
    }
}
