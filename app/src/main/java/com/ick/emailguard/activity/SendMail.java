package com.ick.emailguard.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.ick.emailguard.fragment.InboxFragment;
import com.ick.emailguard.helper.SessionManager;

import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Created by Edo Firmansyah on 01/12/2017.
 */

class SendMail extends AsyncTask<Void,Void,Void> {

    //Declaring Variables
    private Context context;
    private Session session;
    SessionManager sessionManager;
    //Information to send email
    private String email;
    private String subject;
    private String message;
    private String attachment;

    //private KeyPair key;
    //Progressdialog to show while sending email
    private ProgressDialog progressDialog;

    //Class Constructor
    public SendMail(Context context, String mail, String subj, String mess, String attach){
        //Initializing variables
        this.context = context;
        this.email = mail;
        this.subject = subj;
        this.message = mess;
        this.attachment = attach;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Showing progress dialog while sending email
        progressDialog = ProgressDialog.show(context,"Sending message","Please wait...",false,false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Dismissing the progress dialog
        progressDialog.dismiss();
        //Showing a success message
        Toast.makeText(context,"Message Sent",Toast.LENGTH_LONG).show();
    }

    @Override
    protected Void doInBackground(Void... params) {

        String smtphost = InboxFragment.host;
        Log.d ("SMTP HOST", String.valueOf(smtphost));
        Log.d ("SMTP mail", String.valueOf(InboxFragment.email));
        Log.d ("SMTP pass", String.valueOf(InboxFragment.pass));
        Log.d ("SMTP Penerima", String.valueOf(email));
        Log.d ("SMTP subjek", String.valueOf(subject));
        Log.d ("SMTP Content", String.valueOf(message));
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtphost);
        props.put("mail.smtp.port", "587");
        //Creating a new session
        session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(InboxFragment.email, InboxFragment.pass);
                    }
                });

        try {

            //Creating MimeMessage object
            if (attachment!= null ) {
                MimeMessage mm = new MimeMessage(session);

                //Setting sender address
                mm.setFrom(new InternetAddress(InboxFragment.email));
                //Adding receiver
                mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
                //Adding subject
                mm.setSubject(subject);
                //Adding message

                BodyPart messageBodyPart = new MimeBodyPart();
                BodyPart attachPart = new MimeBodyPart();

                // Now set the actual message
                messageBodyPart.setText(message);
                Log.d("PATH FIle:", attachment);
                // Create a multipar message

                Multipart multipart = new MimeMultipart();

                // Set text message part
                multipart.addBodyPart(messageBodyPart);

                // Part two is attachment
                messageBodyPart = new MimeBodyPart();
                String filename = attachment;
                Log.d("PATH FIle:", filename);
                DataSource source = new FileDataSource(filename);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(filename);
                multipart.addBodyPart(messageBodyPart);
                multipart.addBodyPart(attachPart);
                MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
                mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
                mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
                mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
                mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
                mc.addMailcap("message/rfc822;; x-java-content- handler=com.sun.mail.handlers.message_rfc822");

                // Send the complete message parts
                mm.setContent(multipart);
                Transport.send(mm);
            }

            Log.d("Tanpa Attachment", attachment);
            MimeMessage mm = new MimeMessage(session);
            //Log.d("celeng",enkrip);
            //Setting sender address
            mm.setFrom(new InternetAddress(InboxFragment.email));
            //Adding receiver
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            //Adding subject
            mm.setSubject(subject);
            //Adding message
            //Asymmetric asym = new Asymmetric();
            //String enkripmessage = asym.Encrypts(message);
            mm.setText(message);
            Transport.send(mm);

            //Sending email

            //Sending email


        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
