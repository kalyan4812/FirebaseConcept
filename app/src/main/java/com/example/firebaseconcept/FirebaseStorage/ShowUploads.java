package com.example.firebaseconcept.FirebaseStorage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.firebaseconcept.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ShowUploads extends AppCompatActivity {
    RecyclerView recyclerView;
    private MyAdapter mAdapter;
    private DatabaseReference mDatabaseRef;
    private List<Upload> mUploads;
    private ValueEventListener valueEventListener;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_uploads);
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mUploads = new ArrayList<>();
        mAdapter = new MyAdapter(ShowUploads.this, mUploads);
        recyclerView.setAdapter(mAdapter);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        valueEventListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUploads.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    //   if (snapshot1.getValue(Upload.class) != null) {
                    Upload upload = snapshot1.getValue(Upload.class);
                    upload.setKey(snapshot1.getKey());
                    mUploads.add(upload);

                    // }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
       /* mDatabaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getValue(Upload.class) != null) {
                    mUploads.add(snapshot.getValue(Upload.class));
                }
                mAdapter = new MyAdapter(ShowUploads.this, mUploads);
                recyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
    }

    public void uploadPhoto(View view) {
        Intent i = new Intent(getApplicationContext(), FirebaseStorage.class);
        startActivity(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(valueEventListener);
    }
}
