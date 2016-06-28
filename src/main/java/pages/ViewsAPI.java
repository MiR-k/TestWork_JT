package pages;

import apicore.RestApiServer;
import apicore.interfaces.IPostService;
import okhttp3.*;
import org.dom4j.Document;
import org.dom4j.Element;
import retrofit2.*;

import java.io.IOException;
import java.util.List;

import static apicore.RestApiServer.getBaseUrl;

public class ViewsAPI extends BaseAPI{

    private final String  messageOneView = "Запрос - %s View: %s";
    private final String  query = "createView?name=%s";

    private final String resourceName = "XMLFile/XMLViewCreate";
    //количество Job на сервере
    private int numView = 0;

    public int getNumView() {
        return numView;
    }

    protected void setNumView(int currentView) {
        this.numView = currentView;
    }

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
     * Query delete View
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

    /**
     * Query checking View count
     * @param nameView String
     * @param expected
     */
    public void check(String nameView, int expected) {

        RestApiServer restApiServer = new RestApiServer();

        Document doc = restApiServer.getService();

        List<Element> document = doc.getRootElement().elements("view");

        String messageSuccess = String.format("Фактическое количество View - %s",document.size());
        String messageFail = String.format("Фактическое количество View - %s",getNumView());
        info("Текущее количество элементов: " +getNumView());
        info("Ожидаемое количество Job - " + (getNumView()+expected));

        doAssert(document.size() == getNumView()+expected,messageSuccess,messageFail);
        // -1 удаление немного меняется логика проверки
        if (expected < 0) {
            doAssert(!checkList(document, "name", nameView), "Элемент отсутствует", "Элемент имеется в списке");
        } else {
            doAssert(checkList(document, "name", nameView), "Элемент имеется в списке", "Элемент отсутствует");
        }
    }

    /**
     * Checking and save count View on server
     */
    public void checkNum(){
        RestApiServer restApiServer = new RestApiServer();

        Document doc = restApiServer.getService();
        List<Element> document = doc.getRootElement().elements("view");
        numView = document.size();
        info("На сервере всего View - " + getNumView());
        info("Состояние сохранено");
    }

    protected String formatLogMsg(String message) {
        return message;
    }
}
