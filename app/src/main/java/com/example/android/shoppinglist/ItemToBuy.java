package com.example.android.shoppinglist;

/**
 * Created by HIENDANG on 4/12/18.
 */

public class ItemToBuy extends Item implements GetInfo {
    private int quantity;
    private int priority;
    private double price;
    private boolean purchased;

    public ItemToBuy()
    {
        super();
    }
    public ItemToBuy(String initialName, int initialQuantity,
                     int initialPriority, double initialPrice)
    {
        super(initialName);
        quantity = initialQuantity;
        priority = initialPriority;
        price = initialPrice;
        purchased = false;
    }
    public boolean isPurchased()
    {
        return purchased;
    }
    public void setPurchased (boolean purchased)
    {
        this.purchased = purchased;
    }
    public int getQuantity()
    {
        return quantity;
    }
    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }
    public int getPriority()
    {
        return priority;
    }
    public void setPriority(int priority)
    {
        this.priority = priority;
    }
    public double getPrice()
    {
        return price;
    }
    public void setPrice(double price)
    {
        this.price = price;
    }
    public double getTotalCost()
    {
        return (this.quantity * this.price);
    }
    public boolean equalsIgnoreCase(String str)
    {
        return this.getName().equalsIgnoreCase(str);
    }
    @Override
    public String toString()
    {
        return String.format("%-15d %-15s %-15d %-15.2f\n", getQuantity(),
                getName(), getPriority(), getPrice());
    }
}
