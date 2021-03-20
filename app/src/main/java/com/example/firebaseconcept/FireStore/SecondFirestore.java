package com.example.firebaseconcept.FireStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.firebaseconcept.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class SecondFirestore extends AppCompatActivity {
    private EditText editTextTitle;
    private EditText editTextDescription;
    private TextView textViewData;
    private CollectionReference collectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_firestore);
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        textViewData = findViewById(R.id.text_view_data);
        collectionReference = FirebaseFirestore.getInstance().collection("NOTEBOOK");
    }

    @Override
    protected void onStart() {
        super.onStart();
        collectionReference.addSnapshotListener(this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                Log.i("check","called");
                if (e != null) {
                    return;
                }
                String data = "";
                assert queryDocumentSnapshots != null;
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Note note = documentSnapshot.toObject(Note.class);
                    note.setDocumentId(documentSnapshot.getId());
                    data = data + "Title : " + note.getTitle() + "\n" + "Description : " + note.getDescription() + "\n\n";
                }
                textViewData.setText(data);
            }
        });
    }

    public void loadNotes(View view) {
        collectionReference.whereGreaterThan("title","Sai Kalyan").whereEqualTo("description","android developer").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String data = "";
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Note note = documentSnapshot.toObject(Note.class);
                    note.setDocumentId(documentSnapshot.getId());
                    data = data + "Title : " + note.getTitle() + "\n" + "Description : " + note.getDescription() + "\n\n";
                }
                textViewData.setText(data);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            Log.i("check",e.getMessage());
            }
        });
    }

    public void addNote(View view) {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        Note note = new Note(title, description);
        collectionReference.add(note);
    }
}
