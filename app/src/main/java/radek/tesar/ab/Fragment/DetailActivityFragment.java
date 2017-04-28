package radek.tesar.ab.Fragment;

import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import radek.tesar.ab.Activity.DetailActivity;
import radek.tesar.ab.App;
import radek.tesar.ab.Cache.Cache;
import radek.tesar.ab.Client.APICallListener;
import radek.tesar.ab.Client.APICallManager;
import radek.tesar.ab.Client.APICallTask;
import radek.tesar.ab.Client.ResponseStatus;
import radek.tesar.ab.Client.entity.Account;
import radek.tesar.ab.Client.entity.ListTrans;
import radek.tesar.ab.Client.request.GetDetailTransactionRequest;
import radek.tesar.ab.Client.request.GetListOfTransactionRequest;
import radek.tesar.ab.Client.response.Response;
import radek.tesar.ab.Enum.Direction;
import radek.tesar.ab.R;
import radek.tesar.ab.Utils.NetworkingUtils;
import radek.tesar.ab.Utils.Utils;

/**
 * Created by tesar on 28.04.2017.
 */

public class DetailActivityFragment extends BaseFragment implements APICallListener {
    protected static APICallManager mAPICallManager;
    TextView accountNumber;
    TextView accountName;
    TextView bankCode;
    @Override
    public void handleContent(View view) {
        DetailActivity activity = (DetailActivity) getActivity();


        AppCompatImageView imageView = (AppCompatImageView) view.findViewById(R.id.image);
        if(activity.getTrans().getDirection().equals(Direction.incoming.toString())){
            imageView.setImageResource(R.drawable.ic_call_received);
        }else{
            imageView.setImageResource(R.drawable.ic_call_made);
        }


        TextView price = (TextView) view.findViewById(R.id.price);
        price.setText(Utils.convertWithoutRounding(activity.getTrans().getAmountInAccountCurrency()));
        TextView direct = (TextView) view.findViewById(R.id.direct);
        direct.setText(activity.getTrans().getDirection());

         accountNumber = (TextView) view.findViewById(R.id.account_number);
         accountName = (TextView) view.findViewById(R.id.account_name);
         bankCode = (TextView) view.findViewById(R.id.bank_code);

        makeAPi(activity.getTrans().getId());
    }


    @Override
    public int mainView() {
        return R.layout.fragment_detail;
    }


    private void makeAPi(int id){
        if (NetworkingUtils.isOnline()) {
            mAPICallManager = new APICallManager();
            GetDetailTransactionRequest request = new GetDetailTransactionRequest(id);
            mAPICallManager.executeTask(request, DetailActivityFragment.this);
        } else {
            showoffline();
        }
    }

    @Override
    public void onAPICallRespond(APICallTask task, ResponseStatus status, Response<?> response) {
        if (task.getRequest().getClass().equals(GetDetailTransactionRequest.class)) {
            handleDetailResponse((Response<Account>) response, task, status);
        }
        mAPICallManager.finishTask(task);
    }

    private void handleDetailResponse(Response<Account> response, APICallTask task, ResponseStatus status) {
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


            if (task.getRequest().getClass().equals(GetDetailTransactionRequest
                    .class)) {
                final Account res = response.getResponseObject();
                    fillView(res);
                    showContent();
            }
        }
    }

    private void fillView(Account res) {
        accountNumber.setText(res.getContraAccount().getAccountNumber());
        accountName.setText(res.getContraAccount().getAccountName());
        bankCode.setText(res.getContraAccount().getBankCode());
    }

    @Override
    public void onAPICallFail(APICallTask task, ResponseStatus status, Exception exception) {
        showEmptyList();
    }
}
