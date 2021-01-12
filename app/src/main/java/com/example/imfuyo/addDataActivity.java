package com.example.imfuyo;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.text.TextUtils.isEmpty;

public class addDataActivity extends AppCompatActivity {



    private Button add;
    private EditText namaTernak, namaPeternak, jenisTernak, kondisi;
    private ImageView fotoSapi;
    private FirebaseAuth auth;
    private String url;
    private ProgressBar pgBar;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    String currentPhotoPath;
    public Uri contentUri;
    public static final String TAG = "TAG";
    FirebaseFirestore fStore;
    StorageReference storageReference;
    String filename="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();


        namaTernak = findViewById(R.id.idNamaTernak);
        pgBar=findViewById(R.id.pg_bar);
        namaPeternak = findViewById(R.id.idNamaPeternak);
        jenisTernak = findViewById(R.id.textView12);
        kondisi = findViewById(R.id.textView9);
        fotoSapi = findViewById(R.id.idFotoSapi);
        fotoSapi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        add = findViewById(R.id.addButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filename!=null&&contentUri!=null) {
                    if(namaTernak.getText().toString().equals("")
                            ||namaPeternak.getText().toString().equals("")
                            ||kondisi.getText().toString().equals("")
                            ||jenisTernak.getText().toString().equals("")){
                        Toast.makeText(addDataActivity.this, "Data Harus Lengkap", Toast.LENGTH_SHORT).show();
                    }else {
                        uploadImageToFirebase(filename, contentUri);
                    }
                }else{
                    Toast.makeText(addDataActivity.this, "Pilih Gambar Terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void addDataToRTDB(){
        String getUserID = auth.getCurrentUser().getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference getReference;

        //Menyimpan Data yang diinputkan User kedalam Variable
        String getNamaTernak = namaTernak.getText().toString();
        String getNamaPeternak = namaPeternak.getText().toString();
        String getJenisTernak = jenisTernak.getText().toString();
        String getKondisiTernak = kondisi.getText().toString();


        getReference = database.getReference(); // Mendapatkan Referensi dari Database

        // Mengecek apakah ada data yang kosong
        if(isEmpty(getNamaTernak) && isEmpty(getNamaPeternak) && isEmpty(getJenisTernak)){
            //Jika Ada, maka akan menampilkan pesan singkan seperti berikut ini.
            Toast.makeText(addDataActivity.this, "Data tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show();
        }else {
        /*
        Jika Tidak, maka data dapat diproses dan meyimpannya pada Database
        Menyimpan data referensi pada Database berdasarkan User ID dari masing-masing Akun
        */
            getReference.child("Owner").child(getUserID).child("Ternak").push()
                    .setValue(new LiveStockData(getNamaTernak, getNamaPeternak, getJenisTernak,url, getKondisiTernak));
            pgBar.setVisibility(View.GONE);
            Toast.makeText(addDataActivity.this, "Data berhasi; ditambahkan!", Toast.LENGTH_SHORT).show();
            open();
        }
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(addDataActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    askCameraPermissions();
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(gallery, GALLERY_REQUEST_CODE);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private boolean askCameraPermissions() {
        int ExtstorePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (ExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded
                            .toArray(new String[listPermissionsNeeded.size()]),
                    CAMERA_PERM_CODE);
            return false;
        } else {
            dispatchTakePictureIntent();
        }
        return true;


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Camera Permission is Required to Use camera & storage.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                File f = new File(currentPhotoPath);
                fotoSapi.setImageURI(Uri.fromFile(f));
                Log.d("tag", "ABsolute Url of Image is " + Uri.fromFile(f));

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
                filename=f.getName();
                //uriFoto=contentUri;
                //uploadImageToFirebase(f.getName(), contentUri);

            }

        }

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);
                Log.d("tag", "onActivityResult: Gallery Image Uri:  " + imageFileName);
                fotoSapi.setImageURI(contentUri);
                filename=imageFileName;
                //uploadImageToFirebase(imageFileName, contentUri);
            }

        }


    }

    private void uploadImageToFirebase(String name, Uri contentUri) {
        pgBar.setVisibility(View.VISIBLE);
        final StorageReference profileRef = storageReference.child("users/" + auth.getCurrentUser().getUid() + "/profile.jpg");
        profileRef.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("tag", "onSuccess: Uploaded Image URl is " + uri.toString());
                        url=uri.toString();
                        addDataToRTDB();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(addDataActivity.this, "Upload Failled.", Toast.LENGTH_SHORT).show();
                pgBar.setVisibility(View.GONE);
            }
        });

    }


    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }
    public void open(){
        Intent intent = new Intent(this, LiveStockOwnerActivity.class);
        startActivity(intent);
    }
}