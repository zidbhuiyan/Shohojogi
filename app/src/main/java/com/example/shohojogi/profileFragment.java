package com.example.shohojogi;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public profileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static profileFragment newInstance(String param1, String param2) {
        profileFragment fragment = new profileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    EditText name,email,dob,gender,skill;

    String ucat1,ucat2;

    Button button;

    LinearLayout lh_s;

    FirebaseAuth mAuth;
    FirebaseFirestore fstore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        ucat1 = "Client";
        ucat2= "Worker";

        name = (EditText) v.findViewById(R.id.pct1);
        email = (EditText) v.findViewById(R.id.pct2);
        dob = (EditText) v.findViewById(R.id.pct3);
        gender = (EditText) v.findViewById(R.id.pct4);
        skill = (EditText) v.findViewById(R.id.pct5);
        button = (Button) v.findViewById(R.id.button);
        lh_s =(LinearLayout) v.findViewById(R.id.skill_p);
        mAuth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();

        DocumentReference docRef = fstore.collection("users").document(mAuth.getCurrentUser().getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){

                    if(documentSnapshot.getString("Category").equals(ucat1)){
                        button.setVisibility(View.GONE);
                        button.setEnabled(false);
                        lh_s.setVisibility(View.GONE);
                        lh_s.setEnabled(false);
                    }

                    else if(documentSnapshot.getString("Category").equals(ucat2)){
                        button.setVisibility(View.VISIBLE);
                        button.setEnabled(true);
                        lh_s.setVisibility(View.VISIBLE);
                        lh_s.setEnabled(true);
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference workerSta = FirebaseDatabase.getInstance().getReference().child("Users").child("Worker").child(userId).child("Working");
                        workerSta.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    String sta = snapshot.getValue().toString();

                                    if(sta.contains("False")){
                                        workerSta.removeValue();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }

                    name.setText(documentSnapshot.getString("Name"));
                    email.setText(documentSnapshot.getString("email"));
                    dob.setText(documentSnapshot.getString("Date of Birth"));
                    gender.setText(documentSnapshot.getString("Gender"));
                    skill.setText(documentSnapshot.getString("Skills"));

                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),WorkerMapsActivity.class);
                startActivity(intent);
            }
        });

        return  v;
    }
}