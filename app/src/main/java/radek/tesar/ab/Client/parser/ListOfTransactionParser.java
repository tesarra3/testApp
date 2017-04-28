package radek.tesar.ab.Client.parser;

import com.google.gson.Gson;

import java.io.BufferedReader;

import radek.tesar.ab.Client.entity.ListTrans;

/**
 * Created by tesar on 28.04.2017.
 */

public class ListOfTransactionParser extends Parser<ListTrans> {

    @Override
    protected ListTrans getFromJson(BufferedReader reader) throws IllegalStateException {
        // Gson gson = new Gson();
        Gson gson = new Gson();
        return gson.fromJson(reader, ListTrans.class);
    }
}
