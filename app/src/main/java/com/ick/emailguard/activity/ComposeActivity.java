package com.ick.emailguard.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.ick.emailguard.R;

/**
 * Created by Edo Firmansyah on 01/12/2017.
 */

public class ComposeActivity extends AppCompatActivity implements View.OnClickListener {

    String email, subject, message, attachmentFile;
    Uri URI = null;
    private static final int PICK_FROM_GALLERY = 101;
    int columnIndex;

    //Declaring EditText
    private EditText editTextFrom;
    private EditText editTextEmail;
    private EditText editTextSubject;
    private EditText editTextMessage;
    private String strFrom;
    // TextView txtp;
    //private String strEK;

    //Send button
    //private Button buttonSend, btnAttachment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_compose);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Tulis Email");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Bundle b = getIntent().getExtras();
        strFrom = b.getString("strFrom");
        //Initializing the views
        editTextFrom = (EditText) findViewById(R.id.txt_compose_from);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextSubject = (EditText) findViewById(R.id.editTextSubject);
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);
        //txtp = (TextView) findViewById(R.id.tes);
        editTextFrom.setText(strFrom);

        //btnAttachment = (Button) findViewById(R.id.btnAttachment);
        //buttonSend = (Button) findViewById(R.id.buttonSend);

        //Adding click listener

        //buttonSend.setOnClickListener(this);
        //btnAttachment.setOnClickListener(this);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {
                /**
                 * Get Path
                 */
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                attachmentFile = cursor.getString(columnIndex);
                //Log.e("Attachment Path:", attachmentFile);
                URI = Uri.parse(attachmentFile);
                //Log.d("Path:",String.valueOf(URI));
                cursor.close();
            }
        }catch (Exception e){

        }
    }

    private void sendEmail() {
        //Getting content for email
        String email = editTextEmail.getText().toString().trim();
        String subject = editTextSubject.getText().toString().trim();

        String message1 = editTextMessage.getText().toString();
        String attachment = String.valueOf(URI);

        SendMail sm = new SendMail(this, email, subject, message, attachment);

        //Executing sendmail to send email
        sm.execute();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // TODO: method untuk item menu toolbar compose, lalu ganti nilai return -> true
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.compose, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View view) {

    }
}
