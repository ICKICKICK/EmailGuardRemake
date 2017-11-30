package com.ick.emailguard.model;

/**
 * Created by Edo Firmansyah on 30/11/2017.
 */

public class Mail {
    private String from, subject, content;

    public Mail() {
    }

    public Mail(String from, String subject, String content) {
        this.from = from ;
        this.subject = subject;
        this.content = content;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
