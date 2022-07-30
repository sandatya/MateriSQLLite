package com.santara.materisqlite;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button btAddData;
    GridView gridViewData;

    ArrayList<MyMahasiswa> myMahasiswas=new ArrayList<MyMahasiswa>();
    DatabaseHelper dbhelperMain=new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridViewData=(GridView) findViewById(R.id.gridViewDataAll);
        btAddData=(Button) findViewById(R.id.buttonAddData);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1001);
        }

        if(dbhelperMain.getCountData()>0){
            myMahasiswas=dbhelperMain.transferArrayList();
            if (myMahasiswas.size()>0){
                setAdapterListView();
            }
        }

        btAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,MainActivity2.class);
                intent.putExtra("Mode","insert");
                startActivity(intent);
            }
        });
    }


    private class MyListAdapter extends ArrayAdapter<MyMahasiswa>{
        public MyListAdapter(){
            super(MainActivity.this,R.layout.ls_item,myMahasiswas);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View itemview = convertView;
            if (itemview==null){
                itemview=getLayoutInflater().inflate(R.layout.ls_item,parent,false);
            }

            MyMahasiswa mhs = myMahasiswas.get(position);
            ImageView imgfoto =(ImageView) itemview.findViewById(R.id.imageViewItem);

            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize=16;

            Bitmap img=BitmapFactory.decodeFile(mhs.getPath(),options);

            myImage mi = new myImage();
            img=mi.automatic_rotateImg(img,mhs.getPath());
            imgfoto.setImageBitmap(img);

            TextView txNim = (TextView) itemview.findViewById(R.id.textViewNim);
            txNim.setText(mhs.getNim());
            TextView txNama = (TextView) itemview.findViewById(R.id.textViewNama);
            txNama.setText(mhs.getNama());
            TextView txUmur = (TextView) itemview.findViewById(R.id.textViewUmur);
            txUmur.setText(String.valueOf(mhs.getUmur()));
            TextView txPath = (TextView) itemview.findViewById(R.id.textViewPath);
            txPath.setText(mhs.getPath());
            return itemview;
        }
    }
    private void setAdapterListView(){
        ArrayAdapter<MyMahasiswa> adapter = new MyListAdapter();

        gridViewData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,MainActivity2.class);
                MyMahasiswa mhsData = myMahasiswas.get(position);
                intent.putExtra("Mode","UpdateDelete");
                intent.putExtra("Nim",mhsData.getNim());
                intent.putExtra("Nama",mhsData.getNama());
                intent.putExtra("Umur",mhsData.getUmur());
                intent.putExtra("Path",mhsData.getPath());
                startActivity(intent);

            }
        });
                gridViewData.setAdapter(adapter);
    }
    
}