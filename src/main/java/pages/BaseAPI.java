package pages;

import apicore.BaseEntity;
import apicore.RestApiServer;
import apicore.interfaces.IGetService;
import apicore.interfaces.IPostService;
import okhttp3.*;
import okhttp3.Response;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.List;

import static apicore.RestApiServer.getBaseUrl;

abstract class BaseAPI extends BaseEntity {

    private final String messageOne = "Запрос - %s %s: %s";
    protected final String messageSucces = "Запрос успешен. Статус: 200";
    protected final String mesageExpexted = "Ожидаемый результат статуса: 200";
    protected final String messageFail = "Запрос вернул статус: %s";

    private final String resourceName = "xml";

    /**
     * Convert xml File to string
     * @return String xml
     */
    protected String jobXMLString(String resourceName){
        try {

            SAXReader reader = new SAXReader();
            Document document = reader.read(this.getClass().getClassLoader().getResourceAsStream(resourceName));

            String stringXML = document.asXML();
            return stringXML;
        } catch (Exception ex) {
            throw new RuntimeException("Ошибка конвертации", ex);
        }
    }

    /**
     * Create Entity by Name
     * @param nameEntity String
     * @param query String
     */
    public void create(String nameEntity,String query){

        String urlCreate = String.format(getBaseUrl()+query,nameEntity);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/xml; charset=utf-8"), jobXMLString(resourceName));

        Request request = new Request.Builder()
                .url(urlCreate)
                .post(body)
                .build();

        info(String.format(messageOne,"создание", nameEntity));
        info(mesageExpexted);
        try {
            Response response = client.newCall(request).execute();
            doAssert( 200 == response.code(),messageSucces,String.format(messageFail,response.code()));
            response.close();
        } catch (IOException e) {
            fatal(e.getMessage());
            e.printStackTrace();
        }
    }

    protected void disable(String entity, String name, String stringDisable){
        RestApiServer restApiServer = new RestApiServer();
        Retrofit retrofit = restApiServer.getRetrofit();

        IPostService service = retrofit.create(IPostService.class);

        Call<Void> call = service.disable(entity,name,stringDisable);

        info(String.format(messageOne,"отключение",  entity, name));
        info(mesageExpexted);
        checkStatus(call);
    }

    /**
     * Check status query
     * before create method
     * @param call
     */
    protected void checkStatus(Call<Void> call){
        try {
            int status = call.execute().code();
            doAssert(200 == 200, messageSucces, String.format(messageFail,status));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check status query before create Entity
     * @param request Request.okhttp3
     * @param client OkHttpClient.okhttp3
     */
    protected void checkCreateStatus(Request request, OkHttpClient client){
        try {
            Response response = client.newCall(request).execute();
            doAssert( 200 == response.code(),messageSucces,String.format(messageFail,response.code()));
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected boolean checkList(List<Element> list, String resourceName, String checkName){
        boolean ckeking = false;

        for (Element obj: list){
            if (obj.elementText(resourceName).equals(checkName))
                ckeking = true;
        }
        return ckeking;
    }

    /**
     *  Entitiy jenkins:
     *  job,view,node
     */
    protected enum Entity{
        View("view"),
        Job("job"),
        Node("computer");

        public String value;

        Entity(final String values) {
            value = values;
        }

        public String toString() {
            return value;
        }
    }

    /**
     * Waiting until the end Job Activity
     * time is given in msseconds
     * @param nameJob
     * @param msecond
     * @throws InterruptedException
     * @throws IOException
     * @throws DocumentException
     */
    public void wait(String nameJob, int msecond) throws InterruptedException, IOException, DocumentException {

        Thread.sleep(msecond);
        RestApiServer restApiServer = new RestApiServer();
        Retrofit retrofit = restApiServer.getRetrofit();
        int elementAnimate;
        do{
            elementAnimate = 0;

            IGetService service = retrofit.create(IGetService.class);
            Call<ResponseBody> call = service.statusJobs(nameJob);
            ResponseBody response = call.execute().body();

            Document document = DocumentHelper.parseText(response.string());
            response.close();
            List<Element> elementList = document.getRootElement().elements("color");
            for (Element node: elementList){
                if (node.getText().contains("_anime")) {
                    Thread.sleep(msecond);
                    elementAnimate++;
                }
            }
        } while (elementAnimate != 0);
    }

}
