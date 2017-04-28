package radek.tesar.ab.Client.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import radek.tesar.ab.Client.BaseAPICallEntity;

/**
 * Created by tesar on 28.04.2017.
 */

public class Account extends BaseAPICallEntity implements Serializable {
    @SerializedName("contraAccount")
    @Expose
    private ContraAccount contraAccount;

    public ContraAccount getContraAccount() {
        return contraAccount;
    }

    public void setContraAccount(ContraAccount contraAccount) {
        this.contraAccount = contraAccount;
    }
}
