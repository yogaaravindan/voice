package com.example.uidesign;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateVoiceFormActivity extends AppCompatActivity {

    EditText etformname;
    String formname;
    Button btnformname;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,demoref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_voice_form);

        etformname = findViewById(R.id.et_formname);
        btnformname = findViewById(R.id.btn_formname);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        demoref = databaseReference.child( "Forms" );

        btnformname.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uploadnametofirebase();
                Toast.makeText( CreateVoiceFormActivity.this , "Form Name Created" , Toast.LENGTH_SHORT ).show();
            }
        } );

    }
    private void Uploadnametofirebase() {
        formname = etformname.getText().toString().trim();
        demoref.child( formname ).setValue( formname );
        sendusertoAdd_Field_Activity();

    }
    private void sendusertoAdd_Field_Activity() {
        Intent Add_Field_intent = new Intent( CreateVoiceFormActivity.this,AddFieldActivity.class );
        Add_Field_intent.putExtra( "FORMNAME",formname );
        startActivity( Add_Field_intent );
    }
}
