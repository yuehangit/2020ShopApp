package com.b07.actions;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import android.content.Context;

import com.b07.users.Roles;
import com.b07.users.UserInterface;
import com.example.androidlayouts.AdminView;
import com.example.androidlayouts.CustomerView;
import com.example.androidlayouts.EmployeeView;
import com.example.androidlayouts.R;

import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.b07.store.ApplicationHelper;

public class onLoginBtn implements View.OnClickListener {
    private Context appContext;

    public onLoginBtn(Context context) {
        System.out.println("Called loginBtn");
        this.appContext = context;
    }

    @Override
    public void onClick(View view) {
        EditText userId = (EditText) ((AppCompatActivity)appContext).findViewById(R.id.etNameLogin);
        EditText userPw = (EditText) ((AppCompatActivity)appContext).findViewById(R.id.etPasswordLogin);
        Spinner userType = (Spinner) ((AppCompatActivity)appContext).findViewById(R.id.sUserTypeMenu);

        int id = -1;
        try {
            id = Integer.parseInt(userId.toString());
        } catch (NumberFormatException e) {
            Toast.makeText(appContext, "Invalid ID", Toast.LENGTH_SHORT).show();
        }

        String password = userPw.toString();
        String role = userType.getSelectedItem().toString();
        UserInterface user;
        Intent nextPage = null;
        System.out.println("Got data");
            switch (role) {
                case "Customer":
                    System.out.println("Customer, going to validate");
                    user = ApplicationHelper.logIn(Roles.CUSTOMER, id, password, appContext);
                    if (user != null) {
                        nextPage = new Intent(appContext, CustomerView.class);
                    }
                    break;
                case "Employee":
                    System.out.println("Employee, going to validate");
                    user = ApplicationHelper.logIn(Roles.EMPLOYEE, id, password, appContext);
                    if (user != null) {
                        nextPage = new Intent(appContext, EmployeeView.class);
                    }
                    break;
                case "Admin":
                    System.out.println("Admin, going to validate");
                    user = ApplicationHelper.logIn(Roles.ADMIN, id, password, appContext);
                    if (user != null) {
                        nextPage = new Intent(appContext, AdminView.class);
                    }
                    break;
                default:
                    break;
            }
            if (nextPage != null) {
                appContext.startActivity(nextPage);
            }
    }



}
