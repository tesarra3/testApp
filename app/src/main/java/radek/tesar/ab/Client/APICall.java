package radek.tesar.ab.Client;

import com.google.gson.JsonParseException;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import radek.tesar.ab.App;
import radek.tesar.ab.Client.request.Request;
import radek.tesar.ab.Client.response.Response;
import radek.tesar.ab.R;



//import java.util.zip.GZIPInputStream;


public class APICall
{

    private Request mRequest = null;
    private APICallTask mAPICallTask = null;
    private Exception mException = null;
    private ResponseStatus mResponseStatus = new ResponseStatus();

    private HttpURLConnection mConnection = null;
    private HttpsURLConnection mConnections = null; // for SSL
    private OutputStream mRequestStream = null;
    private InputStream mResponseStream = null;


    public APICall(Request request)
    {
        mRequest = request;
    }


    public APICall(Request request, APICallTask task)
    {
        mRequest = request;
        mAPICallTask = task;
    }


    public Request getRequest()
    {
        return mRequest;
    }


    public Exception getException()
    {
        return mException;
    }


    public ResponseStatus getResponseStatus()
    {
        return mResponseStatus;
    }


    public void kill()
    {
        disconnect();
    }




    public Response execute()
    {

        if(mRequest.getHTTPS()){
            try {
                // disables Keep-Alive for all connections
                if (mAPICallTask != null && mAPICallTask.isCancelled()) return null;
                System.setProperty("https.keepAlive", "false");

                // new connection
                byte[] requestData = mRequest.getContent();
//            byte[] requestFile = mRequest.getFile();
//            boolean multiLine = requestFile != null;

//            SSLContext sslcontext = SSLContext.getInstance("TLSv1");
//            sslcontext.init(null,
//                    new X509TrustManager[]{new CertificateAuthorityTrustManager()},
//                    new SecureRandom());
//            SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(sslcontext.getSocketFactory());
//            HttpsURLConnection.setDefaultSSLSocketFactory(NoSSLv3Factory);
                URL url = new URL(mRequest.getAddress());
//            if(mRequest.getHTTPS()){

//            }else {
HostnameVerifier ssl = mRequest.getHostnameVerifier();


                    mConnections = (HttpsURLConnection) url.openConnection(); // for SSL



                if(ssl != null){
                    mConnections.setHostnameVerifier(ssl);
                    HostnameVerifier ssld = mConnections.getHostnameVerifier();
                    if(ssl.equals(ssld)){
                  App.log("Hostname set");
                    }
                }
//
////                // ssl connection properties

//            }
                // connection properties
                if (mRequest.getRequestMethod() != null) {
                    mConnections.setRequestMethod(mRequest.getRequestMethod());// GET, POST, OPTIONS, HEAD, PUT, DELETE, TRACE

                }
                mConnections.setRequestProperty("Accept", "application/json");
//
//            //mConnection.setRequestProperty("Accept-Encoding", "gzip");
                mConnections.setRequestProperty("Accept-Charset", "UTF-8");
                if(mRequest.getToken() != null) {
                    mConnections.setRequestProperty("Authorization",mRequest.getToken());
                }


                //mConnection.setRequestProperty("Content-Length", requestData == null ? "0" : String.valueOf(requestData.length));
                //if(requestData!=null) mConnection.setChunkedStreamingMode(0);

                App.log("APICall.connection.getRequestMethod(): " + mConnections.getRequestMethod());
                if (requestData != null)
                    mConnections.setFixedLengthStreamingMode(requestData.length);
                mConnections.setConnectTimeout(mRequest.getTimeout());
                mConnections.setReadTimeout(mRequest.getTimeout());
                if (requestData != null) {
                    // this call automatically sets request method to POST on Android 4
                    // if you don't want your app to POST, you must not call setDoOutput
                    // http://webdiary.com/2011/12/14/ics-get-post/
                    mConnections.setDoOutput(true);
                }

                App.log("APICall.connection.getRequestMethod(): " + mConnections.getRequestMethod());
                mConnections.setDoInput(true);
                mConnections.setUseCaches(false);
                mConnections.connect();

                // send request
                if (mAPICallTask != null && mAPICallTask.isCancelled()) return null;
                if (requestData != null) {

                    mRequestStream = new DataOutputStream(mConnections.getOutputStream());// OutputStream(mConnection.getOutputStream());
                    mRequestStream.write(requestData);
                    mRequestStream.flush();
                    mRequestStream.close();


                }

                App.log("APICall.connection.getRequestMethod(): " + mConnections.getRequestMethod());

                // receive response
                if (mAPICallTask != null && mAPICallTask.isCancelled()) return null;
                String encoding = mConnections.getHeaderField("Content-Encoding");
                //boolean gzipped = encoding != null && encoding.toLowerCase().contains("gzip");
                try {
                    InputStream inputStream = mConnections.getInputStream();
//                if (gzipped)
//                    mResponseStream = new BufferedInputStream(new GZIPInputStream(inputStream));
//                else
                    mResponseStream = new BufferedInputStream(inputStream);
                } catch (FileNotFoundException e) {
                    // error stream
                    InputStream errorStream = mConnections.getErrorStream();
//                if (gzipped)
//                    mResponseStream = new BufferedInputStream(new GZIPInputStream(errorStream));
//                else
                    mResponseStream = new BufferedInputStream(errorStream);
                }

//            response info
                App.log("APICall.connection.getRequestMethod(): " + mConnections.getRequestMethod());
                App.log("APICall.connection.getURL(): " + mConnections.getURL());
                App.log("APICall.connection.getContentType(): " + mConnections.getContentType());
                App.log("APICall.connection.getContentEncoding(): " + mConnections.getContentEncoding());
                App.log("APICall.connection.getResponseCode(): " + mConnections.getResponseCode());
                App.log("APICall.connection.getResponseMessage(): " + mConnections.getResponseMessage());

                // parse response
                if (mAPICallTask != null && mAPICallTask.isCancelled()) return null;
                Response<?> response = mRequest.parseResponse(mResponseStream);
                if (response == null) throw new RuntimeException("Parser returned null response");

                if (mAPICallTask != null && mAPICallTask.isCancelled()) return null;

                if (mConnections.getResponseCode() != getRequest().getExpectedOkResponseCode() && response.getErrorMessage() == null && response.getErrorList() == null) {
                    response.setErrorMessage(App.getContext().getString(R.string.global_general_error));
                    response.setError(true);
                }

                return response;
            } catch (UnknownHostException e) {
                mException = e;
                //e.printStackTrace();
//            Toast.makeText(App.getContext(),R.string.global_general_error,Toast.LENGTH_SHORT).show();
                return null;
            } catch (FileNotFoundException e) {
                mException = e;
               // e.printStackTrace();
//            Toast.makeText(App.getContext(),R.string.global_general_error,Toast.LENGTH_SHORT).show();
                return null;
            } catch (SocketException e) {
                mException = e;
               // e.printStackTrace();

//            Toast.makeText(App.getContext(),R.string.global_general_error,Toast.LENGTH_SHORT).show();
                return null;
            } catch (SocketTimeoutException e) {
                mException = e;
               // e.printStackTrace();
//            Toast.makeText(App.getContext(),R.string.global_general_error,Toast.LENGTH_SHORT).show();
                return null;
            } catch (JsonParseException e) {
                mException = e;
               // e.printStackTrace();
//            Toast.makeText(App.getContext(),R.string.global_general_error,Toast.LENGTH_SHORT).show();
                return null;
            } catch (IOException e) {
                mException = e;
               // e.printStackTrace();
//            Toast.makeText(App.getContext(),R.string.global_general_error,Toast.LENGTH_SHORT).show();
                return null;
            } catch (Exception e) {
                mException = e;
                App.log(e.toString());
//            e.printStackTrace();
//            Toast.makeText(App.getContext(),R.string.global_general_error,Toast.LENGTH_SHORT).show();
                //Toast.makeText(App.getContext(), String.valueOf(e), Toast.LENGTH_LONG).show();
                return null;
            } finally {
                disconnect();
            }
        }else {
            try {
                // disables Keep-Alive for all connections
                if (mAPICallTask != null && mAPICallTask.isCancelled()) return null;
                System.setProperty("http.keepAlive", "false");

                // new connection
                byte[] requestData = mRequest.getContent();
//            byte[] requestFile = mRequest.getFile();
//            boolean multiLine = requestFile != null;


                URL url = new URL(mRequest.getAddress());

                mConnection = (HttpURLConnection) url.openConnection();

                // connection properties
                if (mRequest.getRequestMethod() != null) {
                    mConnection.setRequestMethod(mRequest.getRequestMethod());// GET, POST, OPTIONS, HEAD, PUT, DELETE, TRACE

                }
                mConnection.setRequestProperty("Accept", "application/json");
//
//            //mConnection.setRequestProperty("Accept-Encoding", "gzip");
                mConnection.setRequestProperty("Accept-Charset", "UTF-8");
                if(mRequest.getToken() != null) {
                    mConnection.setRequestProperty("Authorization",mRequest.getToken());
                }


                //mConnection.setRequestProperty("Content-Length", requestData == null ? "0" : String.valueOf(requestData.length));
                //if(requestData!=null) mConnection.setChunkedStreamingMode(0);

                App.log("APICall.connection.getRequestMethod(): " + mConnection.getRequestMethod());
                if (requestData != null)
                    mConnection.setFixedLengthStreamingMode(requestData.length);
                mConnection.setConnectTimeout(mRequest.getTimeout());
                mConnection.setReadTimeout(mRequest.getTimeout());
                if (requestData != null) {
                    // this call automatically sets request method to POST on Android 4
                    // if you don't want your app to POST, you must not call setDoOutput
                    // http://webdiary.com/2011/12/14/ics-get-post/
                    mConnection.setDoOutput(true);
                }

                App.log("APICall.connection.getRequestMethod(): " + mConnection.getRequestMethod());
                mConnection.setDoInput(true);
                mConnection.setUseCaches(false);
                mConnection.connect();

                // send request
                if (mAPICallTask != null && mAPICallTask.isCancelled()) return null;
                if (requestData != null) {

                    mRequestStream = new DataOutputStream(mConnection.getOutputStream());// OutputStream(mConnection.getOutputStream());
                    mRequestStream.write(requestData);
                    mRequestStream.flush();
                    mRequestStream.close();


                }

                App.log("APICall.connection.getRequestMethod(): " + mConnection.getRequestMethod());

                // receive response
                if (mAPICallTask != null && mAPICallTask.isCancelled()) return null;
                String encoding = mConnection.getHeaderField("Content-Encoding");
                //boolean gzipped = encoding != null && encoding.toLowerCase().contains("gzip");
                try {
                    InputStream inputStream = mConnection.getInputStream();
//                if (gzipped)
//                    mResponseStream = new BufferedInputStream(new GZIPInputStream(inputStream));
//                else
                    mResponseStream = new BufferedInputStream(inputStream);
                } catch (FileNotFoundException e) {
                    // error stream
                    InputStream errorStream = mConnection.getErrorStream();
//                if (gzipped)
//                    mResponseStream = new BufferedInputStream(new GZIPInputStream(errorStream));
//                else
                    mResponseStream = new BufferedInputStream(errorStream);
                }

//            response info
                App.log("APICall.connection.getRequestMethod(): " + mConnection.getRequestMethod());
                App.log("APICall.connection.getURL(): " + mConnection.getURL());
                App.log("APICall.connection.getContentType(): " + mConnection.getContentType());
                App.log("APICall.connection.getContentEncoding(): " + mConnection.getContentEncoding());
                App.log("APICall.connection.getResponseCode(): " + mConnection.getResponseCode());
                App.log("APICall.connection.getResponseMessage(): " + mConnection.getResponseMessage());

                // parse response
                if (mAPICallTask != null && mAPICallTask.isCancelled()) return null;
                Response<?> response = mRequest.parseResponse(mResponseStream);
                if (response == null) throw new RuntimeException("Parser returned null response");

                if (mAPICallTask != null && mAPICallTask.isCancelled()) return null;

                if (mConnection.getResponseCode() != getRequest().getExpectedOkResponseCode() && response.getErrorMessage() == null && response.getErrorList() == null) {
                    response.setErrorMessage(App.getContext().getString(R.string.global_general_error));
                    response.setError(true);
                }

                return response;
            } catch (UnknownHostException e) {
                mException = e;
                //e.printStackTrace();
//            Toast.makeText(App.getContext(),R.string.global_general_error,Toast.LENGTH_SHORT).show();
                return null;
            } catch (FileNotFoundException e) {
                mException = e;
                //e.printStackTrace();
//            Toast.makeText(App.getContext(),R.string.global_general_error,Toast.LENGTH_SHORT).show();
                return null;
            } catch (SocketException e) {
                mException = e;
               // e.printStackTrace();

//            Toast.makeText(App.getContext(),R.string.global_general_error,Toast.LENGTH_SHORT).show();
                return null;
            } catch (SocketTimeoutException e) {
                mException = e;
////            Toast.makeText(App.getContext(),R.string.global_general_error,Toast.LENGTH_SHORT).show();
                return null;
            } catch (JsonParseException e) {
                mException = e;
              //  e.printStackTrace();
//            Toast.makeText(App.getContext(),R.string.global_general_error,Toast.LENGTH_SHORT).show();
                return null;
            } catch (IOException e) {
                mException = e;
               // e.printStackTrace();
//            Toast.makeText(App.getContext(),R.string.global_general_error,Toast.LENGTH_SHORT).show();
                return null;
            } catch (Exception e) {
                mException = e;
                App.log(e.toString());
//            e.printStackTrace();
//            Toast.makeText(App.getContext(),R.string.global_general_error,Toast.LENGTH_SHORT).show();
                //Toast.makeText(App.getContext(), String.valueOf(e), Toast.LENGTH_LONG).show();
                return null;
            } finally {
                disconnect();
            }
        }
    }


    private void disconnect()
    {
        try
        {
            if (mRequestStream != null) mRequestStream.close();
        }
        catch (IOException e)
        {
        }

        try
        {
            if (mResponseStream != null) mResponseStream.close();
        }
        catch (IOException e)
        {
        }

        try
        {
            if (mConnections!= null)
            {
                mResponseStatus.setStatusCode(mConnections.getResponseCode());
                mResponseStatus.setStatusMessage(mConnections.getResponseMessage());
                mConnections.disconnect();
            }
            // set status
            if (mConnection != null)
            {
                mResponseStatus.setStatusCode(mConnection.getResponseCode());
                mResponseStatus.setStatusMessage(mConnection.getResponseMessage());
                mConnection.disconnect();
            }
        }
        catch (Throwable e)
        {
        }

        mRequestStream = null;
        mResponseStream = null;
        mConnection = null;
        mConnections =null;
    }


    private String getBasicAuthToken(String token)
    {
        return "Bearer " + token;
    }
}
