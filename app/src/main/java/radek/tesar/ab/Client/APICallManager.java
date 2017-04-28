package radek.tesar.ab.Client;

import android.os.AsyncTask;
import android.os.Build;

import java.util.LinkedList;

import radek.tesar.ab.App;
import radek.tesar.ab.Client.request.Request;


public class APICallManager
{

    private LinkedList<APICallTask> mTaskList = new LinkedList<APICallTask>();


    public APICallManager()
    {

    }


    public void executeTask(Request request, APICallListener listener)
    {
        executeTask(request, listener, 1);
    }


    public void executeTask(Request request, APICallListener listener, int maxAttempts)
    {
        APICallTask task = new APICallTask(request, listener, maxAttempts);
        mTaskList.add(task);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            // use AsyncTask.THREAD_POOL_EXECUTOR or AsyncTask.SERIAL_EXECUTOR
            try {
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } catch (Exception e) {

                //e.printStackTrace();
            }
        }
        else
        {
            task.execute();
        }
    }


    public boolean finishTask(APICallTask task)
    {
        return mTaskList.remove(task);
    }


    public int getTasksCount()
    {
        return mTaskList.size();
    }


    public boolean hasRunningTask(Class<?> requestClass)
    {
        String className = requestClass.getSimpleName();

        for (APICallTask task : mTaskList)
        {
            String taskName = task.getRequest().getClass().getSimpleName();
            if (className.equals(taskName)) return true;
        }

        return false;
    }


    public void cancelAllTasks()
    {
        for (int i = mTaskList.size() - 1; i >= 0; i--)
        {
            APICallTask task = mTaskList.get(i);
            if (task != null)
            {
                task.cancel(true);
                mTaskList.remove(task);
            }
        }
    }


    public void killAllTasks()
    {
        for (int i = mTaskList.size() - 1; i >= 0; i--)
        {
            APICallTask task = mTaskList.get(i);
            if (task != null)
            {
                task.kill();
                task.cancel(true);
                mTaskList.remove(task);
            }
        }
    }


    public void printRunningTasks()
    {
        for (APICallTask task : mTaskList)
        {
            App.log("APICallManager.printRunningTasks(): " + (task == null ? "null" : (task.getRequest().getClass().getSimpleName() + " / " + task.getStatus().toString())));
        }

        if (mTaskList.isEmpty()) App.log("APICallManager.printRunningTasks(): empty");
    }
}
