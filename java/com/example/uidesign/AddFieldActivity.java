package com.example.uidesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddFieldActivity extends AppCompatActivity implements ExampleDialog.ExampleDialogListner {

    TextView tvfield;
    ListView lvfield;
    Button createformbtn,addfield;
    ArrayList<String> FieldList=new ArrayList<>(  );
    String formname1;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,demoref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_field);

        tvfield = findViewById(R.id.tvfield);
        lvfield = findViewById(R.id.Field_view);
        createformbtn = findViewById(R.id.createBtn);
        addfield = findViewById(R.id.btnaddfield);

        formname1 = getIntent().getStringExtra( "FORMNAME" );
        databaseReference = FirebaseDatabase.getInstance().getReference();
        demoref = databaseReference.child( "Forms" ).child( formname1 );

        addfield.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDailog();
            }
        } );

        createformbtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatefieldtodatabase();
                sendusertoMainActivity();
            }
        } );

    }

    private void sendusertoMainActivity() {
        Intent mainActivityintent=new Intent( AddFieldActivity.this, MainActivity.class );
        startActivity(mainActivityintent);
    }

    private void updatefieldtodatabase() {
        for(int i=0;i<FieldList.size();i++){
            String temp = FieldList.get( i );
            demoref.child(temp).push().setValue( temp );

        }
        Toast.makeText( this , "FORM HAS BEEN CREATED" , Toast.LENGTH_SHORT ).show();
    }
    private void openDailog() {

        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show( getSupportFragmentManager(),"example dialog" );
    }

    @Override
    public void applyTexts(String Field) {
        FieldList.add( Field );

        ArrayAdapter arrayAdapter = new ArrayAdapter( this,android.R.layout.simple_list_item_1,FieldList);
        lvfield.setAdapter( arrayAdapter );
    }

}
