package com.github.mayanklearning.models;

public class Tweet {

    private String id_str;
    private String created_at;
    private String text;
    private String source;
    private boolean truncated;


    Tweet(){
        // mandatory
    }

    Tweet( String id_str, String created_at, String text, String source, boolean truncated){
        this.id_str = id_str;
        this.created_at = created_at;
        this.text =  text;
        this.source = source;
        this.truncated = truncated;
    }

    public String getId_str() {
        return id_str;
    }

    public void setId_str(String id_str) {
        this.id_str = id_str;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }
}
