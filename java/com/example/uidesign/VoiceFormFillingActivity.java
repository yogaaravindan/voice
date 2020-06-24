package com.example.uidesign;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;

public class VoiceFormFillingActivity extends AppCompatActivity {

    final ArrayList<String> list = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, demoref;
    TextView fieldtv;
    EditText dataet;
    ImageView micicon, speakericon;
    Button nextbtn, okbtn;
    int index = 0;
    String a, b, c;
    ListView listView;
    public static final int Recogniser_result = 1;
    private TextToSpeech mtts;
    int pitch = 1;
    int speed = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_voice_form_filling );
        listView = (ListView) findViewById( R.id.listview );
        speakericon = (ImageView) findViewById( R.id.speakericon );
        //textView = (TextView)findViewById( R.id.textView3 );
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>( this , android.R.layout.simple_list_item_1 , list );
        //listView.setAdapter( arrayAdapter );
        String formname3 = getIntent().getStringExtra( "FORMNAME3" );
        String user = getIntent().getStringExtra( "NAME" );
        databaseReference = FirebaseDatabase.getInstance().getReference().child( "Forms" ).child( formname3 );
        demoref = FirebaseDatabase.getInstance().getReference().child( "Response" ).child( formname3 ).child( user );
        fieldtv = (TextView) findViewById( R.id.textView4 );
        dataet = (EditText) findViewById( R.id.data );
        micicon = (ImageView) findViewById( R.id.micicon );
        nextbtn = (Button) findViewById( R.id.button2 );
        okbtn = (Button) findViewById( R.id.button );


        if (fieldtv.getText() == null) {
            okbtn.setEnabled( false );

        }

        okbtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( VoiceFormFillingActivity.this , "clicked" , Toast.LENGTH_SHORT ).show();
                uploadToDatabase();
            }
        } );


        micicon.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voicerecognition();

            }
        } );
        databaseReference.addChildEventListener( new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot , @Nullable String s) {
                list.add( dataSnapshot.getKey() );
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot , @Nullable String s) {
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot , @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
        //fieldtv.setText(arrayAdapter.getItem( index ));
        nextbtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                okbtn.setEnabled( true );


                dataet.getText().clear();
                if (index != arrayAdapter.getCount()) {
                    a = arrayAdapter.getItem( index );
                    if (a.contains( "*" )) {
                        Toast.makeText( VoiceFormFillingActivity.this , "REQUIRED FIELD" , Toast.LENGTH_LONG ).show();
                        Toast.makeText( VoiceFormFillingActivity.this , "please enter this field or else your form will not be validated" , Toast.LENGTH_SHORT ).show();

                    }
                    fieldtv.setText( "ENTER YOUR " + a );

                    index++;
                } else {
                    Toast.makeText( VoiceFormFillingActivity.this , "YOUR RESPONSE HAS BEEN SUBMITTED" , Toast.LENGTH_SHORT ).show();
                    dataet.setText( "" );
                    nextbtn.setEnabled( false );
                    sendusertoMainActivity();
                }

            }
        } );

        mtts = new TextToSpeech( this , new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int reult = mtts.setLanguage( Locale.ENGLISH );
                    if (reult == TextToSpeech.LANG_MISSING_DATA || reult == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText( VoiceFormFillingActivity.this , "LANGUAGE NOT SUPPORTED" , Toast.LENGTH_SHORT ).show();
                    } else {
                        speakericon.setEnabled( true );

                    }
                } else {
                    Toast.makeText( VoiceFormFillingActivity.this , "INITIALIZATION FAILED" , Toast.LENGTH_SHORT ).show();
                }

            }
        } );
        speakericon.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        } );
    }

    private void sendusertoMainActivity() {
        Intent intent = new Intent( VoiceFormFillingActivity.this , MainActivity.class );
        startActivity( intent );
    }

    private void speak() {
        String TEXT = fieldtv.getText().toString();

        mtts.setPitch( pitch );
        mtts.setSpeechRate( speed );

        mtts.speak( TEXT , TextToSpeech.QUEUE_FLUSH , null );
    }

    @Override
    protected void onDestroy() {
        if (mtts != null) {
            mtts.stop();
            mtts.shutdown();
        }
        super.onDestroy();
    }

    private void voicerecognition() {
        Intent speechintent = new Intent( RecognizerIntent.ACTION_RECOGNIZE_SPEECH );
        speechintent.putExtra( RecognizerIntent.EXTRA_LANGUAGE_MODEL , RecognizerIntent.LANGUAGE_MODEL_FREE_FORM );
        speechintent.putExtra( RecognizerIntent.EXTRA_PROMPT , "speech to text" );
        startActivityForResult( speechintent , Recogniser_result );
    }

    @Override
    public void onActivityResult(int requestCode , int resultCode , @Nullable Intent data) {

        if (requestCode == Recogniser_result && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra( RecognizerIntent.EXTRA_RESULTS );
            b = matches.get( 0 );
            dataet.setText( b );
        }
        super.onActivityResult( requestCode , resultCode , data );

    }

    private void uploadToDatabase() {
        c = dataet.getText().toString();
        nextbtn.setEnabled( true );
        demoref.child( a ).push().setValue( c );
        c = "";
    }
}