/*
 * Copyright (c) 2014-2016 Andreas Lianos
 * Licensed under the MIT Liecense (LICENSE.txt).
 * Author Andreas Lianos
 */
package tools;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class GenericTools {

    /**
     * It merges the two given object in a new one. If the same variables
     * overlap, the ones of b object are used. The original items are
     * unaffected.
     *
     * @param a
     * @param b
     * @return
     */
    public static JsonObject merge(JsonObject a, JsonObject b) {
        JsonObject merged = new JsonObject();
        for (Map.Entry<String, JsonElement> entry : a.entrySet()) {
            merged.add(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, JsonElement> entry : b.entrySet()) {
            merged.add(entry.getKey(), entry.getValue());
        }
        return merged;
    }
}
