package com.example.firebaseconcept.FirebaseUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.firebaseconcept.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddNote extends AppCompatActivity {
    private EditText editTextTitle;
    private EditText editTextDescription;
    private NumberPicker numberPickerPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_black_24dp);
        setTitle("Add Note");
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        numberPickerPriority = findViewById(R.id.number_picker_priority);
        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(15);
        if (getIntent() != null && getIntent().getStringExtra("type") != null) {
            String path = getIntent().getStringExtra("documentpath");
            MyNote myNote = (MyNote) getIntent().getSerializableExtra("class");
            if(myNote!=null) {
                editTextTitle.setText(myNote.getTitle());
                editTextDescription.setText(myNote.getDescription());
                numberPickerPriority.setValue(myNote.getPriority());
            }
            setTitle("Edit Note");

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.savenote:
                savenote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void savenote() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        int priority = numberPickerPriority.getValue();
        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return;
        }
        if (getIntent() != null && getIntent().getStringExtra("type") != null) {
            FirebaseFirestore.getInstance().document(getIntent().getStringExtra("documentpath")).set(new MyNote(title, description, priority));
            Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();
        } else {
            FirebaseFirestore.getInstance().collection("MyNotes").add(new MyNote(title, description, priority));
            Toast.makeText(this, "Note added", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}
