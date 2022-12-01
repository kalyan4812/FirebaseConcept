package com.example.firebaseconcept.RealTimeDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firebaseconcept.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class RealTimeDatabaseActivity extends AppCompatActivity {
    EditText name, email;

    private DatabaseReference databaseReference;
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time_database);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        serachUsersByInput("ZO");
    }


    private String lastFetchedTitle = null;

    private void serachUsersByInput(String text) {
        if (lastFetchedTitle != null) {
            query = databaseReference.orderByChild("name_key")
                    .startAt(lastFetchedTitle).endAt(text + "\uf8ff")
                    .limitToFirst(100);
        } else {
            query = databaseReference.orderByChild("name_key").startAt(text)
                    .endAt(text + "\uf8ff")
                    .limitToFirst(200);
        }
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


                if (snapshot != null) {
                    User user = (User) snapshot.getValue(User.class);
                    System.out.println("child added " + user.getName());
                 //   if(previousChildName==null){
                        lastFetchedTitle = user.getName_key();
                   // }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot != null) {
                    User user = (User) snapshot.getValue(User.class);
                    System.out.println("child chnaged " + user.getName());
                }
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
        });
    }

    public void add(View view) {

        String uname = name.getText().toString();
        String uemail = email.getText().toString();
        for (int i = 0; i < 100; i++) {
            //    if(uemail!=null && uname!=null) {
            String key=FirebaseDatabase.getInstance().getReference("Users").push().getKey();
            String name= RandomStringUtils.randomAlphabetic(5,15);
            User user = new User(name, uemail,name+"_"+key);
            FirebaseDatabase.getInstance().getReference("Users").child(key)
                    .setValue(user);
            //   }
        }
        name.setText("");
        email.setText("");
        Toast.makeText(getApplicationContext(), "ADDED", Toast.LENGTH_SHORT).show();
    }

    public void read(View view) {
        FirebaseDatabase.getInstance().getReference("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    Toast.makeText(getApplicationContext(), user.getEmail() + "\n" + user.getName(), Toast.LENGTH_SHORT).show();
                    Log.i("data", user.getEmail() + "\n" + user.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void update(View view) {
        String uname = name.getText().toString();
        String uemail = email.getText().toString();
        if (uemail != null && uname != null) {
            User user = new User();
            user.setEmail(uemail);
            user.setName(uname);
            boolean b = FirebaseDatabase.getInstance().getReference("Users").child(uname)
                    .setValue(user).isSuccessful();
            name.setText("");
            email.setText("");
            if (b) {
                Toast.makeText(getApplicationContext(), "UPDATED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "NAME DOESN'T EXIST.", Toast.LENGTH_SHORT).show();
            }
        } else {
            return;
        }

    }

    public void delete(View view) {
        String uname = name.getText().toString();
        if (uname.trim().length() > 0) {
            boolean b = FirebaseDatabase.getInstance().getReference("Users").child(uname).removeValue().isSuccessful();
            name.setText("");
            email.setText("");
            if (b) {
                Toast.makeText(getApplicationContext(), "DELETED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "NAME DOESN'T EXIST.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "ENTER NAME TO DELETE", Toast.LENGTH_SHORT).show();
        }
    }

    public void searchQuery(View view) {
        serachUsersByInput("ZO");
    }
}
