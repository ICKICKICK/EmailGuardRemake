package com.ick.emailguard.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

/**
 * Created by Edo Firmansyah on 02/12/2017.
 */

class DeleteEmail extends AsyncTask<Void,Void,Void> {

    private static String id;
    private final String hostdelete;
    private final String password;
    private final String user;
    private Context context;
    private ProgressDialog progressDialog;

    public DeleteEmail(Context context, String ID, String host, String mailStoreType, String username, String password) {
        this.context = context;
        this.id = ID;
        this.hostdelete = host;
        this.user = username;
        this.password = password;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context, "Deleting message", "Please wait...", false, false);
        //Showing progress dialog while sending email

    }
    @Override
    protected void onPostExecute(Void Void) {
        super.onPostExecute(Void);
        //Dismissing the progress dialog
        progressDialog.dismiss();

        //Showing a success message
        Toast.makeText(context,"Message Deleted",Toast.LENGTH_LONG).show();
    }
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            // get the session object
            Properties properties = new Properties();
            properties.put("mail.store.protocol", "imap");
            properties.put("mail.imaps.host", hostdelete);
            properties.put("mail.imaps.port", "993");
            properties.put("mail.imap.starttls.enable", "true");
            Session emailSession = Session.getInstance(properties);
            // emailSession.setDebug(true);

            // create the POP3 store object and connect with the pop server
            Store store = emailSession.getStore("imaps");
            String number = id;
            store.connect(hostdelete, user, password);

            // create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_WRITE);

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    System.in));
            // retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.getMessages();
            System.out.println("messages.length---" + messages.length);
            for (int i = 0; i < messages.length; i++) {
                Message message = messages[i];
                System.out.println("---------------------------------");
                System.out.println("Email Number " + (i + 1));
                System.out.println("ID:" + message.getMessageNumber());
                System.out.println("Subject: " + message.getSubject());
                System.out.println("From: " + message.getFrom()[0]);

                String subject = message.getSubject();
                int ID = message.getMessageNumber();
                if (number.equals(String.valueOf(ID))) {
                    // set the DELETE flag to true
                    message.setFlag(Flags.Flag.DELETED, true);
                    System.out.println("Marked DELETE for message: " + subject);
                    break;
                }
            }
            // expunges the folder to remove messages which are marked deleted
            emailFolder.close(true);
            store.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;

    }
}
