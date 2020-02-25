package com.onionv2.cheatbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class CreateCheatStepOne extends AppCompatActivity {

    String subject;
    EditText editText;
    Button button;
    ConstraintLayout constraintLayout;
    BroadcastReceiver broadcast_reciever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_cheat_step_one);
        Bundle bundle = getIntent().getExtras();
        subject = bundle.getString("subj");

        editText = findViewById(R.id.edit_text_title);
        button = findViewById(R.id.material_button);
        constraintLayout = findViewById(R.id.constr);
        final TextView status = findViewById(R.id.textView2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().toString().trim().length() == 0){
                    Snackbar snackbar = Snackbar
                            .make(constraintLayout,  "Title must not be empty!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else{
                    Intent intent = new Intent(CreateCheatStepOne.this, CreateCheatStepTwo.class);
                    intent.putExtra("subj", subject);
                    intent.putExtra("title", editText.getText().toString());
                    startActivityForResult(intent, 1234);
                }


            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    status.setText(charSequence.length() + "/35");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

         broadcast_reciever = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("finish")) {
                    finish();
                    unregisterReceiver(broadcast_reciever);
                }
            }
        };
        registerReceiver(broadcast_reciever, new IntentFilter("finish"));


    }
}
