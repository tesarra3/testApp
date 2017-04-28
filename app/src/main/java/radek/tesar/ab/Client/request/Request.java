package radek.tesar.ab.Client.request;

import android.os.Bundle;

import com.google.gson.JsonParseException;

import java.io.IOException;
import java.io.InputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import radek.tesar.ab.Client.response.Response;


public abstract class Request
{

    public static final String CHARSET = "UTF-8";
    //public static final String BOUNDARY = "0xKhTmLbOuNdArY";

    private Bundle mMetaData = null;


    public abstract String getRequestMethod();

    public abstract int getExpectedOkResponseCode();

    public abstract String getAddress();

    public abstract Response<?> parseResponse(InputStream stream) throws IOException, JsonParseException;

    public byte[] getFile(){
        return null;
    }


    public byte[] getContent()
    {
        return null;
    }

    public int getTimeout(){return 30000;}


    public String  getPropertiesAccept()
    {
        return null;
    }


    public String getBasicAuthUsername()
    {
        return null;
    }


    public String getBasicAuthPassword()
    {
        return null;
    }
    public boolean getHTTPS(){
        return false;
    }


    public boolean isMultipart()
    {
        return false;
    }


    public Bundle getMetaData()
    {
        return mMetaData;
    }

    public HostnameVerifier getHostnameVerifier(){
        return null;
    }

    public SSLContext  getSSLContext (){
        return null;
    }

    public String getToken(){return null;}


    public void setMetaData(Bundle metaData)
    {
        mMetaData = metaData;
    }


}
