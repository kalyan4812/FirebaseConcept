package com.example.firebaseconcept.FirebaseStorage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.firebaseconcept.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class FirebaseStorage extends AppCompatActivity {
    private Uri imageURi;
    private ImageView imageView;
    private EditText title;
    private ProgressBar progressBar;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private StorageTask storageTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_storage);
        imageView=findViewById(R.id.image);
        title=findViewById(R.id.title);
        progressBar=findViewById(R.id.progress_bar);
        storageReference= com.google.firebase.storage.FirebaseStorage.getInstance().getReference("uploads");
        databaseReference=FirebaseDatabase.getInstance().getReference("uploads");


    }

    public void upload(View view) {
        if(storageTask!=null && storageTask.isInProgress()){
            Toast.makeText(getApplicationContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
        }
        else {
            final String mytitle=title.getText().toString();
            if(imageURi!=null && mytitle.length()>0){
                final StorageReference mstorageReference=storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageURi));
                storageTask=mstorageReference.putFile(imageURi);
                storageTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(0);

                            }
                        }, 500);
                        Toast.makeText(getApplicationContext(), "Upload successful", Toast.LENGTH_LONG).show();
                        Task<Uri> urlTask = storageTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }

                                // Continue with the task to get the download URL
                                return mstorageReference.getDownloadUrl();
                            }
                        });
                        urlTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if(task.isSuccessful()) {
                                    Upload upload = new Upload(mytitle, task.getResult().toString());
                                    databaseReference.child(databaseReference.push().getKey()).setValue(upload);
                                }
                            }
                        });


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressBar.setProgress((int) progress);
                    }
                });
            }
            else {
                Toast.makeText(getApplicationContext(), "Unable to upload", Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void showupload(View view) {
        Intent i=new Intent(getApplicationContext(),ShowUploads.class);
        startActivity(i);
    }

    public void choosephoto(View view) {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageURi = data.getData();
            Log.i("data",imageURi.toString());
            Picasso.get().load(imageURi).fit().centerCrop().into(imageView);
        }
    }
    // to get type of file jpg/mp4/mp3 etc..
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}
