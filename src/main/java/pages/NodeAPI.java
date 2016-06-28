package pages;

import apicore.RestApiServer;
import apicore.interfaces.IPostService;
import okhttp3.*;
import okhttp3.Response;
import org.dom4j.Document;
import org.dom4j.Element;
import retrofit2.*;
import retrofit2.Call;

import java.io.IOException;
import java.util.List;

import static apicore.RestApiServer.getBaseUrl;

public class NodeAPI extends BaseAPI {

    private final String  messageOne = "Запрос - %s Node: %s";
    private final String  query = "computer/doCreateItem?name=%s&remoteFS=C:\\\\Test1&mode=NORMAL&type=hudson.slaves.DumbSlave&json={\"launcher\":{\"stapler-class\":\"hudson.plugins.sshslaves.SSHLauncher\",\"credentialsId\":\"\"},\"retentionStrategy\":{\"stapler-class\":\"hudson.slaves.RetentionStrategy$Always\"},\"nodeProperties\":{\"stapler-class-bag\":\"true\"}}";

    private String statusEntity;

    public String getStatusEntity() {return statusEntity;}

    //количество Node на сервере
    private int numNode = 0;

    public int getNumNode() {
        return numNode;
    }

    protected void setNumNode(int currentView) {
        this.numNode = currentView;
    }

    /**
     * Create View by Name
     * and check query status
     * @param nameEntity
     */
    public void create(String nameEntity){

        OkHttpClient client = new OkHttpClient();

        Request requestTo = new Request.Builder()
                .url(String.format(getBaseUrl()+ query,nameEntity))
                .build();

        try {
            Response respons = client.newCall(requestTo).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        Call<Void> call = service.delete(nodeName,Entity.Node.toString());

        info(String.format(messageOne,"удаление", nodeName));
        info(mesageExpexted);
        checkStatus(call);
    }

    public void disable(String name){
        disable(Entity.Node.toString(),name,"toggleOffline");
    }

    /**
     *
     * @param nameView String
     * @param expected
     */
    public void check(String nameView, int expected) {

        RestApiServer restApiServer = new RestApiServer();

        Document doc = restApiServer.getService();

        List<Element> document = doc.getRootElement().elements("view");

        String messageSuccess = String.format("Фактическое количество Node - %s",document.size());
        String messageFail = String.format("Фактическое количество Node - %s", getNumNode());
        info("Текущее количество элементов: " + getNumNode());
        info("Ожидаемое количество Job - " + (getNumNode()+expected));

        doAssert(document.size() == getNumNode()+expected,messageSuccess,messageFail);
        // -1 удаление немного меняется логика проверки
        if (expected < 0) {
            doAssert(!checkList(document, "name", nameView), "Элемент отсутствует", "Элемент имеется в списке");
        } else {
            doAssert(checkList(document, "name", nameView), "Элемент имеется в списке", "Элемент отсутствует");
        }
    }

    public void checkNum(){
        RestApiServer restApiServer = new RestApiServer();

        Document doc = restApiServer.getService();
        List<Element> document = doc.getRootElement().elements("view");
        numNode = document.size();
        info("На сервере всего Node - " + getNumNode());
        info("Состояние сохранено");
    }

    protected String formatLogMsg(String message) {
        return message;
    }
}
