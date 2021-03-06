package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Utils.PostModel;
import Utils.UserApi;
import de.hdodenhof.circleimageview.CircleImageView;
import fragments.UpdateProfileFragment;
import ui.ImageGridAdapter;
import ui.PostRecyclerAdapter;

public class ProfileFragment extends Fragment {
    FirebaseUser currentuser;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    TextView tv_username,tv_bio,tv_prof;
    CircleImageView profile_image_container;
    ImageView coverImageContainer;
    FloatingActionButton editProfileBtn;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseUser currentUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference = db.collection("post");

    UserApi userApi = UserApi.getInstance();
    List<PostModel> postModelList = new ArrayList<>();
    RecyclerView recyclerView;
    ImageGridAdapter imageGridAdapter;



    public ProfileFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        View view=inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth=FirebaseAuth.getInstance();
        currentuser=mAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users");


        tv_username = view.findViewById(R.id.fp_username);
        tv_bio = view.findViewById(R.id.fp_bio);
        tv_prof = view.findViewById(R.id.fp_profession);


        profile_image_container =(CircleImageView) view.findViewById(R.id.fp_profileImage);
        coverImageContainer=view.findViewById(R.id.fp_cover);

        editProfileBtn=view.findViewById(R.id.fp_editprofile);

        recyclerView = view.findViewById(R.id.fp_display_post_images);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));



        UserApi userApi=UserApi.getInstance();

        tv_bio.setText(userApi.getBio());
        tv_prof.setText(userApi.getProfession());
        tv_username.setText(userApi.getUsername());
        try{
            Log.i("info",userApi.getImage());
            Picasso.get().load(userApi.getImage()).into(profile_image_container);
        }catch (Exception e){
            Log.i("exception image",userApi.getImage());
            Picasso.get().load(R.drawable.ic_baseline_person_outline_24).into(profile_image_container);
        }
        try{
            Log.i("info",userApi.getCover());
            Picasso.get().load(userApi.getCover()).into(coverImageContainer);
        }catch (Exception e){
            Log.i("excepton image",userApi.getCover());
            Picasso.get().load(R.drawable.ic_baseline_person_outline_24).into(coverImageContainer);
        }

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameL, new UpdateProfileFragment()).addToBackStack(null).commit();

            }
        });

        loadPostFromFirebase();

        return view;
    }

    private void loadPostFromFirebase() {

        collectionReference
                .whereEqualTo("uid", userApi.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "onComplete: getting images");
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                PostModel p = document.toObject(PostModel.class);
                                postModelList.add(p);
                                Log.d("TAG", "onComplete: "+p.getImageUrl());
                            }

                            imageGridAdapter = new ImageGridAdapter(ProfileFragment.this, postModelList);
                            recyclerView.setAdapter(imageGridAdapter);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
}