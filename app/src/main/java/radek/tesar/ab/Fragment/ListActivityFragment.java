package radek.tesar.ab.Fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import radek.tesar.ab.Activity.DetailActivity;
import radek.tesar.ab.Adapter.ListTransactionAdapter;
import radek.tesar.ab.App;
import radek.tesar.ab.Cache.Cache;
import radek.tesar.ab.Client.APICallListener;
import radek.tesar.ab.Client.APICallManager;
import radek.tesar.ab.Client.APICallTask;
import radek.tesar.ab.Client.ResponseStatus;
import radek.tesar.ab.Client.entity.ListTrans;
import radek.tesar.ab.Client.entity.Transaction;
import radek.tesar.ab.Client.request.GetListOfTransactionRequest;
import radek.tesar.ab.Client.response.Response;
import radek.tesar.ab.R;
import radek.tesar.ab.Utils.NetworkingUtils;

/**
 * A placeholder fragment containing a simple view.
 */
public class ListActivityFragment extends BaseFragment implements APICallListener,ListTransactionAdapter.OnItemClick, AdapterView.OnItemSelectedListener {

    protected static APICallManager mAPICallManager;
    private RecyclerView list;
    SwipeRefreshLayout  swipe;
    Spinner spinner;
    public ListActivityFragment() {
    }


    @Override
    public void handleContent(View view) {
         list = (RecyclerView) view.findViewById(R.id.list);

        spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.transaction_direct, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        makeAPi();

         swipe = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swipe.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        spinner.setSelection(0);
                        makeAPi();
                    }
                }
        );

    }

    @Override
    public int mainView() {
        return R.layout.fragment_list;
    }

    private void makeAPi(){
        if (NetworkingUtils.isOnline()) {
            mAPICallManager = new APICallManager();
            GetListOfTransactionRequest request = new GetListOfTransactionRequest();
            mAPICallManager.executeTask(request, ListActivityFragment.this);
        } else {
            showoffline();
        }
    }

    @Override
    public void onAPICallRespond(APICallTask task, ResponseStatus status, Response<?> response) {
        if (task.getRequest().getClass().equals(GetListOfTransactionRequest.class)) {
            handleTransactionResponse((Response<ListTrans>) response, task, status);
        }
        mAPICallManager.finishTask(task);
    }

    private void handleTransactionResponse(Response<ListTrans> response, APICallTask task, ResponseStatus status) {
        if (response.isError()) {
            App.log("Request.onAPICallFail(Request): " + status.getStatusCode() + " " + status.getStatusMessage() +
                    " / error / " + response.getErrorType() + " / " + response.getErrorMessage());


            // handle error
            Toast.makeText(App.getContext(), response.getErrorMessage(), Toast.LENGTH_LONG).show();
            showEmptyList();
        }

        // response
        else {
            App.log("Request.onAPICallFail(Request): " + status.getStatusCode() + " " + status.getStatusMessage());

            // get data


            if (task.getRequest().getClass().equals(GetListOfTransactionRequest
                    .class)) {
                final ListTrans res = response.getResponseObject();
                if (res.getItems().isEmpty()) {
                    showEmptyList();
                }else{

                    makeAdapter(res.getItems());
                    showContent();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Cache.putItems(res.getItems());
                        }
                    }).start();
                }

            }
        }
    }

    @Override
    public void onAPICallFail(APICallTask task, ResponseStatus status, Exception exception) {
        showEmptyList();
    }

    private void makeAdapter(List<Transaction> items) {

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        list.setLayoutManager(manager);

        ListTransactionAdapter adapter = new ListTransactionAdapter(items,ListActivityFragment.this);
        list.setAdapter(adapter);
        swipe.setRefreshing(false);
    }

    private void updateAdapter(List<Transaction> items){
        ListTransactionAdapter adapter = (ListTransactionAdapter)list.getAdapter();
        if(adapter != null) {
            adapter.changeDataSet(items);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItem(Transaction transId) {
        Intent intent = new Intent(getActivity().getBaseContext(), DetailActivity.class);
        intent.putExtra(DetailActivity.TAG, transId);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String[] values = App.getContext().getResources().getStringArray(R.array.transaction_direct_value);
        updateAdapter(Cache.getItems(values[position]));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
