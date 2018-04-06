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
    String p1;
    String p2;
    String p3;
    String email;

    User(String email, String crypto1, String crypto2, String crypto3)
    {
        this.email = email;
        this.crypto1 = crypto1;
        this.crypto2 = crypto2;
        this.crypto3 = crypto3;
        this.p1 = "0";
        this.p2 = "0";
        this.p3 = "0";
    }

    User(){

    }

    public Map<String, Object> toMap()
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("crypto1", crypto1);
        result.put("crypto2", crypto2);
        result.put("crypto3", crypto3);
        result.put("p1", p1);
        result.put("p2", p2);
        result.put("p3", p3);


        return result;
    }

}
