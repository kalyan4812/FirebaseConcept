package com.example.firebaseconcept.FirebaseStorage;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebaseconcept.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
    private Context context;
    private List<Upload> uploads;
    private com.google.firebase.storage.FirebaseStorage firebaseStorage;

    public MyAdapter(Context context, List<Upload> uploads) {
        this.context = context;
        this.uploads = uploads;
        this.firebaseStorage = FirebaseStorage.getInstance();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {

        Upload uploadCurrent = uploads.get(position);
        holder.textViewName.setText(uploadCurrent.getTitle());
        Picasso.get()
                .load(uploadCurrent.getImageUrl())

                .into(holder.imageView);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                v.showContextMenu();
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder implements MenuItem.OnMenuItemClickListener, View.OnCreateContextMenuListener {
        public TextView textViewName;
        public ImageView imageView;


        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                switch (item.getItemId()) {
                    case 1:
                        Toast.makeText(context, "DO Whatever", Toast.LENGTH_SHORT).show();
                        return true;
                    case 2:
                        final String key = uploads.get(position).getKey();
                        StorageReference storageReference = firebaseStorage.getReferenceFromUrl(uploads.get(position).getImageUrl());
                        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                FirebaseDatabase.getInstance().getReference("uploads").child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context, "DELETED", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        });


                        return true;
                }
            }
            return false;
        }

        public MyHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            imageView = itemView.findViewById(R.id.image_view_upload);
            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem doWhatever = menu.add(Menu.NONE, 1, 1, "Do whatever");
            MenuItem delete = menu.add(Menu.NONE, 2, 2, "Delete");
            doWhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }
    }

}
