package radek.tesar.ab.Client.parser;

import android.util.Pair;

import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ErrorResponseParser
{

    /**
     * parses the json error response that can have any name-value pairs (expected are value and an array of values)
     *
     * @param jsonElement the response json element
     * @return list of pairs that consist of name and list of errors that correspond to the value
     * e.g. "email" : ["empty field", "invalid format"]
     */
    public static List<Pair<String, List<String>>> parse(JsonElement jsonElement)
    {
        List<Pair<String, List<String>>> list = new ArrayList<>();

        try
        {
            JSONObject jsonRoot = new JSONObject(jsonElement.toString());
            Iterator<String> iterator = jsonRoot.keys();
            while (iterator.hasNext())
            {
                String key = iterator.next();

                List<String> valueList = new ArrayList<>();
                JSONArray jsonArray = jsonRoot.getJSONArray(key);

                for (int i=0; i<jsonArray.length(); i++) {
                    valueList.add(jsonArray.getString(i));
                }

                list.add(new Pair<>(key, valueList));
            }
        }
        catch (JSONException e)
        {
            //e.printStackTrace();
        }

        return list;
    }
}
