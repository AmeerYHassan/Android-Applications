package com.example.hangman.data;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.hangman.data.model.LoggedInUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    private LoggedInUser user;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void login(String username, String password, Consumer<Result<LoggedInUser>> success, Consumer<Result.Error> error) {
        try {
            AndroidNetworking.post("https://class.whattheduck.app/api/login")
                    .addBodyParameter("email", username)
                    .addBodyParameter("password", password)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .addHeaders("api-key", "DUDvegZNMhZcFYBqCy3N")
                    .build().getAsJSONObject((new JSONObjectRequestListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(JSONObject response) {
                    Log.v("external login success", response.toString());
                    // do anything with response
                    try {
                        int status = response.getInt("status");

                        if(status == 200){
                            JSONObject userJO = response.getJSONObject("data");
                            String email = userJO.getString("email");
                            String username = userJO.has("displayName")?userJO.getString("displayName"):email;
                            String uid = userJO.getString("uid");
                            user = new LoggedInUser(uid, username, email);
                            success.accept(new Result.Success<>(user));
                        }
                        else{
                            JSONObject respError = response.getJSONObject("error");
                            error.accept(new Result.Error(new Exception(respError.getString("message")), respError.getString("code")));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        error.accept(new Result.Error(new Exception(e.getMessage()), "json-error"));
                    }
                }

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onError(ANError e) {
                    // handle error

                    Log.e("external login error", e.getErrorBody());

                    try {
                        JSONObject jo = new JSONObject(e.getErrorBody());
                        error.accept(new Result.Error(new Exception(jo.getJSONObject("error").getString("message")), jo.getJSONObject("error").getString("code")));
                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }
                }
            }));
        } catch (Exception e) {
            error.accept(new Result.Error(new IOException("Error registering", e), "unknown"));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void register(String email, String username, String password, Consumer<Result<LoggedInUser>> success, Consumer<Result.Error> error) {
        try {
            AndroidNetworking.post("https://class.whattheduck.app/api/register")
                    .addBodyParameter("username", username)
                    .addBodyParameter("email", email)
                    .addBodyParameter("password", password)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .addHeaders("api-key", "1234")
                    .build().getAsJSONObject((new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int status = response.getInt("status");
                        if(status == 200) {
                            user = new LoggedInUser(
                                    response.getJSONObject("data").getString("uid"), response.getJSONObject("data").getString("displayName"));
                            success.accept(new Result.Success<>(user));
                        }
                        else{
                            JSONObject respError = response.getJSONObject("error");
                            error.accept(new Result.Error(new Exception(respError.getString("message")), respError.getString("code")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        error.accept(new Result.Error(e, "json-error"));
                    }
                }

                @Override
                public void onError(ANError e) {
                    // handle error
                    error.accept(new Result.Error(new Exception(e.getErrorBody()), "an-error"));
                }
            }));

        } catch (Exception e) {
            Log.v("register", e.getMessage());
            error.accept(new Result.Error(new IOException("Error registering", e), "unknown"));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}