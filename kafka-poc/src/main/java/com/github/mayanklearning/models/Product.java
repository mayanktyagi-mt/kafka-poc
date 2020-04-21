package com.github.mayanklearning.models;

public class Product {

    private String cfn;

    private String title;

    private String description;

    private String count;

    public Product(){
        // must to have no argument constructor
    }

    public Product(String cfn, String title, String description, String count){
        this.cfn = cfn;
        this.title = title;
        this.description = description;
        this.count = count;
    }

    public String getCfn() {
        return cfn;
    }

    public void setCfn(String cfn) {
        this.cfn = cfn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
