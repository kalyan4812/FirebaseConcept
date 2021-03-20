package com.example.firebaseconcept.FireStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firebaseconcept.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class FireStoreActivity extends AppCompatActivity {
    private static final String TAG = "FireStoreActivity";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private EditText editTextTitle;
    private EditText editTextDescription;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference documentReference;
    private ListenerRegistration listenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_store);
        firebaseFirestore = FirebaseFirestore.getInstance();
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
    }

    public void saveNote(View view) {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        Map<String, Object> note = new HashMap<>();
        note.put(KEY_TITLE, title);
        note.put(KEY_DESCRIPTION, description);
        firebaseFirestore.collection("FirstCollection").document("FirstDocument").set(note).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Note saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }

    @Override
    protected void onStart() {
        documentReference = firebaseFirestore.collection("FirstCollection").document("FirstDocument");
        listenerRegistration = documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(getApplicationContext(), "Error while loading!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.toString());
                    return;
                }
                if (documentSnapshot.exists()) {
                    Map<String, Object> note = documentSnapshot.getData();
                    Toast.makeText(getApplicationContext(), "Title: " + note.get(KEY_TITLE) + "\n" + "Description: " + note.get(KEY_DESCRIPTION), Toast.LENGTH_SHORT).show();

                }
            }
        });
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        listenerRegistration.remove();
    }

    public void loadNote(View view) {

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {

                    Map<String, Object> note = documentSnapshot.getData();
                    Toast.makeText(getApplicationContext(), "Title: " + note.get(KEY_TITLE) + "\n" + "Description: " + note.get(KEY_DESCRIPTION), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Document does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }

    public void updateDescription(View view) {
        String description = editTextDescription.getText().toString();
        //Map<String, Object> note = new HashMap<>();
        //note.put(KEY_DESCRIPTION, description);
       // documentReference.set(note, SetOptions.merge());
       // documentReference.update(note);

        documentReference.update(KEY_DESCRIPTION, description);
    }

    public void deleteDescription(View view) {
        //Map<String, Object> note = new HashMap<>();
        //note.put(KEY_DESCRIPTION, FieldValue.delete());
        //documentReference.update(note);
        documentReference.update(KEY_DESCRIPTION, FieldValue.delete());
    }

    public void deleteNote(View view) {
        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"NOTE DELETED",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
