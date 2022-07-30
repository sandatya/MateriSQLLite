package com.santara.materisqlite;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {
    public static  final int PICKFILE_RESULT_CODE=100;
    Button btInsert,btUpdate,btDelete,btView;
    EditText etNim,etNama,etUmur;
    ImageView imgFoto;
    DatabaseHelper dbhelper=new DatabaseHelper(this);
    String path=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btInsert = (Button) findViewById(R.id.buttonInsert);
        btUpdate = (Button) findViewById(R.id.buttonUpdate);
        btDelete = (Button) findViewById(R.id.buttonDelete);
        btView = (Button) findViewById(R.id.buttonView);
        etNim = (EditText) findViewById(R.id.editTextNim);
        etNama = (EditText) findViewById(R.id.editTextNama);
        etUmur = (EditText) findViewById(R.id.editTextUmur);
        imgFoto = (ImageView) findViewById(R.id.imageViewFoto);

        final Intent intent = getIntent();
        String ModeResult = intent.getStringExtra("Mode");

        if (ModeResult.equalsIgnoreCase("UpdateDelete")){
            etNim.setText(intent.getStringExtra("Nim"));
            etNama.setText(intent.getStringExtra("Nama"));
            etUmur.setText(String.valueOf(intent.getIntExtra("Umur",0)));
            path=intent.getStringExtra("Path");
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap pic = BitmapFactory.decodeFile(path,options);
            myImage mi = new myImage();
            pic=mi.automatic_rotateImg(pic,path);
            imgFoto.setImageBitmap(pic);
            etNim.setEnabled(false);
            btInsert.setEnabled(false);
        }else if(ModeResult.equalsIgnoreCase("insert")){
            btInsert.setEnabled(true);
            btDelete.setEnabled(false);
            btUpdate.setEnabled(false);
            imgFoto.setImageResource(R.mipmap.ic_launcher_round);
        }

        imgFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooseFile = new Intent (Intent.ACTION_GET_CONTENT);
                chooseFile.setType("image/jpeg|image/png");
                chooseFile=Intent.createChooser(chooseFile,"Choose File : ");
                startActivityForResult(chooseFile,PICKFILE_RESULT_CODE);
            }
        });

        btInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAlert("insert");
            }
        });

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAlert("update");
            }
        });

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAlert("delete");
            }
        });

        btView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private String getRealPathFromURI(Uri contentURI){
        String result;
        Cursor cursor = getContentResolver().query(contentURI,null,null,null,null);
        if(cursor==null){
            result=contentURI.getPath();
        }else{
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result =cursor.getString(idx);
            cursor.close();
        }

        return result;
    }

    private void CleanComponent(){
        etUmur.setText("");
        etNim.setText("");
        etNama.setText("");
        imgFoto.setImageResource(R.mipmap.ic_launcher_round);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data==null){
            return;
        }
        switch (requestCode){
            case PICKFILE_RESULT_CODE:
                Uri fileUri=data.getData();
                path=getRealPathFromURI(fileUri);
                try{
                    BitmapFactory.Options options= new BitmapFactory.Options();
                    options.inSampleSize=0;
                    Bitmap mypic=BitmapFactory.decodeFile(path,options);
                    myImage mi = new myImage();
                    mypic=mi.automatic_rotateImg(mypic,path);
                    imgFoto.setImageBitmap(mypic);
                }catch(Exception e){
                    e.printStackTrace();
                }
                break;
        }
    }

    private void showDialogAlert(final String mode){
        AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
        int buttonpic=0;
        String message=null;
        switch (mode){
            case "insert":
                buttonpic=R.drawable.save_icon;
                alBuilder.setTitle("Do You Sure to Save Data?");
                message="Click Yes to save data";
                break;
            case "update":
                buttonpic=R.drawable.update_icon;
                alBuilder.setTitle("Do You Sure to Update Data?");
                message="Click Yes to update data";
                break;
            case "delete":
                buttonpic=R.drawable.delete_icon;
                alBuilder.setTitle("Do You Sure to Delete Data?");
                message="Click Yes to delete data";
                break;
        }

        alBuilder
                .setMessage(message)
                .setIcon(buttonpic)
                .setCancelable(false)

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (mode) {
                            case "insert":
                                MyMahasiswa mhs = dbhelper.getDataExist(etNim.getText().toString());
                                if (mhs == null) {
                                    MyMahasiswa mhs2 = new MyMahasiswa(etNim.getText().toString(), etNama.getText().toString()
                                            , path, Integer.parseInt(etUmur.getText().toString()));

                                    boolean benar = dbhelper.insertData(mhs2);

                                    if (benar) {
                                        Toast.makeText(getBaseContext(), "Insert Sukses", Toast.LENGTH_LONG).show();
                                        CleanComponent();
                                    } else {
                                        Toast.makeText(getBaseContext(), "Insert Gagal", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(getBaseContext(), "Nim dimiliki : " +
                                            mhs.getNama(), Toast.LENGTH_LONG).show();
                                }
                                break;
                            case "update":
                                if (dbhelper.updateData(etNim.getText().toString(), etNama.getText().toString(),
                                        path, Integer.parseInt(etUmur.getText().toString()))) {
                                    Toast.makeText(getBaseContext(), "update sukses", Toast.LENGTH_LONG).show();
                                    CleanComponent();
                                } else {
                                    Toast.makeText(getBaseContext(), "update gagal", Toast.LENGTH_LONG).show();
                                }
                                break;
                            case "delete":
                                if (dbhelper.deleteData(etNim.getText().toString()) > 0) {
                                    Toast.makeText(getBaseContext(), "Delete Sukses", Toast.LENGTH_SHORT).show();
                                    CleanComponent();
                                } else {
                                    Toast.makeText(getBaseContext(), "Delete Gagal", Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }
                    }
                })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            switch (mode){
                                case "insert":
                                    Toast.makeText(getBaseContext(), "Insert dibatalkan", Toast.LENGTH_SHORT).show();
                                    break;
                                case "update":
                                    Toast.makeText(getBaseContext(), "Update dibatalkan", Toast.LENGTH_SHORT).show();
                                    break;
                                case "delete":
                                    Toast.makeText(getBaseContext(), "Insert dihapus", Toast.LENGTH_SHORT).show();
                                    break;
                        }
                    }

        });

        AlertDialog alertDialog=alBuilder.create();
        alertDialog.show();


    }
}

