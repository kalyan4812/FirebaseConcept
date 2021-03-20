package com.example.firebaseconcept.FireStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebaseconcept.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;

public class FirestoreArrays extends AppCompatActivity {
    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextPriority;
    private EditText editTextTags;
    private TextView textViewData;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Notebooks");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firestore_arrays);
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        editTextPriority = findViewById(R.id.edit_text_priority);
        editTextTags = findViewById(R.id.edit_text_tags);
        textViewData = findViewById(R.id.text_view_data);
    }

    public void loadNotes(View view) {
        notebookRef.whereArrayContains("tags", "tag5").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            ArrayNote note = documentSnapshot.toObject(ArrayNote.class);
                            note.setDocumentId(documentSnapshot.getId());
                            String documentId = note.getDocumentId();
                            data += "ID: " + documentId;
                            for (String tag : note.getTags()) {
                                data += "\n-" + tag;
                            }
                            data += "\n\n";
                        }
                        textViewData.setText(data);
                    }
                });
    }

    public void addNote(View view) {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        if (editTextPriority.length() == 0) {
            editTextPriority.setText("0");
        }
        int priority = Integer.parseInt(editTextPriority.getText().toString());
        String tagInput = editTextTags.getText().toString();
        String[] tagArray = tagInput.split("\\s*,\\s*");
        List<String> tags = Arrays.asList(tagArray);
        ArrayNote note = new ArrayNote(title, description, priority, tags);
       // notebookRef.add(note);
        notebookRef.document(title).set(note);
    }
    private void updateArray() {
        String title = editTextTitle.getText().toString();
        if(title.length()==0){
            return;
        }
        notebookRef.document(title)
                .update("tags", FieldValue.arrayUnion("new tag")).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void removeValueInArray() {
        String title = editTextTitle.getText().toString();
        if(title.length()==0){
            return;
        }
        notebookRef.document(title)
                .update("tags", FieldValue.arrayRemove("new tag")).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void update(View view) {
        updateArray();
    }

    public void delete(View view) {
        removeValueInArray();
    }
}
