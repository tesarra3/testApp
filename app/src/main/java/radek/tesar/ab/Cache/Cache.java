package radek.tesar.ab.Cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import radek.tesar.ab.Client.entity.Transaction;

/**
 * Created by tesar on 28.04.2017.
 */

public class Cache {


    private static ConcurrentHashMap<String, List<Transaction>> sTrans = new ConcurrentHashMap<>();

    public static List<Transaction> getItems(String value){
        List<Transaction> res =  sTrans.get(value);
        if(res == null){
            res = new ArrayList<>();
        }
        return res;
    }

    public static void putItems(List<Transaction> LT){
        sTrans.clear();
        for(Transaction t: LT){
            addItems(t,t.getDirection().toString());
        }
        sTrans.put("ALL",LT);
    }

    public static void addItems(Transaction trans, String direction){
        List<Transaction> LT = getItems(direction);
        LT.add(trans);
        sTrans.put(direction,LT);
    }

}
