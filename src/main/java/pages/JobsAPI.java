package pages;

import java.io.*;
import java.util.List;

import okhttp3.*;
import okhttp3.Response;
import org.dom4j.*;
import org.dom4j.io.*;

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


    /**
     * Create Job for Name
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

    public void copy(String fromJobName, String toJobName){

        RestApiServer restApiServer = new RestApiServer();
        Retrofit retrofit = restApiServer.getRetrofit();

        IPostService service = retrofit.create(IPostService.class);

        Call<Void> call = service.copyJob(fromJobName, "copy", toJobName);
        info(String.format(messageTwoJob,"копирование", fromJobName, toJobName));
        info(mesageExpexted);

        checkStatus(call);

    }

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
     *
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

    public void build(String jobName, int second){
        RestApiServer restApiServer = new RestApiServer();
        Retrofit retrofit = restApiServer.getRetrofit();

        IPostService service = retrofit.create(IPostService.class);

        Call<Void> call = service.buildJob(jobName, second+"sec");
        info(String.format("Запрос - сборка Job через %s секунд", second));
        info(mesageExpexted);
        checkStatus(call);
    }

    public void disable(String jobName){
        disable("job", jobName, "disable");
    }

    protected String formatLogMsg(String message) {
        return message;
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
        info("Текущее количество элементов: " +getNumJob());
        info("Ожидаемое количество Job - " + (getNumJob()+expected));

        doAssert(document.size() == getNumJob()+expected,messageSuccess,messageFail);
        // -1 удаление немного меняется логика проверки
        if (expected < 0) {
            doAssert(!checkList(document, "name", nameJob), "Элемент отсутствует", "Элемент имеется в списке");
        } else {
            doAssert(checkList(document, "name", nameJob), "Элемент имеется в списке", "Элемент отсутствует");
        }
    }

    //количество Job на сервере
    private int numJob = 0;

    public int getNumJob() {
        return numJob;
    }

    protected void setNumJob(int currentJob) {
        this.numJob = currentJob;
    }

    public void checkNum(){
        RestApiServer restApiServer = new RestApiServer();

        Document doc = restApiServer.getService();
        List<Element> document = doc.getRootElement().elements("job");
        numJob = document.size();
        info("На сервере всего Job - " + getNumJob());
        info("Состояние сохранено");
    }
}
