package com.example.pankaj.farmguide;

/**
 * Created by PANKAJ on 2/2/2019.
 */

public class chat_list_item {
    String seller_id;
    public chat_list_item()
    {

    }
    public chat_list_item(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }
}
