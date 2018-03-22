package com.example.dhruv.coinsense;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by Dhruv on 3/21/18.
 */

public class FileRead {


    private Context ctxt;
    private static FileRead f1;
    private HashMap<String, Integer> toReturn;
    FileRead(Context ctxt)
    {
        this.ctxt = ctxt;
         this.toReturn= new HashMap<String, Integer>();
    }

    static FileRead getInstance(Context ctxt)
    {
        if(f1 == null) {

            FileRead f = new FileRead(ctxt);
            return f;
        }
        else
            return f1;
    }

   public void reader() {
       System.out.println("PRINTHEREEE");
       InputStream is = null;
       try {
           is = this.ctxt.getAssets().open("AFINN-111.txt");
           if(is == null)
               System.out.println("LOOOOOOLLLLLLLLLL");
       } catch (IOException e) {
           e.printStackTrace();
       }

       BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = "" ;
       try {
           while((line = br.readLine()) != null)
           {
               line.replaceAll(" ", "\t");
               String[] arr = line.split("\t");

               String word = arr[0];
               int id = Integer.parseInt(arr[1]);

               toReturn.put(word, id);


           }
       } catch (IOException e) {
           e.printStackTrace();
       }
   }

   public HashMap<String, Integer> get()
   {
       return toReturn;
   }


}
