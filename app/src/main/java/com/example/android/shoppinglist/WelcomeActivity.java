package com.example.android.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WelcomeActivity extends AppCompatActivity {

    public static PinpointManager pinpointManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Initialize the AWS Mobile Client
        AWSMobileClient.getInstance().initialize(this).execute();

        PinpointConfiguration config = new PinpointConfiguration(
                WelcomeActivity.this,
                AWSMobileClient.getInstance().getCredentialsProvider(),
                AWSMobileClient.getInstance().getConfiguration()
        );
        pinpointManager = new PinpointManager(config);
        pinpointManager.getSessionClient().startSession();
        pinpointManager.getAnalyticsClient().submitEvents();

        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                Log.d("WelcomeActivity", "AWSMobileClient is instantiated and you are connected to AWS!");
            }
        }).execute();

        goShopping();
    }
    public void goShopping()
    {
        final EditText inputName = findViewById(R.id.editUserName);
        final EditText inputBudget = findViewById(R.id.editBudget);
        final Button goShoppingButton = findViewById(R.id.goShoppingButton);


        // Lambda Expression
        goShoppingButton.setOnClickListener(view -> {
            String userBudget = inputBudget.getText().toString();

            String userName = inputName.getText().toString();

            if (userName.matches(""))
            {
                inputName.setError("Please enter your name");
                return;
            }
            if(userBudget.matches(""))
            {
                inputBudget.setError("Please enter your budget");
                return;
            }

            if (!isValidName(userName))
            {
                inputName.setError("Invalid Name");
                return;
            }


          Intent intent = new Intent(WelcomeActivity.this, ShoppingActivity.class);
            intent.putExtra("budget", userBudget);
            intent.putExtra("name", userName);
            startActivity(intent);
        });


    }
    // User Name Validation
    private boolean isValidName(String name)
    {
        String NAME_PATTERN = "[A-Za-z\\s]+";
        Pattern pattern = Pattern.compile(NAME_PATTERN);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }
}
