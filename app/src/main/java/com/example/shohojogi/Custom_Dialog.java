package com.example.shohojogi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class Custom_Dialog extends AppCompatDialogFragment {
    int total_price;
    @NonNull
    @Override

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_dialog,null);

        TextView catd,catinfo,costinfo;
        Button cancelbtn,confirmbtn;
        EditText quantity;


        catd = view.findViewById(R.id.catd);
        cancelbtn = view.findViewById(R.id.cancelbtn);
        confirmbtn = view.findViewById(R.id.confirmlbtn);
        quantity = view.findViewById(R.id.numSpin);
        catinfo = view.findViewById(R.id.q);
        costinfo = view.findViewById(R.id.q2);


        Bundle bundle = this.getArguments();
        String catdata = bundle.getString("key");
        String catInformation = bundle.getString("info");
        String price = bundle.getString("price");
        catd.setText(catdata);
        catinfo.setText(catInformation);

        builder.setView(view).setTitle(" ");

        quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               if(!quantity.getText().toString().isEmpty()){
                   int unitprice = Integer.parseInt(price);
                   int quant = Integer.parseInt(quantity.getText().toString());
                   total_price = unitprice * quant;

                   costinfo.setText("Cost will be "+total_price+" BDT");
                   confirmbtn.setEnabled(true);
               }
               else {
                   confirmbtn.setEnabled(false);
                   costinfo.setText("");
               }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ClientMapsActivity.class);
                intent.putExtra("catWorker",catdata);
                intent.putExtra("total_price",Integer.toString(total_price));
                startActivity(intent);
                dismiss();

            }
        });

        return builder.create();
    }
}
