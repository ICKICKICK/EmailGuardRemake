package com.ick.emailguard.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.ick.emailguard.R;
import com.ick.emailguard.helper.BaseApp;
import com.ick.emailguard.helper.Helper;
import com.ick.emailguard.helper.SessionManager;

import java.util.HashMap;

public class LoginActivity extends BaseApp {

    SessionManager sessionManager;
    private EditText logEmail;
    private EditText logPassword; //provider;
    private Spinner provider;
    private Button btnSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logEmail = (EditText) findViewById(R.id.logEmail);
        logPassword = (EditText) findViewById(R.id.logPassword);
        provider = (Spinner) findViewById(R.id.logProvider);
        btnSet = (Button) findViewById(R.id.logbtnSet);

        Spinner spinner = (Spinner) findViewById(R.id.logProvider);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.provider,
                android.R.layout.simple_spinner_item);
        spinner.setPrompt("Gmail");

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        final String name = user.get(SessionManager.kunci_email);

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {

                SettingEmail();

            }
        });

    }

    private void SettingEmail() {
        logEmail.setError(null);
        logPassword.setError(null);
        /*check kebaradan teks*/
        if (Helper.isEmpty(logEmail)) {
            logEmail.setError("Email masih kosong");
            logEmail.requestFocus();
        } else if (Helper.isEmpty(logPassword)) {
            logPassword.setError("Password masih kosong");
            logPassword.requestFocus();
        } else {

            /*menampilkan progressbar saat mengirim data*/
            ProgressDialog pd = new ProgressDialog(context);
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.setInverseBackgroundForced(false);
            pd.setCanceledOnTouchOutside(false);
            pd.setTitle("Info");
            pd.setMessage("Login");
            pd.show();
            String mailSetting = logEmail.getText().toString();
            Log.d("Email", mailSetting);
            Log.d("password", logPassword.getText().toString());
            if(mailSetting!=null)
            {
                sessionManager.createSession(logEmail.getText().toString(),logPassword.getText().toString(),
                        provider.getSelectedItem().toString());
                startActivity(new Intent(context, MainActivity.class));
                pd.dismiss();
            }
            else
            {
                Helper.pesan(context, "Check UserName or Password");
            }
        }
    }

}
