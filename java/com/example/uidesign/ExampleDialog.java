package com.example.uidesign;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

public class ExampleDialog extends DialogFragment {
    private EditText fieldet;
    private ExampleDialogListner listner;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate( R.layout.layout_dialog ,null);

        builder.setView( view ).setTitle( "ADD FIELD" ).setNegativeButton( "CANCEL" , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog , int which) {

            }
        } ).setPositiveButton( "ok" , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog , int which) {
                String field = fieldet.getText().toString();
                listner.applyTexts( field );

            }
        } );
        fieldet = view.findViewById( R.id.Fieldet );
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach( context );
        try {
            listner = (ExampleDialogListner) context;
        } catch (ClassCastException e) {
            throw new ClassCastException( context.toString()+ "dialog not opened" );
        }
    }

    public interface ExampleDialogListner{
        void applyTexts(String Field);
    }
}

