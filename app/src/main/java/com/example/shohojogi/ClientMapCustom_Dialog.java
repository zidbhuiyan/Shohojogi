package com.example.shohojogi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.firebase.geofire.GeoFire;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ClientMapCustom_Dialog extends AppCompatDialogFragment {
    @NonNull
    @Override

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_clientmapdialog,null);

        TextView catd;
        Button cancelbtn,confirmbtn;

        catd = view.findViewById(R.id.catd);
        cancelbtn = view.findViewById(R.id.cancelbtn);
        confirmbtn = view.findViewById(R.id.confirmlbtn);

        Bundle bundle = this.getArguments();
        String catdata = bundle.getString("key");
        catd.setText(catdata);


        builder.setView(view).setTitle("Payment");

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ClientRequest");
                GeoFire geoFire = new GeoFire(ref);
                geoFire.removeLocation(userId);
                Intent intent = new Intent(getContext(),dashboard.class);
                startActivity(intent);
                dismiss();

            }
        });

        confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ClientRequest");
                GeoFire geoFire = new GeoFire(ref);
                geoFire.removeLocation(userId);
                Intent intent = new Intent(getContext(),dashboard.class);
                startActivity(intent);
                dismiss();

            }
        });

        return builder.create();
    }
}

