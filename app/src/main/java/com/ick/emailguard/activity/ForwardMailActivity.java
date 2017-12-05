package com.ick.emailguard.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ick.emailguard.R;

public class ForwardMailActivity extends AppCompatActivity implements View.OnClickListener {
    public String strFrom, strSender;
    public String strSubject;
    public String strContent;
    String email, subject, message, attachmentFile;
    Uri URI = null;
    private static final int PICK_FROM_GALLERY = 101;
    int columnIndex;

    //Declaring EditText
    private EditText editTextEmail, editTextSender;
    private EditText editTextSubject;
    private EditText editTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forward_mail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_compose);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Teruskan");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //Initializing the views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextSender = findViewById(R.id.txt_reply_from);
        editTextSubject = (EditText) findViewById(R.id.editTextSubject);
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);

        String GarisBatas = "-------------Forwarded Message-------------";
        Bundle b = getIntent().getExtras();
        strFrom = b.getString("strFrom");
        strSubject = b.getString("strSubject");
        strContent = b.getString("strContent");
        strSender = b.getString("strEmail");
        editTextSubject.setText("FWD:" + String.valueOf(strSubject));
        editTextSender.setText(String.valueOf(strSender));
        editTextMessage.setText(Html.fromHtml("<br>" + "<br>" + "<b>" + GarisBatas + "<br>" + strContent + "</b>"));

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {
            /**
             * Get Path
             */
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            attachmentFile = cursor.getString(columnIndex);
            Log.e("Attachment Path:", attachmentFile);
            URI = Uri.parse(attachmentFile);
            Log.d("Path:",String.valueOf(URI));
            cursor.close();
        }
    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("return-data", true);
        startActivityForResult(
                Intent.createChooser(intent, "Complete action using"),
                PICK_FROM_GALLERY);

    }

    private void sendEmail() {
        //Getting content for email
        String email = editTextEmail.getText().toString().trim();
        String subject = editTextSubject.getText().toString().trim();
        String message = editTextMessage.getText().toString().trim();
        String attachment = String.valueOf(URI);

        //Creating SendMail object
        SendMail sm = new SendMail(this, email, subject, message, attachment);

        //Executing sendmail to send email
        sm.execute();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.compose, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // TODO: method untuk item menu toolbar compose, lalu ganti nilai return -> true
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
            case R.id.Attachment:
                openGallery();
                return true;
            case R.id.Send:
                sendEmail();
                return true;
            case R.id.compose_menu:
//                openComposeSettings();
                return false;
            default:
                return false;
        }
    }



    @Override
    public void onClick(View v) {

    }
}
