package radek.tesar.ab.Client;


import radek.tesar.ab.Client.response.Response;

public interface APICallListener
{
    public void onAPICallRespond(APICallTask task, ResponseStatus status, Response<?> response);

    public void onAPICallFail(APICallTask task, ResponseStatus status, Exception exception);
}
