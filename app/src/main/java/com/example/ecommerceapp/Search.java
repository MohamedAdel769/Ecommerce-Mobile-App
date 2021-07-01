package com.example.ecommerceapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jetbrains.annotations.NotNull;

import java.io.Console;
import java.util.ArrayList;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class Search extends Fragment implements View.OnClickListener{

    private EditText srch_text;
    private Button srch_qr, srch_voice, search;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        //setHasOptionsMenu(true);

        srch_text = view.findViewById(R.id.srchTxt);
        srch_qr = view.findViewById(R.id.srchQr);
        srch_voice = view.findViewById(R.id.srchVoice);
        search = view.findViewById(R.id.srchBtn);

        srch_qr.setOnClickListener(this);
        srch_voice.setOnClickListener(this);
        search.setOnClickListener(this);

        return view;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.srchQr:
                search_QR();
                break;
            case R.id.srchVoice:
                search_voice();
                break;
            case R.id.srchBtn:
                String srch_val = srch_text.getText().toString().trim();
                if(!Helper.handleExp_Edit(srch_text)) return;
                Helper.srch_text = srch_val;
                Helper.srch_method = 0;
                srch_text.setText("");
                ViewPager mviewPager = getActivity().findViewById(R.id.view_pager);
                mviewPager.setCurrentItem(0);
                break;
        }
    }

    private void search_QR(){
        try {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);

        } catch (Exception e) {
            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
            startActivity(marketIntent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                Helper.srch_qr = contents.trim();
                Helper.srch_method = 1;
                ViewPager mviewPager = getActivity().findViewById(R.id.view_pager);
                mviewPager.setCurrentItem(0);
                //Toast.makeText(getContext(), contents, Toast.LENGTH_LONG).show();
            }
        }
        else if(requestCode == 100){
            if (resultCode == RESULT_OK && data != null) {
                ArrayList result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String contents = result.get(0).toString().toLowerCase();
                Helper.srch_voice = contents;
                Helper.srch_method = 2;
                ViewPager mviewPager = getActivity().findViewById(R.id.view_pager);
                mviewPager.setCurrentItem(0);
                //Toast.makeText(getContext(), contents, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void search_voice(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak");
        try {
            startActivityForResult(intent, 100);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getContext(),
                    "Sorry your device not supported",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
