package com.example.simpleblog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PostActivity extends AppCompatActivity {

    private ImageButton seleccionarImagen;
    private EditText postTitle, postDescription;
    private Button publicar;

    private Uri imageUri = null;
    private static final int GALLERY_REQUEST = 1;
    private StorageReference almacenamiento;

    private ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        almacenamiento = FirebaseStorage.getInstance().getReference();

        seleccionarImagen = (ImageButton) findViewById(R.id.imageSelect);
        postTitle = (EditText) findViewById(R.id.titleField);
        postDescription = (EditText) findViewById(R.id.desField);
        publicar = (Button) findViewById(R.id.publicarbtn);


        seleccionarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galeriaIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galeriaIntent.setType("image/*");
                startActivityForResult(galeriaIntent,GALLERY_REQUEST);
            }
        });


        publicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starPosting();
            }
        });
    }

    private void starPosting() {
        String title_val = postTitle.getText().toString().trim();
        String des_val = postDescription.getText().toString().trim();

        if (!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(des_val) && imageUri != null){
            //referencia de almacenamiento
            StorageReference rutarchivo = almacenamiento.child("Blog_images").child(imageUri.getLastPathSegment());

            /*rutarchivo.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri descargar = taskSnapshot.getDownload();
                }
            });*/




        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    if(requestCode == GALLERY_REQUEST && requestCode == RESULT_OK){
        imageUri = data.getData();

        seleccionarImagen.setImageURI(imageUri);
        }
    }
}
