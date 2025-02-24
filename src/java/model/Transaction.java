/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;

/**
 *
 * @author FPTSHOP
 */
public class Transaction {
    private int transactionId;
    private int userId;
    private int courseId;
    private double amount;
    private String status; 
    private Date transactionDate;
    
    public Transaction() {
    }

    public Transaction(int transactionId, int userId, int courseId, double amount, String status, Date transactionDate) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.courseId = courseId;
        this.amount = amount;
        this.status = status;
        this.transactionDate = transactionDate;
    }
    
    public Transaction(int userId, int courseId, double amount) {
        this.userId = userId;
        this.courseId = courseId;
        this.amount = amount;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }
}
