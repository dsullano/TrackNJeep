package com.example.tracknjeep_test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tracknjeep_test.auth.Login;
import com.example.tracknjeep_test.auth.Register;

public class MainActivity extends AppCompatActivity {

    private Button signUp;
    private Button test;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        signUp = (Button) findViewById(R.id.btnUser);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register register = new Register();
                register.show(getSupportFragmentManager(),"RegisterBottomeSheetDIalog");
            }
        });

        test = (Button) findViewById(R.id.btnGuest);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login loginDialog = new Login();
                loginDialog.show(getSupportFragmentManager(), "LoginBottomSheetDialog");
            }
        });
    }
}