package radek.tesar.ab.Client.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

import radek.tesar.ab.Enum.Direction;

/**
 * Created by tesar on 28.04.2017.
 */

public class Transaction implements Serializable{

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("amountInAccountCurrency")
    @Expose
    private BigDecimal amountInAccountCurrency;
    @SerializedName("direction")
    @Expose
    private String direction;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getAmountInAccountCurrency() {
        return amountInAccountCurrency;
    }

    public void setAmountInAccountCurrency(BigDecimal amountInAccountCurrency) {
        this.amountInAccountCurrency = amountInAccountCurrency;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
