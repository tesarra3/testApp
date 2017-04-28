package radek.tesar.ab.Client.request;

import com.google.gson.JsonParseException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import radek.tesar.ab.Client.entity.Account;
import radek.tesar.ab.Client.entity.ListTrans;
import radek.tesar.ab.Client.parser.DetailTransParser;
import radek.tesar.ab.Client.parser.ListOfTransactionParser;
import radek.tesar.ab.Client.response.Response;

/**
 * Created by tesar on 28.04.2017.
 */

public class GetDetailTransactionRequest extends Request
{
    private static final String REQUEST_METHOD = "GET";
    private static final String REQUEST_PATH = "http://demo0569565.mockable.io/transactions/";
    private static final int EXPECTED_OK_RESPONSE_CODE = 200;
    private int mID;

    public GetDetailTransactionRequest(int id) {
        mID = id;

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
        try {
            builder.append(URLEncoder.encode(String.valueOf(mID),CHARSET));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return builder.toString();
    }


    @Override
    public Response<Account> parseResponse(InputStream stream) throws IOException, JsonParseException
    {
        return new DetailTransParser().parse(stream);
    }


    @Override
    public byte[] getContent()
    {

        return null;
    }
}
