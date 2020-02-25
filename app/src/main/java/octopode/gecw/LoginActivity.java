package octopode.gecw;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;



public class LoginActivity extends AppCompatActivity
{
    String user, pass;
    CircularProgressButton loginBtn;
    TextView loginError;
    EditText username,password;
    SharedPreferences UserData;
    String stat;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginBtn=findViewById(R.id.login_button);
        username=findViewById(R.id.username_entry);
        password=findViewById(R.id.password_entry);
        loginError=findViewById(R.id.login_error);
        UserData=getSharedPreferences("login",MODE_PRIVATE);

        username.setFilters(new InputFilter[] {new InputFilter.AllCaps()});


        if(UserData.getBoolean("isLogin", false))
        {
            Toast.makeText(getApplicationContext(),"Already logged in",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, Dashboard.class));
            finish();
        }


        loginBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                loginBtn.startAnimation();
                user=username.getText().toString();
                pass=password.getText().toString();
                sendLoginData();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {
            case (5) : {
                if (resultCode == Activity.RESULT_OK) {
                    String newStudentID = data.getStringExtra("STUDENT_ID");
                    username.setText(newStudentID);
                }
                break;
            }
        }
    }

    private void sendLoginData()
    {
        RequestQueue requestQueue;
        String base = getString(R.string.base_url);
        String url=base+"api/signin.php";
        requestQueue = Volley.newRequestQueue(this);
        JSONObject signinObject = new JSONObject();
        try {
            signinObject.put("user", user);
            signinObject.put("passwd", pass);
        } catch (Exception e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, signinObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try {
                            stat = response.getString("Status");
                            loginBtn.revertAnimation();
                            if (stat.equals("Success")) {
                                UserData.edit().putBoolean("isLogin", true).apply();
                                UserData.edit().putString("Student_ID", user).apply();
                                Toast.makeText(getApplicationContext(),"Logged success",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, Dashboard.class));
                                finish();
                            } else {
                                loginError.setText(stat);
                                loginError.setVisibility(View.VISIBLE);
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loginBtn.revertAnimation();
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjReq);
    }




    @Override
    public void onDestroy()
    {
        super.onDestroy();
        loginBtn.dispose();
    }
}