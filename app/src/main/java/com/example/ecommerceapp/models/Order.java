package com.example.ecommerceapp.models;

import java.util.List;

public class Order {
    String date, uID, total;
    List<String> items;

    public Order(){ }

    public Order(String date, String uID, String total, List<String> items) {
        this.date = date;
        this.uID = uID;
        this.total = total;
        this.items = items;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }
}
