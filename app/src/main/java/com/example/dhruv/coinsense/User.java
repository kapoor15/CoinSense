package com.example.dhruv.coinsense;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dhruv on 2/24/18.
 */

public class User {

    String crypto1;
    String crypto2;
    String crypto3;
    String email;

    User(String email, String crypto1, String crypto2, String crypto3)
    {
        this.email = email;
        this.crypto1 = crypto1;
        this.crypto2 = crypto2;
        this.crypto3 = crypto3;
    }

    public Map<String, Object> toMap()
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("crypto1", crypto1);
        result.put("crypto2", crypto2);
        result.put("crypto3", crypto3);

        return result;
    }

}
