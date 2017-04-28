package radek.tesar.ab.Client.request;

import com.google.gson.JsonParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import radek.tesar.ab.Client.entity.ListTrans;
import radek.tesar.ab.Client.parser.ListOfTransactionParser;
import radek.tesar.ab.Client.response.Response;

/**
 * Created by tesar on 28.04.2017.
 */

public class GetListOfTransactionRequest extends Request
{
    private static final String REQUEST_METHOD = "GET";
    private static final String REQUEST_PATH = "http://demo0569565.mockable.io/transactions";
    private static final int EXPECTED_OK_RESPONSE_CODE = 200;


    public GetListOfTransactionRequest() {

    }

    @Override
    public String getRequestMethod()
    {
        return REQUEST_METHOD;
    }


    @Override
    public int getExpectedOkResponseCode()
    {
        return EXPECTED_OK_RESPONSE_CODE;
    }


    @Override
    public boolean getHTTPS() {
        return false;
    }

    @Override
    public String getAddress()
    {
        StringBuilder builder = new StringBuilder();


        builder.append(REQUEST_PATH);

        return builder.toString();
    }


    @Override
    public Response<ListTrans> parseResponse(InputStream stream) throws IOException, JsonParseException
    {
        return new ListOfTransactionParser().parse(stream);
    }


    @Override
    public byte[] getContent()
    {

        return null;
    }
}
