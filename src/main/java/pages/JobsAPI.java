package pages;

import java.util.List;

import apicore.interfaces.IGetService;
import okhttp3.*;
import org.dom4j.*;

import apicore.RestApiServer;
import apicore.interfaces.IPostService;
import retrofit2.*;
import retrofit2.Call;


import static apicore.RestApiServer.getBaseUrl;

public class JobsAPI extends BaseAPI{

    private final String resourceName = "XMLFile/XMLJobCreate";

    private final String stringCreateJob = "createItem?name=%s";

    private final String messageOneJob = "Запрос - %s Job: %s";
    private final String messageTwoJob = "Запрос - %s Job: %s в %s";

    private String statusEntity;

    public void setStatusEntity(String status){this.statusEntity = status;}

    public String getStatusEntity(){return statusEntity;}

    //количество Job на сервере
    private int numJob = 0;

    public int getNumJob() {
        return numJob;
    }

    protected void setNumJob(int currentJob) {
        this.numJob = currentJob;
    }

    /**
     * Create Job for Name
     * using xml file
     * @param jobName String
     */
    public void create(String jobName){

        String urlCreate = String.format(getBaseUrl()+stringCreateJob,jobName);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/xml; charset=utf-8"), jobXMLString(resourceName));

        Request request = new Request.Builder()
                .url(urlCreate)
                .post(body)
                .build();

        info(String.format(messageOneJob,"создание", jobName));
        info(mesageExpexted);
        checkCreateStatus(request,client);
    }

    /**
     * Query copy Job
     * @param fromJobName String
     * @param toJobName String
     */
    public void copy(String fromJobName, String toJobName){

        RestApiServer restApiServer = new RestApiServer();
        Retrofit retrofit = restApiServer.getRetrofit();

        IPostService service = retrofit.create(IPostService.class);

        Call<Void> call = service.copyJob(fromJobName, "copy", toJobName);
        info(String.format(messageTwoJob,"копирование", fromJobName, toJobName));
        info(mesageExpexted);

        checkStatus(call);
    }

    /**
     * Query rename Job
     * @param jobOldName String
     * @param jobNewName String
     */
    public void rename(String jobOldName, String jobNewName){
        RestApiServer restApiServer = new RestApiServer();
        Retrofit retrofit = restApiServer.getRetrofit();

        IPostService service = retrofit.create(IPostService.class);

        Call<Void> call = service.renameJod(jobOldName, jobNewName);

        info(String.format(messageTwoJob,"переименование", jobOldName, jobNewName));
        info(mesageExpexted);
        checkStatus(call);
    }

    /**
     * Inquiry delete Job
     * @param jobName String
     */
    public void delete(String jobName){
        RestApiServer restApiServer = new RestApiServer();
        Retrofit retrofit = restApiServer.getRetrofit();

        IPostService service = retrofit.create(IPostService.class);

        Call<Void> call = service.delete(jobName,Entity.Job.toString());

        info(String.format(messageOneJob,"удаление", jobName));
        info(mesageExpexted);
        checkStatus(call);
    }

    /**
     * Inquiry builds Job
     * @param jobName String
     */
    public void build(String jobName){

        // implementation delay. while in the body of the function. can be taken
        int secondBuild = 0;
        RestApiServer restApiServer = new RestApiServer();
        Retrofit retrofit = restApiServer.getRetrofit();

        IPostService service = retrofit.create(IPostService.class);

        Call<Void> call = service.buildJob(jobName, secondBuild+"sec");
        info(String.format("Запрос - сборка Job через %s секунд", secondBuild));
        info(mesageExpexted);
        checkStatus(call);
        try {
            wait(jobName,secondBuild+300);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка Wait ", e);
        }
    }

    /**
     * Inquiry disable Job
     * @param jobName String
     */
    public void disable(String jobName){
        disable("job", jobName, "disable");
    }

    /**
     *
     * @param nameJob String
     * @param expected
     */
    public void check(String nameJob, int expected) {

        RestApiServer restApiServer = new RestApiServer();

        Document doc = restApiServer.getService();

        List<Element> document = doc.getRootElement().elements("job");

        String messageSuccess = String.format("Фактическое количество Job - %s",document.size());
        String messageFail = String.format("Фактическое количество Job - %s",getNumJob());
        info("Текущее количество элементов " +getNumJob());
        info("Ожидаемое количество Job - " + (getNumJob()+expected));

        doAssert(document.size() == getNumJob()+expected,messageSuccess,messageFail);
        // -1 удаление немного меняется логика проверки
        if (expected < 0) {
            doAssert(!checkList(document, "name", nameJob), "Элемент отсутствует", "Элемент имеется в списке");
        } else {
            doAssert(checkList(document, "name", nameJob), "Элемент имеется в списке", "Элемент отсутствует");
        }
    }

    /**
     * Check and save the number of Job on the server
     * on int numJob
     */
    public void checkNum(){
        RestApiServer restApiServer = new RestApiServer();

        Document doc = restApiServer.getService();
        List<Element> document = doc.getRootElement().elements("job");
        numJob = document.size();
        info("На сервере всего Job - " + getNumJob());
        info("Состояние сохранено");
    }

    /**
     * Checking and save satus build
     * @param jobName String
     */
    public void checkBuild(String jobName){
        Document doc = getServerToXml();
        List<Element> elementList = doc.getRootElement().elements("job");
        for (Element node: elementList){
            if (getStatusEntity() == null)
            {
                if (node.elementText("displayName").equals(jobName)){
                    setStatusEntity(node.elementText("color"));
                    info(String.format("Статус успешно сохранён %s", getStatusEntity()));
                }
            } else
            {
                if (node.elementText("displayName").equals(jobName)){
                    info(String.format("Сохранённый статус build(color) - %s", getStatusEntity()));
                    doAssert(!node.elementText("color").equals(getStatusEntity()),
                            String.format("Статус build(color) %s был изменён на - %s",jobName, node.elementText("color")),
                            String.format("Статус build(color) %s не был изменён - %s",jobName, getStatusEntity()));
                }
            }
        }
    }

    /**
     * Job gets the list with the following fields
     * display: Name, url, color
     * @return Document org.dom4j
     */
    private Document getServerToXml() {

        RestApiServer restApiServer = new RestApiServer();
        Retrofit retrofit = restApiServer.getRetrofit();

        IGetService service = retrofit.create(IGetService.class);

        Call<ResponseBody> call = service.jobList();
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

    protected String formatLogMsg(String message) {
        return message;
    }
}
