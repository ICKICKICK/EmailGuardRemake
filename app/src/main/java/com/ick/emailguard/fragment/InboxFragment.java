package com.ick.emailguard.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ick.emailguard.R;
import com.ick.emailguard.activity.ComposeActivity;
import com.ick.emailguard.helper.SessionManager;
import com.ick.emailguard.model.Mail;
import com.ick.emailguard.model.MailArrayAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;


public class InboxFragment extends Fragment {

    public static final String GMAIL = "smtp.gmail.com";
    public static final String YAHOO = "smtp.mail.yahoo.com";
    public static final String OUTLOOK = "smtp-mail.outlook.com";
    public static final String IMAPGMAIL = "imap.gmail.com";
    public static final String IMAPYAHOO = "imap.mail.yahoo.com";
    public static final String IMAPOUTLOOK = "imap-mail.outlook.com";
    public static String email;
    public static String pass;
    public static String host;
    public static String imaphost;
    SessionManager sessionManager;
    private List<Mail> mailList = new ArrayList<>();

    public InboxFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);

        sessionManager = new SessionManager(view.getContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        final String name = user.get(SessionManager.kunci_email);
        email = name;
        final String password = user.get(SessionManager.kunci_password);
        final String provider = user.get(SessionManager.kunci_provider);

        if (provider.equals("Gmail") || provider.equals("gmail")) {
            host = GMAIL;
            imaphost = IMAPGMAIL;
        } else if (provider.equals("Yahoo!") || provider.equals("Yahoo") || provider.equals("yahoo") || provider.equals("yahoo!")) {
            host = YAHOO;
            imaphost = IMAPYAHOO;
        } else if (provider.equals("Outlook") || provider.equals("outlook")) {
            host = OUTLOOK;
            imaphost = IMAPOUTLOOK;
        }
        pass = password;
        TextView txtprofil = (TextView) view.findViewById(R.id.label);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), ComposeActivity.class);
//                Bundle bun = new Bundle();
//                bun.putString("strFrom", email);
//                intent.putExtras(bun);
//                startActivity(intent);
//                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                //      .setAction("Action", null).show();
//            }
//        });

        final ListView lvwMail = (ListView) view.findViewById(R.id.mailBoxList);
        Log.d("sunda", email);
        Log.d("sunda2", pass);
        Log.d("sunda3", imaphost);
        try {
            String[][] result = new LoadMail().execute().get();
            lvwMail.setAdapter(new MailArrayAdapter(getContext(), result[0], result[1], result[2], result[3], result[4], result[5]));
            lvwMail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String strID = ((TextView) view.findViewById(R.id.id)).getText().toString();
                    String strFrom = ((TextView) view.findViewById(R.id.from)).getText().toString();
                    String strSubject = ((TextView) view.findViewById(R.id.subject)).getText().toString();
                    String strContent = ((TextView) view.findViewById(R.id.content)).getText().toString();
                    String strAttachment = ((TextView) view.findViewById(R.id.attachment)).getText().toString();
                    String strDate = ((TextView) view.findViewById(R.id.date)).getText().toString();
                    Log.d("tes", strDate);
//                    Intent myIntent = new Intent(getActivity(), BacaEmail.class);
                    Bundle bun = new Bundle();
                    // bun.putString("strID", strID);
                    bun.putString("strFrom", strFrom);
                    bun.putString("strSubject", strSubject);
                    bun.putString("strDate", strDate);
                    bun.putString("strContent", strContent);
                    bun.putString("strAttachment", strAttachment);
//                    myIntent.putExtras(bun);
//                    getActivity().startActivity(myIntent);
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return view;
    }

    private class LoadMail extends AsyncTask<String, Void, String[][]> {

        // private String saveDir;
        @Override
        protected String[][] doInBackground(String[] params) {
            String mail_host = imaphost;// change accordingly
            String mail_storeType = "pop3";
            final String mail_username = email;// change accordingly
            final String mail_password = pass;// change accordingly

            String result[][] = new String[6][9999];

            // do above Server call here
            try {
                //create properties field
                Properties properties = new Properties();

                properties.put("mail.imap.host", mail_host);
                properties.put("mail.imap.port", "995");
                properties.put("mail.imap.starttls.enable", "true");
                properties.put("mail.imap.starttls.enable", "true");

                Session emailSession = Session.getInstance(properties,
                        new javax.mail.Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(mail_username, mail_password);
                            }
                        });
                emailSession.setDebug(true);

                Store store = emailSession.getStore("imaps");

                store.connect(mail_host, mail_username, mail_password);
                MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
                mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
                mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
                mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
                mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
                mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
                CommandMap.setDefaultCommandMap(mc);

                //create the folder object and open it
                Folder emailFolder = store.getFolder("[Gmail]/Sent Mail");
                emailFolder.open(Folder.READ_ONLY);

                // retrieve the messages from the folder in an array and print it
                Message[] messages = emailFolder.getMessages();

                String[] ID = new String[messages.length];
                String[] from = new String[messages.length];
                String[] subject = new String[messages.length];
                String[] content = new String[messages.length];
                String[] attachment = new String[messages.length];
                String[] date = new String[messages.length];
                result = new String [6][messages.length];

                System.out.println("messages.length---" + messages.length);

                for (int i = 0; i < 15; i++) {
                    Message message = messages[i];
                    /*int ID = message.getMessageNumber();
                    Address[] fromAddress = message.getFrom();
                    String from = fromAddress[0].toString();
                    String subject = message.getSubject();
                    String sentDate = message.getSentDate().toString();*/

                    String contentType = message.getContentType();

                    // store attachment file name, separated by comma
                    String attachFiles = "";

                    if (contentType.contains("multipart")) {
                        Multipart mp = (Multipart) message.getContent();
                        int numberOfParts = mp.getCount();
                        for (int partCount = 0; partCount < numberOfParts; partCount++) {
                            MimeBodyPart part = (MimeBodyPart) mp.getBodyPart(partCount);
                            if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                                // this part is attachment
                                String fileName = part.getFileName();

                                attachFiles += fileName + ", ";

                                //part.saveFile(saveDir + File.separator + fileName);
                            } else {
                                // this part may be the message content

                                content[i] = part.getContent().toString();
                                Log.d("BBBBB",contentType);
                            }
                        }

                        if (attachFiles.length() > 1) {
                            attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
                        }
                    } else if (contentType.contains("text/plain")
                            || contentType.contains("text/html")) {
                        Object conten = message.getContent();
                        if (conten != null) {
                            content[i] = conten.toString();
                            Log.d("sunda",contentType);
                        }
                    }
                    ID[i] = String.valueOf(message.getMessageNumber());
                    Log.d("ID", ID.toString());
                    from[i] = message.getFrom()[0].toString();
                    Log.d("ID", from.toString());
                    subject[i] = message.getSubject();
                    Log.d("ID", subject.toString());
                    date[i] = message.getSentDate().toString().substring(4,10);
                    Log.d("ID", date.toString());
                    attachment[i] = attachFiles;
                }

                result[0] = ID;
                result[1] = from;
                result[2] = subject;
                result[3] = content;
                result[4] = attachment;
                result[5] = date;

                //close the store and folder objects
                emailFolder.close(false);
                store.close();

            }catch (NoSuchProviderException e) {
                Log.d("AAAAAA", "ERR1");
                e.printStackTrace();
            } catch (MessagingException e) {
                Log.d("AAAAAA", "ERR2");
                e.printStackTrace();
            }catch (Exception e) {
                Log.d("AAAAAA", "ERR3");
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String[][] result) {
            super.onPostExecute(result);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("INBOX");
    }
}
