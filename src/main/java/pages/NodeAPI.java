package pages;

import apicore.RestApiServer;
import apicore.interfaces.IGetService;
import apicore.interfaces.IPostService;
import okhttp3.*;
import okhttp3.Response;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
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

    private String getStatusEntity() {return statusEntity;}
    private void setStatusEntity(String statusEntity){this.statusEntity = statusEntity;}

    //количество Node на сервере
    private int numNode = 0;

    protected int getNumNode() {
        return numNode;
    }

    protected void setNumNode(int currentView) {
        this.numNode = currentView;
    }

    /**
     * Create View by Name
     * and check query status
     * @param nameEntity String
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

    /**
     * Disable Node by Name
     * @param name String
     */
    public void disable(String name){
        disable(Entity.Node.toString(),name,"toggleOffline");
    }

    /**
     * Checking Node
     * depending on the verification operation. operation contained in the enum
     * @param nameNode String
     * @param expected int
     */
    public void check(String nameNode, int expected) {

        Document doc = getService();

        List<Element> document = doc.getRootElement().elements("computer");

        String messageSuccess = String.format("Фактическое количество Node - %s",document.size());
        String messageFail = String.format("Фактическое количество Node - %s", getNumNode());
        info("Текущее количество элементов: " + getNumNode());
        info("Ожидаемое количество Node - " + (getNumNode()+expected));

        doAssert(document.size() == getNumNode()+expected,messageSuccess,messageFail);
        // -1 удаление немного меняется логика проверки
        if (expected < 0) {
            doAssert(!checkList(document, "displayName", nameNode), "Элемент отсутствует", "Элемент имеется в списке");
        } else {
            doAssert(checkList(document, "displayName", nameNode), "Элемент имеется в списке", "Элемент отсутствует");
        }
    }

    public void checkNum(){
        Document doc = getService();
        List<Element> document = doc.getRootElement().elements("computer");
        numNode = document.size();
        info("На сервере всего Node - " + getNumNode());
        info("Состояние сохранено");
    }

    /**
     *
     * @return Document org.dom4j
     * @throws Exception
     */
    private Document getService() {

        RestApiServer restApiServer = new RestApiServer();
        Retrofit retrofit = restApiServer.getRetrofit();

        IGetService service = retrofit.create(IGetService.class);

        Call<ResponseBody> call = service.nodeList();
        Document document;
        try {
            ResponseBody response = call.execute().body();
            document = DocumentHelper.parseText(response.string());
            response.close();
        } catch (Exception ex) {
            throw new RuntimeException("Ошибка get запроса", ex);
        }
        return document;
    }

    /**
     * Checking status Node by name
     * @param nodeName String
     */
    public void checkStatus(String nodeName){
        Document doc = getService();
        List<Element> elementList = doc.getRootElement().elements("computer");
        for (Element node: elementList){
            if (getStatusEntity() == null)
            {
                if (node.elementText("displayName").equals(nodeName)){
                    setStatusEntity(node.elementText("temporarilyOffline"));
                    info(String.format("Статус успешно сохранён %s", getStatusEntity()));
                }
            } else
            {
                if (node.elementText("displayName").equals(nodeName)){
                    info(String.format("Сохранённый статус temporarilyOffline - %s", getStatusEntity()));
                    info(String.format("Ожидаемый статус temporarilyOffline - %s", node.elementText("temporarilyOffline")));
                    doAssert(!node.elementText("temporarilyOffline").equals(getStatusEntity()),
                            String.format("Статус Node %s был изменён на - %s",nodeName, node.elementText("temporarilyOffline")),
                            String.format("Статус Node %s не был изменён - %s",nodeName, getStatusEntity()));
                }
            }
        }
    }

    protected String formatLogMsg(String message) {
        return message;
    }
}
