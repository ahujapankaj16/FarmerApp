package com.example.pankaj.farmguide;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

/**
 * Created by PANKAJ on 1/31/2019.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {
    List<Product> list;
    FirebaseFirestore mfirestore;
    public ProductAdapter(List<Product> list)
    {
        this.list = list;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, final int position) {
        holder.name.setText(list.get(position).getName());
        holder.type.setText(list.get(position).getType());
        holder.price.setText(list.get(position).getPrice());
        holder.quantity.setText(list.get(position).getQuantity());
        holder.seller_id.setText(list.get(position).getSeller_id());
        holder.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("update hua",Integer.toString(position));
                Toast.makeText(ShowProducts.context,Integer.toString(position) , Toast.LENGTH_SHORT).show();
            mfirestore = FirebaseFirestore.getInstance();
            // Buyer update chat list
                DocumentReference dref = mfirestore.collection("Seller").document(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                dref.update("chat_id", FieldValue.arrayUnion(FirebaseAuth.getInstance().getCurrentUser().getUid()+list.get(position).getSeller_id()));
                Toast.makeText(ShowProducts.context,"Updated in firestore", Toast.LENGTH_SHORT).show();
                // Seller update chatlist
                dref = mfirestore.collection("Seller").document(list.get(position).getSeller_id());
                dref.update("chat_id", FieldValue.arrayUnion(FirebaseAuth.getInstance().getCurrentUser().getUid()+list.get(position).getSeller_id()));
            }
        });
     //   Log.d("data aaya",model.getName());
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_item,viewGroup,false);
        return new ProductHolder(v);
    }

    class ProductHolder extends RecyclerView.ViewHolder{
        TextView name,type,price,quantity,seller_id;
        ImageButton cart;
        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cvname);
            type = itemView.findViewById(R.id.cvtype);
            price = itemView.findViewById(R.id.cvprice);
            quantity = itemView.findViewById(R.id.cvquantity);
            seller_id = itemView.findViewById(R.id.cvseller);
            cart = itemView.findViewById(R.id.cvcart);

        }
    }
}
