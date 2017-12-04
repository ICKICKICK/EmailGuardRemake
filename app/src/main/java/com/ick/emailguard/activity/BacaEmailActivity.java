package com.ick.emailguard.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ick.emailguard.R;
import com.ick.emailguard.fragment.InboxFragment;

public class BacaEmailActivity extends AppCompatActivity implements View.OnClickListener{

    public String strFrom;
    public String strSubject;
    public String strContent;
    public String strAttachment;
    public Boolean star;
    public String strDate;
    public String strID;
    TextView txtFrom;
    TextView txtSubject;
    TextView txtContent;
    TextView txtAttachment;
    TextView txtDate;
    TextView reply;
    TextView forward;
    TextView replyall;
    ImageButton unduhAttachment;
    CardView cvAttach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baca_email);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Email Guard");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        Bundle b = getIntent().getExtras();
        strID = b.getString("strID");
        strFrom = b.getString("strFrom");
        strSubject = b.getString("strSubject");
        strContent = b.getString("strContent");
        strAttachment = b.getString("strAttachment");
        Log.d("AStes", strAttachment);
        strDate = b.getString("strDate");
        star = b.getBoolean("");

        txtFrom = (TextView) findViewById(R.id.from);
        txtSubject = (TextView) findViewById(R.id.subject);
        txtContent = (TextView) findViewById(R.id.content);
        //starToggle = (ToggleButton) findViewById(R.id.tb_star_detail);
        txtAttachment = (TextView) findViewById(R.id.tv_attach);
        txtDate = (TextView) findViewById(R.id.date);
        reply = (TextView) findViewById(R.id.btnReply);
        forward = (TextView) findViewById(R.id.btnForward);
        replyall = (TextView) findViewById(R.id.textSMS);
        unduhAttachment = (ImageButton) findViewById(R.id.btnDownload);
        txtFrom.setText(String.valueOf(strFrom));
        txtSubject.setText(strSubject);
        txtContent.setText(Html.fromHtml(strContent));
        txtAttachment.setText(Html.fromHtml(strAttachment));
        cvAttach = (CardView) findViewById(R.id.cv_attach);
        if (txtAttachment.getText().toString().isEmpty()){
            cvAttach.setVisibility(View.GONE);
        } else {
            cvAttach.setVisibility(View.VISIBLE);
        }
        try{
            txtDate.setText(String.valueOf(strDate));}
        catch (Exception e){
            Log.d("Errrorleng","error");
            Log.d("Errrorleng","error");
        }

        reply.setOnClickListener(this);
        forward.setOnClickListener(this);
        replyall.setOnClickListener(this);
        unduhAttachment.setOnClickListener(this);

    }

    public String unduh() {
        final String m_chosenDir = "";
        boolean m_newFolderEnabled = true;


        DirectoryChooserDialog directoryChooserDialog = new DirectoryChooserDialog(BacaEmailActivity.this,
                new DirectoryChooserDialog.ChosenDirectoryListener() {
                    @Override
                    public void onChosenDir(String chosenDir) {
                        String m_chosenDir = chosenDir;
                        Log.d("Sundanesse", String.valueOf(m_chosenDir));
                        Toast.makeText(
                                getApplicationContext(), "Chosen directory: " +
                                        chosenDir, Toast.LENGTH_LONG).show();
                        Log.d("Sundanesse2", m_chosenDir);
                        String ID = strID;
                        String host = InboxFragment.imaphost;// change accordingly
                        String mailStoreType = "imap";
                        String username = InboxFragment.email;// change accordingly
                        String password = InboxFragment.pass;// change accordingly
                        Log.d("Directbos", m_chosenDir);
                        Log.d("Directbos", strID);
                        if (!TextUtils.isEmpty(m_chosenDir)) {
                            EmailAttachmentReceiver receiver = new EmailAttachmentReceiver(BacaEmailActivity.this, ID, host, mailStoreType, username,
                                    password, m_chosenDir);
                            receiver.execute();
                        }
                    }

                });

        // Toggle new folder button enabling
        directoryChooserDialog.setNewFolderEnabled(m_newFolderEnabled);
        // Load directory chooser dialog for initial 'm_chosenDir' directory.
        // The registered callback will be called upon final directory selection.
        directoryChooserDialog.chooseDirectory(m_chosenDir);
        m_newFolderEnabled = !m_newFolderEnabled;

        //String m_chosenDir;

        return null;
    }

    public void Delete(){
        String ID = strID;
        String host = InboxFragment.imaphost;// change accordingly
        String mailStoreType = "imap";
        String username = InboxFragment.email;// change accordingly
        String password = InboxFragment.pass;// change accordingly
        DeleteEmail dm = new DeleteEmail(this, ID, host, mailStoreType, username, password);
        dm.execute();
    }
    @Override
    public void onRestart(){
        super.onRestart();
        Intent myIntent = new Intent(BacaEmailActivity.this, InboxFragment.class);
        startActivity(myIntent);
        this.finish();
    }
    @Override
    public void onClick(View view) {
        try {
            if (view == reply) {
                Intent myIntent = new Intent(BacaEmailActivity.this, ReplyMailActivity.class);
                Bundle bun = new Bundle();
                bun.putString("strFrom", strFrom);
                bun.putString("strSubject", strSubject);
                bun.putString("strContent", strContent);
                myIntent.putExtras(bun);
                startActivity(myIntent);
            }
        }
        catch (Exception e){
            Log.d("error","error");
        }
        if (view == forward) {

            Intent myIntent = new Intent(BacaEmailActivity.this, ForwardMailActivity.class);
            Bundle bun = new Bundle();
            bun.putString("strFrom", strFrom);
            bun.putString("strSubject", strSubject);
            bun.putString("strContent", strContent);
            myIntent.putExtras(bun);
            startActivity(myIntent);
        }

//        if (view == delete) {
//            Delete();
//            onRestart();
//        }
        if (view == unduhAttachment) {
            unduh();
            //unduh();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // TODO: method untuk item menu toolbar compose, lalu ganti nilai return -> true
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.hapus:
                Delete();
                onRestart();
                return true;
            default:
                return false;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.compose, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
