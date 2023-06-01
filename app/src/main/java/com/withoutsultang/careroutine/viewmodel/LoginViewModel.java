package com.withoutsultang.careroutine.viewmodel;

import android.text.TextUtils;
import android.widget.Toast;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.withoutsultang.careroutine.model.User;
import com.withoutsultang.careroutine.view.LoginActivity;

public class LoginViewModel extends ViewModel {
    public ObservableField<String> id = new ObservableField<>();
    public ObservableField<String> password = new ObservableField<>();
    private MutableLiveData<String> loginResultLiveData = new MutableLiveData<>();
    private MutableLiveData<User>   userMutableLiveData;

    public LiveData<String> getLoginResult() {
        return loginResultLiveData;
    }

    public LoginViewModel() {
    }

    public MutableLiveData<User> getUser() {
        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
        }
        return userMutableLiveData;
    }

    public void onLoginClicked() {
        User loginUser = new User(id.get(), password.get());

        if (loginUser != null) {
            if (TextUtils.isEmpty(loginUser.getId()) || TextUtils.isEmpty(loginUser.getPassword())) {
                loginResultLiveData.setValue("ID or Password can't be empty");
            } else if (loginUser.getId().equals("admin") && loginUser.getPassword().equals("1234")){
                loginResultLiveData.setValue("Login Success");
            } else {
                loginResultLiveData.setValue("Login Failed");
            }
        }
    }

//    var showMainActivity : MutableLiveData<Boolean> = MutableLiveData(false);
//    var showFindAccountActivity : MutableLiveData<Boolean> = MutableLiveData(false);
//    var showSignUpActivity : MutableLiveData<Boolean> = MutableLiveData(false);
}
