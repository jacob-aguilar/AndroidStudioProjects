package com.example.mediaapp;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mediaapp.models.Datas;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import static androidx.appcompat.app.AppCompatActivity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference; //ojo aqui

    DatabaseReference dataRefe;
    private StorageTask uploadTaks;

    StorageReference storageReference;
    String storagePath = "Users_Profile_Cover_Imgs/";

    ImageView avatarIv, coverIv;
    TextView nameTv, emailTv, phoneTv;
    FloatingActionButton fab;
    ProgressDialog pd;
    Button up, ch;
    EditText txtName, txtPhone, tztdirection;
    Datas datas;

    //Permisos
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PIC_GALLERY_CODE = 300;
    private static final int IMAGE_PIC_CAMERA_CODE = 400;

    String camaraPermmitions[];
    String storagePermmitions[];

    Uri image_uri;
    public Uri imguri;

    String profileOrCoverPhoto;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
      //  storageReference = getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference("Images");

        //int arrays of permitions
        camaraPermmitions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermmitions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        dataRefe = FirebaseDatabase.getInstance().getReference().child("Datas");

        coverIv = view.findViewById(R.id.coverIv);
        nameTv = view.findViewById(R.id.nameTvc);
        emailTv = view.findViewById(R.id.emailTv);
        phoneTv = view.findViewById(R.id.phoneTv);
        fab = view.findViewById(R.id.fab);
        up = view.findViewById(R.id.btnupload);
        ch = view.findViewById(R.id.btnchoose);
        avatarIv = view.findViewById(R.id.avatarIv);
        txtName = view.findViewById(R.id.txtname);
        txtPhone = view.findViewById(R.id.txtbrand);
        tztdirection = view.findViewById(R.id.tztdirection);
        datas = new Datas();

        pd = new ProgressDialog(getActivity());


        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String name = ""+ ds.child("name").getValue();
                    String email = ""+ ds.child("email").getValue();
                    String phone = ""+ ds.child("phone").getValue();
                    String image = ""+ ds.child("image").getValue();
                    String cover  = ""+ ds.child("cover").getValue();

                    //Set data
                    nameTv.setText(name);
                    emailTv.setText(email);
                    phoneTv.setText(phone);

                    try {
                        Picasso.get().load(image).into(avatarIv);
                    }catch (Exception e){
                        Picasso.get().load(R.drawable.ic_default_img_whine).into(avatarIv);
                    }


                    try {
                        Picasso.get().load(cover).into(coverIv);
                    }catch (Exception e){
                       // Picasso.get().load(R.drawable.ic_default_img_whine).into(coverIv);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //BotonFlotante
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditProfileDialog();
            }
        });

        ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileChoose();
            }
        });


        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uploadTaks != null && uploadTaks.isInProgress()){
                    Toast.makeText(getActivity(), "Upload in process", Toast.LENGTH_SHORT).show();
                }else {
                    Fileuploader();
                }

            }
        });

        return view;
    }

    private String getExtension(Uri uri){
        ContentResolver cr = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));

    }

    private void Fileuploader() {
        String imageid;
        imageid = System.currentTimeMillis()+"."+getExtension(imguri);
        datas.setName(txtName.getText().toString().trim());
        datas.setPhone(txtPhone.getText().toString().trim());
        datas.setDireccion(tztdirection.getText().toString().trim());
        datas.setImageId(imageid);
        dataRefe.push().setValue(datas);



        StorageReference Ref = storageReference.child(imageid);
        uploadTaks = Ref.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                      //  Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(getActivity(), "image upload susccesfully", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });

    }

    private void FileChoose() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    private boolean checkStoragePermitions() {
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermitions(){
        requestPermissions(storagePermmitions, STORAGE_REQUEST_CODE);
    }


    private boolean checkCameraPermitions(){
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);


        boolean result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;

    }

    private void requestCameraPermitions(){
        requestPermissions(camaraPermmitions, CAMERA_REQUEST_CODE);
        //ActivityCompat.requestPermissions(getActivity(), camaraPermmitions, CAMERA_REQUEST_CODE);
    }

    private void showEditProfileDialog() {
        String options[] = {"Edit perfil", "Edit Cover Foto", "Edit name", "edit phone"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose actions");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               if (which == 0){
                   pd.setMessage("Updating Profile picture");
                   profileOrCoverPhoto = "image";
                   showImagePictureDialog();

               }else  if (which == 1){
                   pd.setMessage("Updating Cover photo");
                   profileOrCoverPhoto = "cover";
                   showImagePictureDialog();

               }else  if (which == 2){
                   pd.setMessage("Updating name");
                   showNamePhoneUpdateDialog("name");

               }else  if (which == 3){
                   pd.setMessage("Updating Phone");
                   showNamePhoneUpdateDialog("phone");

               }
            }
        });

        builder.create().show();
    }

    //here
    private void showNamePhoneUpdateDialog(final String key) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Update"+ key); //update name or update phone

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10, 10, 10, 10);

        final EditText editText= new EditText(getActivity());
        editText.setHint("Enter"+ key);
        linearLayout.addView(editText);

        builder.setView(linearLayout);


        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String value = editText.getText().toString().trim();
                //validate
                if (!TextUtils.isEmpty(value)){
                    pd.show();
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(key, value);

                    databaseReference.child(user.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), "Updated...", Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                        public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

                }else {
                    Toast.makeText(getActivity(), "Please enter ", Toast.LENGTH_SHORT).show();//ojo

                }

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        builder.create().show();
    }

    private void showImagePictureDialog() {

        String options [] = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pic image from");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    if (!checkCameraPermitions()){
                        requestCameraPermitions();
                    }else {
                        pickFromCamera();
                    }

                }else  if (which == 1){
                    if (!checkStoragePermitions()){
                        requestStoragePermitions();
                    }else {
                        pickFromGallery();
                    }
                }
            }
        });

        builder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//Press deny or allow

        switch (requestCode){
            case CAMERA_REQUEST_CODE:{

                if (grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && writeStorageAccepted){
                        pickFromCamera();
                    }
                    else {
                        Toast.makeText(getActivity(), "Please enable camera and storage permmision", Toast.LENGTH_SHORT).show();


                    }

                }
            }
            break;

            case STORAGE_REQUEST_CODE:{



                if (grantResults.length > 0){
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (writeStorageAccepted){
                        pickFromGallery();
                    }
                    else {
                        Toast.makeText(getActivity(), "Please enable storage permmision", Toast.LENGTH_SHORT).show();


                    }

                }

            }
            break;

        }

        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //After picking image from camera or gallery
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK){
            if (requestCode == IMAGE_PIC_GALLERY_CODE){
                //here
                image_uri = data.getData();
                uploadProfileCoverPhoto(image_uri);
            }
            if (requestCode == IMAGE_PIC_CAMERA_CODE){
                uploadProfileCoverPhoto(image_uri);
            }
        }

         /*if (resultCode == IMAGE_PIC_GALLERY_CODE && resultCode == IMAGE_PIC_CAMERA_CODE  && resultCode==RESULT_OK && data !=null && data.getData()!=null ){
            image_uri=data.getData();
            avatarIv.setImageURI(imguri);

        }*/



        /*if (requestCode ==1 && resultCode==RESULT_OK && data !=null && data.getData()!=null ){
            imguri=data.getData();
            avatarIv.setImageURI(imguri);

        }*/
    }

    private void uploadProfileCoverPhoto(Uri uri) {
        pd.show();

        String filePathAndName = storagePath+ ""+ profileOrCoverPhoto +"_"+ user.getUid();

        StorageReference storageReference2 = storageReference.child(filePathAndName);
        storageReference2.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                while (!uriTask.isSuccessful());
                Uri downloadUri = uriTask.getResult();


                //chequear si ha sido actualizaada o no y el url es recibido
                if (uriTask.isSuccessful()){
                   //image unload
                    // add update image in users firebase
                    HashMap<String, Object> results = new HashMap<>();

                    results.put(profileOrCoverPhoto, downloadUri.toString());

                    databaseReference.child(user.getUid()).updateChildren(results)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), "Image Upldated", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getActivity(), "Error updating image", Toast.LENGTH_SHORT).show();

                        }
                    });


                }else {
                    //error
                    pd.dismiss();
                    Toast.makeText(getActivity(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            pd.dismiss();
                Toast.makeText(getActivity(),  e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private  void pickFromCamera(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp description");

        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraInten = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraInten.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraInten, IMAGE_PIC_CAMERA_CODE);

    }

    private void pickFromGallery(){
        Intent galleryInten = new Intent(Intent.ACTION_PICK);
        galleryInten.setType("image/*");
        startActivityForResult(galleryInten, IMAGE_PIC_GALLERY_CODE);
    }


    private void chechUserUserStatus(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){

            //  mUID = user.getUid();
            //  SharedPreferences sp = this.getActivity().getSharedPreferences("SP_USER", MODE_PRIVATE);
            // SharedPreferences.Editor editor = sp.edit();
            //  editor.putString("Current_USERID", mUID);
            // editor.apply();
        }else {
            //Toast.makeText(getContext(), "Nuevo", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout){
            firebaseAuth.signOut();
            chechUserUserStatus();
        }
        return super.onOptionsItemSelected(item);
    }


}
