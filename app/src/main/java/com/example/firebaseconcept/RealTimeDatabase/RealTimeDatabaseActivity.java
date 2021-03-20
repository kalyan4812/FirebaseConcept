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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RealTimeDatabaseActivity extends AppCompatActivity {
    EditText name, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time_database);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
    }

    public void add(View view) {

        String uname=name.getText().toString();
        String uemail=email.getText().toString();
   //    if(uemail!=null && uname!=null) {
            User user = new User(uname, uemail);
            FirebaseDatabase.getInstance().getReference("Users").child(uname)
                    .setValue(user);
     //   }
       name.setText("");
       email.setText("");
        Toast.makeText(getApplicationContext(),"ADDED",Toast.LENGTH_SHORT).show();
    }

    public void read(View view) {
        FirebaseDatabase.getInstance().getReference("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    User user=dataSnapshot.getValue(User.class);
                    Toast.makeText(getApplicationContext(),user.getEmail()+"\n"+user.getName(),Toast.LENGTH_SHORT).show();
                    Log.i("data",user.getEmail()+"\n"+user.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void update(View view) {
        String uname=name.getText().toString();
        String uemail=email.getText().toString();
            if(uemail!=null && uname!=null) {
                User user = new User();
                user.setEmail(uemail);
                user.setName(uname);
                boolean b = FirebaseDatabase.getInstance().getReference("Users").child(uname)
                        .setValue(user).isSuccessful();
                name.setText("");
                email.setText("");
                if(b) {
                    Toast.makeText(getApplicationContext(), "UPDATED", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "NAME DOESN'T EXIST.", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                return;
            }

    }

    public void delete(View view) {
        String uname=name.getText().toString();
        if(uname.trim().length()>0) {
          boolean b=  FirebaseDatabase.getInstance().getReference("Users").child(uname).removeValue().isSuccessful();
            name.setText("");
            email.setText("");
            if(b) {
                Toast.makeText(getApplicationContext(), "DELETED", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "NAME DOESN'T EXIST.", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "ENTER NAME TO DELETE", Toast.LENGTH_SHORT).show();
        }
    }
}
