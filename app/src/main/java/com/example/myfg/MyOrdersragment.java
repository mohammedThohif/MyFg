package com.example.myfg;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myfg.activities.PaymentActivity;
import com.example.myfg.adapters.MyCartAdapter;
import com.example.myfg.adapters.MyOrderAdapter;
import com.example.myfg.models.MyCartModel;
import com.example.myfg.models.MyOrderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MyOrdersragment extends Fragment {

    FirebaseFirestore db;
    FirebaseAuth auth;

    Context context;

    RecyclerView recyclerView;
    MyOrderAdapter cartAdapter;
    List<MyOrderModel> cartModelList;

    ConstraintLayout r1, r2;

    ProgressBar progressBar;

    public MyOrdersragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_my_ordersragment, container, false);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();


        r1 = root.findViewById(R.id.constaint1);
        r2 = root.findViewById(R.id.constaint2);
        progressBar = root.findViewById(R.id.progressbar);
//        progressBar.setVisibility(View.VISIBLE);
        recyclerView = root.findViewById(R.id.recyclerview);
        if (db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("MyOrder").getPath().isEmpty()) {
            r1.setVisibility(View.GONE);
            r2.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

        } else {
            r1.setVisibility(View.VISIBLE);
            r2.setVisibility(View.GONE);
        }
//        recyclerView.setVisibility(View.GONE);


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        cartModelList = new ArrayList<>();
        cartAdapter = new MyOrderAdapter(getActivity(), cartModelList);

        recyclerView.setAdapter(cartAdapter);


        db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("MyOrder").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {

                        String documentId = documentSnapshot.getId();

                        MyOrderModel cartModel = documentSnapshot.toObject(MyOrderModel.class);

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
}
