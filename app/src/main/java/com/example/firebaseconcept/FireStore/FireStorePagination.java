package com.example.firebaseconcept.FireStore;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebaseconcept.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;


public class FireStorePagination extends AppCompatActivity {
    private static final String TAG = "FireStorePagination";
    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextPriority;
    private TextView textViewData;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Notebook");
    private DocumentSnapshot lastResult;
    private NestedScrollView nestedScrollView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_store_pagination);
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        editTextPriority = findViewById(R.id.edit_text_priority);
        textViewData = findViewById(R.id.text_view_data);
        nestedScrollView = findViewById(R.id.nested);
        nestedScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                View view = (View) nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);

                int diff = (view.getBottom() - (nestedScrollView.getHeight() + nestedScrollView
                        .getScrollY()));

                if (diff == 0) {
                    Toast.makeText(getBaseContext(), "Scroll View bottom reached", Toast.LENGTH_SHORT).show();
                    loadData(notebookRef.orderBy("priority")
                            .startAfter(lastResult)
                            .limit(3));
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        notebookRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    DocumentSnapshot documentSnapshot = dc.getDocument();
                    String id = documentSnapshot.getId();
                    int oldIndex = dc.getOldIndex();
                    int newIndex = dc.getNewIndex();
                    switch (dc.getType()) {
                        case ADDED:

                            Toast.makeText(getApplicationContext(), "\nAdded: " + id +
                                    "\nOld Index: " + oldIndex + "New Index: ", Toast.LENGTH_SHORT).show();
                            break;
                        case MODIFIED:

                            Toast.makeText(getApplicationContext(), "\nModified: " + id +
                                    "\nOld Index: " + oldIndex + "New Index: " + newIndex, Toast.LENGTH_SHORT).show();
                            break;
                        case REMOVED:

                            Toast.makeText(getApplicationContext(), "\nRemoved: " + id +
                                    "\nOld Index: " + oldIndex + "New Index: ", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }
        });
    }
// to load more items when user scrolls down in onScrolled()
    /*if(llm.findLastCompletelyVisibleItemPosition() == data.length() -1){
               //bottom of list!
               loadMoreData();
            }

           -------- or----
           recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (! recyclerView.canScrollVertically(1)){ //1 for down
                    loadMore();
                }
            }
        });
            */

    public void addNote(View v) {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        if (editTextPriority.length() == 0) {
            editTextPriority.setText("0");
        }
        int priority = Integer.parseInt(editTextPriority.getText().toString());
        PageNote note = new PageNote(title, description, priority);
        notebookRef.add(note);
    }

    public void loadNotes(View v) {
        Query query;
        if (lastResult == null) {
            query = notebookRef.orderBy("priority")
                    .limit(3);
            loadData(query);
        }
        /*else {
            query = notebookRef.orderBy("priority")
                    .startAfter(lastResult)
                    .limit(3);
        }*/
    }

    public void loadData(Query query) {
        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            PageNote note = documentSnapshot.toObject(PageNote.class);
                            note.setDocumentId(documentSnapshot.getId());
                            String documentId = note.getDocumentId();
                            String title = note.getTitle();
                            String description = note.getDescrption();
                            int priority = note.getPriority();
                            data += "ID: " + documentId
                                    + "\nTitle: " + title + "\nDescription: " + description
                                    + "\nPriority: " + priority + "\n\n";
                        }
                        if (queryDocumentSnapshots.size() > 0) {
                            data += "___________\n\n";
                            textViewData.append(data);
                            lastResult = queryDocumentSnapshots.getDocuments()
                                    .get(queryDocumentSnapshots.size() - 1);
                        }
                    }
                });
    }
    /* code for batch writes


    private void executeBatchedWrite() {
        WriteBatch batch = db.batch();
        DocumentReference doc1 = notebookRef.document("New Note");
        batch.set(doc1, new PageNote("New Note", "New Note", 1));
        DocumentReference doc2 = notebookRef.document("dGWYZytpR6CPxkXZIM1D");
        batch.update(doc2, "title", "Updated Note");
        DocumentReference doc3 = notebookRef.document("SWUsDtcEfECSJXE6zuZc");
        batch.delete(doc3);
        DocumentReference doc4 = notebookRef.document();
        batch.set(doc4, new PageNote("Added Note", "Added Note", 1));
        batch.commit().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                textViewData.setText(e.toString());
            }
        });
    }

     */
    //**********************************************************************************

    /*  code for transactions

      private void executeTransaction() {
        db.runTransaction(new Transaction.Function<Long>() {
            @Override
            public Long apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentReference exampleNoteRef = notebookRef.document("Example Note");
                DocumentSnapshot exampleNoteSnapshot = transaction.get(exampleNoteRef);
                long newPriority = exampleNoteSnapshot.getLong("priority") + 1;
                transaction.update(exampleNoteRef, "priority", newPriority);
                return newPriority;
            }
        }).addOnSuccessListener(new OnSuccessListener<Long>() {
            @Override
            public void onSuccess(Long result) {
                Toast.makeText(FirestorePagination.this, "New Priority: " + result, Toast.LENGTH_SHORT).show();
            }
        });
    }
     */


}
