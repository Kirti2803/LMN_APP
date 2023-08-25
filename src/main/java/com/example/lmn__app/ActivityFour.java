package com.example.lmn__app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class ActivityFour extends AppCompatActivity {

    private EditText editText1;
    Button btn;

    StorageReference storageReference;
    DatabaseReference databaseReference;
    private Uri pdfData;
    private String pdfName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four);

//        editText=findViewById(R.id.editTextTextPersonName6);
//        editText=findViewById(R.id.editTextTextPersonName);
        editText1=findViewById(R.id.editTextTextPersonName2);
//        btn=findViewById(R.id.button5);
        btn=findViewById(R.id.button8);

        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference("uploadPDF");

        btn.setEnabled(false);

        editText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPDF();
            }
        });


    }

    private void selectPDF() {
        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"PDF FILE SELECT"),12);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        pdfData = data.getData();
        if(pdfData.toString().startsWith("content://")){
            Cursor cursor=null;
            try {
                cursor=ActivityFour.this.getContentResolver().query(pdfData,null,null,null,null);
                if (cursor!=null && cursor.moveToFirst()){
                    pdfName=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)+1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(pdfData.toString().startsWith("file://")){
            pdfName=new File(pdfData.toString()).getName();
        }
        editText1.setText(pdfName);

        if(requestCode==12 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            btn.setEnabled(true);
            editText1.setText(data.getDataString()
                    .substring(data.getDataString().lastIndexOf("/")+1));

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadPDFFileFirebase(data.getData());
                }
            });
        }

    }

    private void uploadPDFFileFirebase(Uri data) {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("file is loading");
        progressDialog.show();

        StorageReference reference=storageReference.child(("uploadPDF"+System.currentTimeMillis()+".pdf"));

        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uriTask =taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        Uri uri=uriTask.getResult();

                        putPDF putPDF=new putPDF(editText1.getText().toString(),uri.toString());
                        databaseReference.child(databaseReference.push().getKey()).setValue(putPDF);
                        Toast.makeText(ActivityFour.this, "file is uploaded", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress=(100.0* snapshot.getBytesTransferred())/ snapshot.getTotalByteCount();
                        progressDialog.setMessage("file uploaded.."+(int) progress+"%");
                    }
                });

    }
}