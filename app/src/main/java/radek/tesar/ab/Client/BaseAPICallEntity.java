package radek.tesar.ab.Client;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

public class BaseAPICallEntity
{

    @SerializedName("message")
    private String mMessage;
    @SerializedName("errors")
    private JsonElement mErrors;


    public String getMessage()
    {
        return mMessage;
    }


    public void setMessage(String message)
    {
        mMessage = message;
    }


    public JsonElement getErrors()
    {
        return mErrors;
    }


    public void setErrors(JsonElement errors)
    {
        mErrors = errors;
    }
}
