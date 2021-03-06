package at.ac.univie.team8.doctoranswers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    public static final String SHARED_PREF_NAME = "LoginData";
    public static final String SHARED_PREF_NAME_2 = "DoctorData";

    boolean userLoggedIn;

    EditText usernameText;
    String usernameStr;

    EditText psswdText;
    String psswdStr;

    public void initialize(){
        usernameText = (EditText) findViewById(R.id.editText);
        usernameStr = "";

        psswdText = (EditText) findViewById(R.id.editText2);
        psswdStr = "";

        userLoggedIn = false;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    public void loginButton(View view) {

        usernameStr = usernameText.getText().toString();
        psswdStr = psswdText.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if(success){
                        String name = jsonResponse.getString("name");
                        String email = jsonResponse.getString("email");
                        int age = jsonResponse.getInt("age");
                        int userID = jsonResponse.getInt("user_id");

                        SharedPreferences sp = MainActivity.this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putBoolean("logedIn",true);
                        editor.putString("userID", userID+"");
                        editor.putString("username", usernameStr);
                        editor.putString("password", psswdStr);
                        editor.putString("name", name);
                        editor.putString("email", email);
                        editor.putString("age", age+"");

                        editor.commit();

                        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                        startActivity(intent);

                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Login Failed").setNegativeButton("Retry", null).create().show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        LoginRequest loginRequest = new LoginRequest(usernameStr, psswdStr, responseListener);
        RequestQueue loginQueue = Volley.newRequestQueue(this);
        loginQueue.add(loginRequest);

    }

    public void registerButton(View view) {

        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

}
