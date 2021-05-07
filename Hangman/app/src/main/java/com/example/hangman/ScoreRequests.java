package com.example.hangman;

import android.content.SharedPreferences;
import android.os.StrictMode;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.ANResponse;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ScoreRequests {
    SharedPreferences sharedPreferences;
    ExecutorService executor = Executors.newFixedThreadPool(10);
    ScheduledExecutorService service;

    public static void sendScore(int score){
        SharedPreferences sharedPreferences = MainActivity.getContextOfApplication().getSharedPreferences("username", MainActivity.getContextOfApplication().MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid","error");

        ANRequest.PostRequestBuilder req = AndroidNetworking.post("https://class.whattheduck.app/api/saveScore");
        req.addBodyParameter("appKey", "com.example.hangman");
        req.addBodyParameter("uid", uid);
        req.addBodyParameter("score", String.valueOf(score));
        req.addBodyParameter("maxScore", "18");
        req.addHeaders("api-key", "DUDvegZNMhZcFYBqCy3N")
                .build().getAsJSONObject((new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 200) {
                        Log.v("ScoreRequests", "Score request successful");
                        //TODO: change response handling from Score to Stats
                        JSONObject scoreJO = (JSONObject) response.getJSONObject("data");
                        Log.v("ScoreDatasource", scoreJO.toString());
                        HashMap<String, Integer> map = new HashMap<>();
                        JSONArray plays = scoreJO.getJSONArray("plays");
                        for(int i = 0; i < plays.length(); i++){
                            JSONObject play = plays.getJSONObject(i);
                            String key = play.keys().next();
                            int value = play.getInt(key);
                            map.put(key, value);
                            Log.v("ScoreSent", play.keys().next());
                        }
                    } else {
                        JSONObject respError = response.getJSONObject("error");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {

            }
        }));
    }

    public static ArrayList<String> getScores(String scoreType, Boolean personalBest) throws JSONException {
        SharedPreferences sharedPreferences = MainActivity.getContextOfApplication().getSharedPreferences("username", MainActivity.getContextOfApplication().MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid","error");
        ArrayList<String> playerList = new ArrayList<>();
        ANRequest request = null;

        if (!personalBest){
            request = AndroidNetworking.get("https://class.whattheduck.app/api/getScores")
                    .addQueryParameter("appKey", "com.example.hangman")
                    .addQueryParameter("type", scoreType)
                    .addHeaders("api-key", "DUDvegZNMhZcFYBqCy3N")
                    .build();
        } else {
            request = AndroidNetworking.get("https://class.whattheduck.app/api/getScores")
                    .addQueryParameter("appKey", "com.example.hangman")
                    .addQueryParameter("type", scoreType)
                    .addQueryParameter("uid", uid)
                    .addHeaders("api-key", "DUDvegZNMhZcFYBqCy3N")
                    .build();
        }

        ANResponse<JSONObject> response = request.executeForJSONObject();

        if(response.isSuccess()){
            JSONObject jsonObject = response.getResult();
            JSONArray scoreJOs = jsonObject.getJSONArray("data");

            for (int i=0; i<scoreJOs.length(); i++){
                JSONObject currObject = scoreJOs.getJSONObject(i);
                String currentString =
                        String.valueOf(i+1) + ": "
                        + String.valueOf(currObject.get("displayName"))
                        + " - " + String.valueOf(currObject.get("score"))
                        + " - " + String.valueOf(currObject.get("created"));
                playerList.add(currentString);
            }

            Log.v("NetworkingManSuccess", playerList.toString());
            return playerList;
        } else {
            ANError error = response.getError();
            playerList.add("Failed to retrieve results");
            Log.v("NetworkingManError", error.toString());
        }

        Log.v("NetworkingMan", "Executed");
        return playerList;
    }
}
