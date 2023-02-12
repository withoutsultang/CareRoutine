package com.example.careroutine.viewmodel;

import androidx.databinding.BaseObservable;

import com.example.careroutine.model.AccountDB;

public class SignUpViewModel extends BaseObservable {

    private AccountDB accountDB;

    private String id;
    private String inputId;

    public SignUpViewModel(AccountDB accountDB){
        this.accountDB = accountDB;

        this.id = accountDB.setId();
    }

}
