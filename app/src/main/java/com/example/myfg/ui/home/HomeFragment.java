package com.example.myfg.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfg.MyCartsFragment;
import com.example.myfg.R;
import com.example.myfg.adapters.HomeAdapter;
import com.example.myfg.adapters.PopularAdapter;
import com.example.myfg.adapters.RecommendedAdapter;
import com.example.myfg.adapters.SliderAdapter;
import com.example.myfg.adapters.ViewAllAdapter;
import com.example.myfg.models.HomeCategory;
import com.example.myfg.models.PopularModel;
import com.example.myfg.models.RecommendedModel;
import com.example.myfg.models.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {


    ScrollView scrollView;
    ProgressBar progressBar;


    SliderView sliderView;
    int[] images = {R.drawable.fg_about_1, R.drawable.fg_about_2, R.drawable.fg_about_3,
            R.drawable.fb_about_5};

    EditText search;
    ImageView search_btn, cart;

    RecyclerView popularRec, homeCatRec, recommendedRec;
    FirebaseFirestore db;

    List<ViewAllModel> viewAllModelList;
    RecyclerView recyclerView;
    ViewAllAdapter viewAllAdapter;

    //populer items
    List<PopularModel> popularModelList;
    PopularAdapter popularAdapter;

    //cat items
    List<HomeCategory> categoryList;
    HomeAdapter homeAdapter;

    //Recommended
    List<RecommendedModel> recommendedModelList;
    RecommendedAdapter recommendedAdapter;

//    //view
//    ViewAllAdapter viewAllAdapter;
//    List<ViewAllModel> viewAllModelList;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        db = FirebaseFirestore.getInstance();

        popularRec = root.findViewById(R.id.pop_rec);
        homeCatRec = root.findViewById(R.id.explore_rec);
        recommendedRec = root.findViewById(R.id.recommended_rec);
        scrollView = root.findViewById(R.id.scroll_view);
        progressBar = root.findViewById(R.id.progressbar);
//        cart = root.findViewById(R.id.go_cart);

//        cart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(),MyCartsFragment.class);
//                startActivity(intent);
//            }
//        });


        sliderView = root.findViewById(R.id.image_slider1);

        SliderAdapter sliderAdapter = new SliderAdapter(images);

        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();

        progressBar.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        //search
//        recyclerView = root.findViewById(R.id.search_recycle);
//        recyclerView.setVisibility(View.GONE);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        viewAllModelList = new ArrayList<>();
//        viewAllAdapter = new ViewAllAdapter(getContext(), viewAllModelList);
//        recyclerView.setAdapter(viewAllAdapter);

        //popular
//        viewAllModelList = new ArrayList<>();
//        viewAllAdapter = new ViewAllAdapter(getActivity(), viewAllModelList);
//        popularRec.setAdapter(viewAllAdapter);


//        if (type != null && type.equalsIgnoreCase("Green Masala")) {
//            firestore.collection("AllProducts").whereEqualTo("type", "Green Masala").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
//                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
//                        viewAllModelList.add(viewAllModel);
//                        viewAllAdapter.notifyDataSetChanged();
//                        progressBar.setVisibility(View.GONE);
//                        recyclerView.setVisibility(View.VISIBLE);
//                    }
//                }
//            });
//        }

        //popular item
        popularRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        popularModelList = new ArrayList<>();
        popularAdapter = new PopularAdapter(getActivity(), popularModelList);
        popularRec.setAdapter(popularAdapter);

        db.collection("PopularProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                PopularModel popularModel = document.toObject(PopularModel.class);
                                popularModelList.add(popularModel);
                                popularAdapter.notifyDataSetChanged();

                                progressBar.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(getActivity(), "error" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //Cat item
        homeCatRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        categoryList = new ArrayList<>();
        homeAdapter = new HomeAdapter(getActivity(), categoryList);
        homeCatRec.setAdapter(homeAdapter);

        db.collection("HomeCategory")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                HomeCategory homeCategory = document.toObject(HomeCategory.class);
                                categoryList.add(homeCategory);
                                homeAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getActivity(), "error" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //Recommended item
        recommendedRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        recommendedModelList = new ArrayList<>();
        recommendedAdapter = new RecommendedAdapter(getActivity(), recommendedModelList);
        recommendedRec.setAdapter(recommendedAdapter);

        db.collection("Recommended")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                RecommendedModel recommendedModel = document.toObject(RecommendedModel.class);
                                recommendedModelList.add(recommendedModel);
                                recommendedAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getActivity(), "error" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //search
//        search_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                           firebaseItemSearch();
//            }
//        });


        return root;
    }


//    private void firebaseItemSearch() {
//
//
//    }
}