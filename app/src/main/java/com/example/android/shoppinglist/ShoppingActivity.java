package com.example.android.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShoppingActivity extends AppCompatActivity {

     EditText inputDescription;
     EditText inputPrice;
     EditText inputPriority;
     EditText inputQuantity;
     Button addToCartButton;
     Button checkoutButton;
     TextView displayBudget;
     TextView displayName;


    private ItemToBuy itemToBuy = new ItemToBuy();
     ShoppingList shoppingList = new ShoppingList();
     public static List<ItemToBuy> list = new ArrayList<>();

     private double budget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String budget = intent.getStringExtra("budget");

        displayBudget = findViewById(R.id.budget);
        displayName = findViewById(R.id.name);
        displayBudget.setText("" + budget);
        displayName.setText("" + name);

        checkOut();


    }

    public void addToCart()
    {
        inputDescription = findViewById(R.id.description);
        inputPrice = findViewById(R.id.price);
        inputPriority = findViewById(R.id.priority);
        inputQuantity = findViewById(R.id.quantity);
        addToCartButton = findViewById(R.id.addToCartButton);

        // Lambda Expression
        addToCartButton.setOnClickListener(view -> {

            String description = inputDescription.getText().toString();
            String price = inputPrice.getText().toString();
            String priority = inputPriority.getText().toString();
            String quantity = inputQuantity.getText().toString();

            if(description.matches(""))
            {
                Toast.makeText(ShoppingActivity.this, "Missing info..",
                        Toast.LENGTH_SHORT ).show();
                inputDescription.setError("Please enter item's name");
                return;
            }
            if(!isValidName(description))
            {
                inputDescription.setError("Invalid description");
                return;
            }
            if(shoppingList.size() > 0 && shoppingList.duplicateName(description))
            {
                Toast.makeText(ShoppingActivity.this, "The item already exists in your cart. Please enter a different one.",
                        Toast.LENGTH_SHORT ).show();
                return;
            }
            if(price.matches(""))
            {
                Toast.makeText(ShoppingActivity.this, "Missing info..",
                        Toast.LENGTH_SHORT ).show();
                inputPrice.setError("Please enter price ");
                return;
            }
            if(priority.matches(""))
            {
                Toast.makeText(ShoppingActivity.this, "Missing info..",
                        Toast.LENGTH_SHORT ).show();
                inputPriority.setError("Please enter priority");
                return;
            }
            if(quantity.matches(""))
            {
                Toast.makeText(ShoppingActivity.this, "Missing info..",
                        Toast.LENGTH_SHORT ).show();
                inputQuantity.setError("Please enter quantity");
                return;
            }

            Toast.makeText(ShoppingActivity.this, inputDescription.getText() + " is added to cart..",
                    Toast.LENGTH_SHORT ).show();

            itemToBuy.setName(description);
            itemToBuy.setPrice(Double.parseDouble(price));
            itemToBuy.setPriority(Integer.parseInt(priority));
            itemToBuy.setQuantity(Integer.parseInt(quantity));

            ItemToBuy itemToBuy = new ItemToBuy(description,Integer.parseInt(quantity),
                    Integer.parseInt(priority), Double.parseDouble(price));

            shoppingList.add(itemToBuy);
            list.add(itemToBuy);

            resetTextFields();

            // Send Custom Event to Amazon Pinpoint
     /*   final AnalyticsClient mgr = AWSProvider.getInstance()
                .getPinpointManager()
                .getAnalyticsClient();
        final AnalyticsEvent evt = mgr.createEvent("AddItemToCart")
                .withAttribute("itemName", String.valueOf(inputDescription.getText()));
        mgr.recordEvent(evt);
        mgr.submitEvents(); */



        });

    }

    public void checkOut()
    {
        addToCart();

        checkoutButton = findViewById(R.id.checkoutButton);

        // Lambda Expression
        checkoutButton.setOnClickListener(view -> {
            String preBudget = displayBudget.getText().toString();
            budget = Double.parseDouble(preBudget);
            shoppingList.bubbleSort();
            shoppingList.goShopping(budget);

            String output = "";
            output += "This is the confirmation of your shopping list today: \n" +
                    String.format("%-10s %-10s %-10s %-10s\n", "Quantity", "Name", "Priority", "Price");
            for (int i = 0; i < list.size(); i++)
            {
                output += list.get(i);
            }

            output += "\nBelow are the items we can get for you today:\n"+
                    String.format("%-10s %-10s %-10s %-10s\n", "Quantity", "Name", "Priority", "Price");
            for (int i = 0; i < list.size(); i++)
            {
                if (list.get(i).isPurchased())
                {
                    output += list.get(i);
                }
            }
            output += "\nAnd here are the items on your list "
                    + "that you have not purchased yet:\n"
                    + String.format("%-10s %-10s %-10s %-10s\n", "Quantity", "Name", "Priority", "Price");
            for (int i = 0; i < list.size(); i++)
            {
                if (!list.get(i).isPurchased())
                {
                    output += list.get(i);
                }
            }
            double finalBudget = shoppingList.goShopping(budget);
            output += "\nThis is the amount of your bank account before shopping: $" + budget;
            output += "\nAnd here is what left in your bank account after shopping: $" + finalBudget;

            AlertDialog alertDialog = new AlertDialog.Builder(ShoppingActivity.this).create();
            alertDialog.setTitle("Confirmation");
            alertDialog.setMessage(output);

            // Lambda Expression
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK"
                    , (dialogInterface, i) -> dialogInterface.dismiss());
            alertDialog.show();

        });

    }

    // Item's Name Validation
    private boolean isValidName(String name)
    {
        String NAME_PATTERN = "[A-Za-z\\s]+";
        Pattern pattern = Pattern.compile(NAME_PATTERN);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }
    public void resetTextFields()
    {
        inputDescription.setText(null);
        inputPrice.setText(null);
        inputPriority.setText(null);
        inputQuantity.setText(null);
    }

}
