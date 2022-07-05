
package com.example.shohojogi;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link homeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class homeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public homeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static homeFragment newInstance(String param1, String param2) {
        homeFragment fragment = new homeFragment();
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

    CardView bathroom,floor,furniture,car,ac,fridge,kitchen,clothes,iron,curtains,carpet,therapy,
             walking,feeding,shower;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        bathroom = (CardView)v.findViewById(R.id.bathroom);
        bathroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key","Bathroom Cleaning");
                bundle.putString("info","Number of Bathroom");
                bundle.putString("price","300");

                Custom_Dialog custom_dialog = new Custom_Dialog();
                custom_dialog.setArguments(bundle);
                custom_dialog.show(getActivity().getSupportFragmentManager(), "Choose Criteria");
            }
        });

        floor = (CardView)v.findViewById(R.id.floor);
        floor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key","Floor Cleaning");
                bundle.putString("info","Number of Floor");
                bundle.putString("price","250");

                Custom_Dialog custom_dialog = new Custom_Dialog();
                custom_dialog.setArguments(bundle);
                custom_dialog.show(getActivity().getSupportFragmentManager(), "Choose Criteria");
            }
        });

        furniture = (CardView)v.findViewById(R.id.furniture);
        furniture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key","Furniture Cleaning");
                bundle.putString("info","Number of Furniture");
                bundle.putString("price","50");

                Custom_Dialog custom_dialog = new Custom_Dialog();
                custom_dialog.setArguments(bundle);
                custom_dialog.show(getActivity().getSupportFragmentManager(), "Choose Criteria");
            }
        });

        car = (CardView)v.findViewById(R.id.car);
        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key","Car Cleaning");
                bundle.putString("info","Number of Car");
                bundle.putString("price","600");

                Custom_Dialog custom_dialog = new Custom_Dialog();
                custom_dialog.setArguments(bundle);
                custom_dialog.show(getActivity().getSupportFragmentManager(), "Choose Criteria");
            }
        });

        ac = (CardView)v.findViewById(R.id.ac);
        ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key","AC Cleaning");
                bundle.putString("info","Number of AC");
                bundle.putString("price","500");

                Custom_Dialog custom_dialog = new Custom_Dialog();
                custom_dialog.setArguments(bundle);
                custom_dialog.show(getActivity().getSupportFragmentManager(), "Choose Criteria");
            }
        });

        fridge = (CardView)v.findViewById(R.id.fridge);
        fridge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key","Fridge Cleaning");
                bundle.putString("info","Number of Fridge");
                bundle.putString("price","400");

                Custom_Dialog custom_dialog = new Custom_Dialog();
                custom_dialog.setArguments(bundle);
                custom_dialog.show(getActivity().getSupportFragmentManager(), "Choose Criteria");
            }
        });

        kitchen = (CardView)v.findViewById(R.id.kitchen);
        kitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key","Kitchen Cleaning");
                bundle.putString("info","Number of Kitchen");
                bundle.putString("price","300");

                Custom_Dialog custom_dialog = new Custom_Dialog();
                custom_dialog.setArguments(bundle);
                custom_dialog.show(getActivity().getSupportFragmentManager(), "Choose Criteria");
            }
        });

        clothes = (CardView)v.findViewById(R.id.clothes);
        clothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key","Clothes Laundry");
                bundle.putString("info","Number of Clothes");
                bundle.putString("price","50");

                Custom_Dialog custom_dialog = new Custom_Dialog();
                custom_dialog.setArguments(bundle);
                custom_dialog.show(getActivity().getSupportFragmentManager(), "Choose Criteria");
            }
        });

        iron = (CardView)v.findViewById(R.id.iron);
        iron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key","Iron Laundry");
                bundle.putString("info","Number of Clothes");
                bundle.putString("price","40");

                Custom_Dialog custom_dialog = new Custom_Dialog();
                custom_dialog.setArguments(bundle);
                custom_dialog.show(getActivity().getSupportFragmentManager(), "Choose Criteria");
            }
        });

        curtains= (CardView)v.findViewById(R.id.curtains);
        curtains.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key","Curtains Laundry");
                bundle.putString("info","Number of Curtains");
                bundle.putString("price","100");

                Custom_Dialog custom_dialog = new Custom_Dialog();
                custom_dialog.setArguments(bundle);
                custom_dialog.show(getActivity().getSupportFragmentManager(), "Choose Criteria");
            }
        });

        carpet = (CardView)v.findViewById(R.id.carpet);
        carpet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key","Carpet Laundry");
                bundle.putString("info","Number of Carpet");
                bundle.putString("price","100");

                Custom_Dialog custom_dialog = new Custom_Dialog();
                custom_dialog.setArguments(bundle);
                custom_dialog.show(getActivity().getSupportFragmentManager(), " ");
            }
        });

        therapy = (CardView)v.findViewById(R.id.therapy);
        therapy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key","Therapy Senior Citizens");

                Custom_Dialog custom_dialog = new Custom_Dialog();
                custom_dialog.setArguments(bundle);
                custom_dialog.show(getActivity().getSupportFragmentManager(), " ");
            }
        });

        walking = (CardView)v.findViewById(R.id.walking);
        walking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key","Walking Senior Citizens");

                Custom_Dialog custom_dialog = new Custom_Dialog();
                custom_dialog.setArguments(bundle);
                custom_dialog.show(getActivity().getSupportFragmentManager(), " ");
            }
        });

        feeding = (CardView)v.findViewById(R.id.feeding);
        feeding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key","Feeding Senior Citizens");

                Custom_Dialog custom_dialog = new Custom_Dialog();
                custom_dialog.setArguments(bundle);
                custom_dialog.show(getActivity().getSupportFragmentManager(), " ");
            }
        });

        shower = (CardView)v.findViewById(R.id.shower);
        shower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key","Shower Senior Citizens");

                Custom_Dialog custom_dialog = new Custom_Dialog();
                custom_dialog.setArguments(bundle);
                custom_dialog.show(getActivity().getSupportFragmentManager(), " ");
            }
        });

        return  v;
    }

}