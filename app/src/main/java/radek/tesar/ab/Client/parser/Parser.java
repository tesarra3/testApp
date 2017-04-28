package radek.tesar.ab.Client.parser;

import com.google.gson.JsonParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import radek.tesar.ab.App;
import radek.tesar.ab.Client.BaseAPICallEntity;
import radek.tesar.ab.Client.response.Response;
import radek.tesar.ab.R;


public abstract class Parser<T extends BaseAPICallEntity>
{
    private static final String ns = null;

    protected abstract T getFromJson(BufferedReader reader) throws Exception;


    public Response<T> parse(InputStream stream) throws IOException, JsonParseException
    {
        Response<T> response;

        BufferedReader reader = null;
        T entity = null;
        try
        {
            reader = new BufferedReader(new InputStreamReader(stream));

            // parse JSON
            entity = getFromJson(reader);
        }
        //this can happen if the response is e.g. 404 and therefore the response is not parsable
        catch (Exception e)
        {
            //e.printStackTrace();
        }
        finally
        {
            if (reader != null)
                reader.close();
        }

        if (entity != null)
        {
            if(entity.getErrors() != null) {
                response = new Response<>();
                response.setError(true);
                response.setErrorList(ErrorResponseParser.parse(entity.getErrors()));
            }
            else if (entity.getMessage() != null)
            {
                response = new Response<>();
                response.setError(true);
                response.setErrorMessage(entity.getMessage());
            }
            else
            {
                response = new Response<>();
                response.setError(false);
                response.setResponseObject(entity);
            }

        }
        else
        {
            response = new Response<>();
            response.setError(true);
            response.setResponseObject(null);
            response.setErrorMessage(App.getContext().getString(R.string.global_general_error));
        }

        return response;
    }

    protected String streamToString(BufferedReader reader) {
        StringBuilder total = new StringBuilder();
        String line;
        try
        {
            while ((line = reader.readLine()) != null) {
                total.append(line);
            }
        }
        catch (IOException e)
        {
            //e.printStackTrace();
        }
        return total.toString();
    }





}
