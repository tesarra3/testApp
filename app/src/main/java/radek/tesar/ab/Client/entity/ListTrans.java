package radek.tesar.ab.Client.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import radek.tesar.ab.Client.BaseAPICallEntity;

/**
 * Created by tesar on 28.04.2017.
 */

public class ListTrans extends BaseAPICallEntity implements Serializable{

    @SerializedName("items")
    @Expose
    private List<Transaction> items = new ArrayList<>();

    public List<Transaction> getItems() {
        return items;
    }

    public void setItems(List<Transaction> items) {
        this.items = items;
    }
}
