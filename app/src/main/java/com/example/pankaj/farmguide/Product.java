package com.example.pankaj.farmguide;

/**
 * Created by PANKAJ on 1/31/2019.
 */

public class Product {
    private String type,name,seller_id;
    private int price, quantity;
    public Product()
    {

    }
    public Product(String Name, int Price, int Quantity, String Seller_id, String Type) {
        this.type = Type;
        this.name = Name;
        this.seller_id = Seller_id;
        this.price = Price;
        this.quantity = Quantity;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getPrice() {

        return Integer.toString(price);
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getQuantity() {
        return Integer.toString(quantity);
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


}
