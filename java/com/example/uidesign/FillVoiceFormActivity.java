package com.example.uidesign;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FillVoiceFormActivity extends AppCompatActivity {

    EditText etsearchform,etname;
    Button btnsearchform;
    String userformname1;
    String user="";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,demoref;

    ArrayList<String> FORMLIST =new ArrayList<String>(  );
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_voice_form);
        etname = findViewById(R.id.etname);
        etsearchform = findViewById(R.id.etsearchform);
        btnsearchform = findViewById(R.id.btnsearchform);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        demoref = databaseReference.child( "Forms" );

        btnsearchform.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateform();
                user = etname.getText().toString();
            }
        } );

    }

    private void validateform() {
        final String userformname = etsearchform.getText().toString().trim();
        userformname1=userformname;
        demoref.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild( userformname )){
                    Toast.makeText( FillVoiceFormActivity.this , "Form Found!" , Toast.LENGTH_SHORT ).show();
                    sendusertoVoice_Form_Filling_Activity();

                }
                else {
                    Toast.makeText( FillVoiceFormActivity.this , "no result" , Toast.LENGTH_SHORT ).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }
    private void sendusertoVoice_Form_Filling_Activity() {
        Intent Voice_Form_Filling_Activityintent = new Intent( FillVoiceFormActivity.this,VoiceFormFillingActivity.class );
        Voice_Form_Filling_Activityintent.putExtra( "FORMNAME3",userformname1);
        Voice_Form_Filling_Activityintent.putExtra( "NAME",user );
        startActivity(Voice_Form_Filling_Activityintent);
    }
}