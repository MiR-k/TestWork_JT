package apicore;

import apicore.helpers.PropertiesResourceManager;
import okhttp3.*;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class RestApiServer {

    static final String PROPERTIES_FILE = "retrofit.properties";

    private static Retrofit retrofit;

    private static PropertiesResourceManager props;

    public RestApiServer()
    {
        retrofit = new Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
    }

    public Retrofit getRetrofit()
    {
        return retrofit;
    }

    private static void initProperties() {
        props = new PropertiesResourceManager(PROPERTIES_FILE);
    }

    public static String getBaseUrl(){
        initProperties();
        return System.getProperty("urlBase", props.getProperty("urlBase"));
    }

    /**
     *
     * @return Document org.dom4j
     * @throws Exception
     */
    public Document getService() {

        String currentURI = getBaseUrl() + "api/xml";
        Request request = new Request.Builder()
                .url(currentURI)
                .build();
        OkHttpClient client = new OkHttpClient();
        try {
            Response response = client.newCall(request).execute();
            ResponseBody owners = response.body();
            Document document = DocumentHelper.parseText(owners.string());
            return  document;
        } catch (Exception ex) {
            throw new RuntimeException("Ошибка get запроса", ex);
        }
    }

}
