package com.example.myfg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfg.activities.AddAddressActivity;
import com.example.myfg.activities.AddTimingActivity;
import com.example.myfg.activities.AddressActivity;
import com.example.myfg.activities.DineInActivity;
import com.example.myfg.activities.PaymentActivity;
import com.example.myfg.adapters.MyCartAdapter;
import com.example.myfg.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyCartsFragment extends Fragment {


    FirebaseFirestore db;
    FirebaseAuth auth;

    Context context;

    TextView overTotalAmount;
    RecyclerView recyclerView;
    MyCartAdapter cartAdapter;
    List<MyCartModel> cartModelList;

    ConstraintLayout layout, layout1;

    RelativeLayout r1, r2;

    Button buy_Now, add_address, dineIn, addSlot;
    int size;

    ProgressBar progressBar;

    public MyCartsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_my_carts, container, false);


        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        r1 = root.findViewById(R.id.rel1);
        r2 = root.findViewById(R.id.rel2);


        cartModelList = new ArrayList<>();
        cartAdapter = new MyCartAdapter(getActivity(), cartModelList);

        size = cartModelList.size();

        buy_Now = root.findViewById(R.id.buy_now);
        add_address = root.findViewById(R.id.add_add_address);
        dineIn = root.findViewById(R.id.din_in);
        addSlot = root.findViewById(R.id.add_add_Timing);


        progressBar = root.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        recyclerView = root.findViewById(R.id.recyclerview);
        recyclerView.setVisibility(View.GONE);


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        overTotalAmount = root.findViewById(R.id.total_top);

        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(mMessageReceiver, new IntentFilter("MyTotalAmount"));


        recyclerView.setAdapter(cartAdapter);

        add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddAddressActivity.class);
                startActivity(intent);
//                r1.setVisibility(View.VISIBLE);
//                r2.setVisibility(View.GONE);
            }
        });

        addSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddTimingActivity.class);
                startActivity(intent);
//                r1.setVisibility(View.VISIBLE);
//                r2.setVisibility(View.GONE);
            }
        });

        buy_Now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PaymentActivity.class);
                intent.putExtra("itemList", (Serializable) cartModelList);
                deletecart();
                startActivity(intent);
                r1.setVisibility(View.VISIBLE);
                r2.setVisibility(View.GONE);
            }
        });


        dineIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DineInActivity.class);
                intent.putExtra("itemList", (Serializable) cartModelList);
                deletecart();
                startActivity(intent);
                r1.setVisibility(View.VISIBLE);
                r2.setVisibility(View.GONE);
            }
        });

        if (db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").getPath().isEmpty()) {
            r1.setVisibility(View.GONE);
            r2.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

        } else {
            r1.setVisibility(View.VISIBLE);
            r2.setVisibility(View.GONE);
        }


        db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {

                        String documentId = documentSnapshot.getId();

                        MyCartModel cartModel = documentSnapshot.toObject(MyCartModel.class);

                        cartModel.setDocumentId(documentId);

                        cartModelList.add(cartModel);
                        cartAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        r1.setVisibility(View.GONE);
                        r2.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        return root;

    }


    private void sendData(View view) {
        Intent intent = new Intent(getContext(), PaymentActivity.class);
        intent.putExtra("itemList", (Serializable) cartModelList);
        deletecart();
    }


    private void deletecart() {
        int position = 0;

        db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                    db.collection("AddToCart").document(snapshot.getId()).delete();
                }
            }
        });
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int totalBill = intent.getIntExtra("totalAmount", 0);
            overTotalAmount.setText("Total Bill :" + totalBill + "₹");
        }
    };

}