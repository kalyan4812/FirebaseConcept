package com.example.firebaseconcept;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebaseconcept.FireStore.FireStoreActivity;
import com.example.firebaseconcept.FireStore.FireStorePagination;
import com.example.firebaseconcept.FireStore.FirestoreArrays;
import com.example.firebaseconcept.FireStore.SecondFirestore;
import com.example.firebaseconcept.FirebaseStorage.FirebaseStorage;
import com.example.firebaseconcept.FirebaseUI.FirebaseUiActivity;
import com.example.firebaseconcept.RealTimeDatabase.RealTimeDatabaseActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    FirebaseUser firebaseUser;
    LinearLayout linearLayout;
    Button button;
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.login);

        linearLayout = findViewById(R.id.mylinear);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            linearLayout.setVisibility(View.VISIBLE);
            button.setVisibility(View.GONE);

        } else {
            linearLayout.setVisibility(View.VISIBLE);
            button.setVisibility(View.VISIBLE);

        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        // Get an instance of AuthUI based on the default app
                        AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.GoogleBuilder().build(),
                                new AuthUI.IdpConfig.EmailBuilder().build(),
                                new AuthUI.IdpConfig.PhoneBuilder().build()
                                )).setIsSmartLockEnabled(false) // diable googl smart lock
                                .build(),
                        RC_SIGN_IN);

            }
    });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
           //     startActivity(SignedInActivity.createIntent(this, response));
             //   finish();
                firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
                linearLayout.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(),firebaseUser.getUid()+"\n"+firebaseUser.getDisplayName(),Toast.LENGTH_LONG).show();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                 //   showSnackbar(R.string.sign_in_cancelled);
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                  //  showSnackbar(R.string.no_internet_connection);
                    return;
                }

              //  showSnackbar(R.string.unknown_error);
              //  Log.e(TAG, "Sign-in error: ", response.getError());
            }
        }
    }

    public void realtimedatabase(View view) {
        Intent intent = new Intent(MainActivity.this, RealTimeDatabaseActivity.class);
        startActivity(intent);
    }

    public void storage(View view) {
        Intent intent = new Intent(MainActivity.this, FirebaseStorage.class);
        startActivity(intent);
    }

    public void firestore1(View view) {
        Intent intent = new Intent(MainActivity.this, FireStoreActivity.class);
        startActivity(intent);
    }

    public void firestore2(View view) {
        Intent intent = new Intent(MainActivity.this, SecondFirestore.class);
        startActivity(intent);
    }

    public void firestore3(View view) {
        Intent intent = new Intent(MainActivity.this, FireStorePagination.class);
        startActivity(intent);
    }

    public void firestore4(View view) {
        Intent intent = new Intent(MainActivity.this, FirestoreArrays.class);
        startActivity(intent);
    }

    public void firestoreui(View view) {
        Intent intent = new Intent(MainActivity.this, FirebaseUiActivity.class);
        startActivity(intent);
    }

    public void logout(View view) {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                      linearLayout.setVisibility(View.GONE);
                      button.setVisibility(View.VISIBLE);
                       // finish();
                    }
                });
    }

    public void remove(View view) {
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            linearLayout.setVisibility(View.GONE);
                            button.setVisibility(View.VISIBLE);
                        } else {
                            // Deletion failed
                        }
                    }
                });
    }

    public void signin(View view) {
    }
}
