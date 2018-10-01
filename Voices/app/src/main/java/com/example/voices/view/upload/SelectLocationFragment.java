package com.example.voices.view.upload;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.voices.R;
import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.Chip;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectLocationFragment extends Fragment {


    private SelectLocationsFragmentInteractionListener mListener;
    UploadActivity uploadActivity;
    ChipsInput locationSelect;
    public SelectLocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_select_location, container, false);
        Button buttonSelect = view.findViewById(R.id.select_location_button);
        locationSelect = view.findViewById(R.id.tags_location_input_select);
        Chip chip = new Chip("Juan Stein", "steinjuan");
        Chip chip2 = new Chip("Juan Stein", "steinjuan");
        Chip chip3 = new Chip("Juan Stein", "steinjuan");
        List<Chip> chips = new ArrayList<>();
        chips.add(chip);
        chips.add(chip2);
        chips.add(chip3);
        locationSelect.setFilterableList(chips);
        buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectUsers();
            }
        });



        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.selectLocationFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SelectLocationsFragmentInteractionListener) {
            mListener = (SelectLocationsFragmentInteractionListener) context;
            uploadActivity = (UploadActivity) context;

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SelectLocationsFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface SelectLocationsFragmentInteractionListener {
        // TODO: Update argument type and name
        void selectLocationFragmentInteraction(Uri uri);
        void onLocationSelected();
    }

    public void onSelectUsers(){
        uploadActivity.locationSelected = (List<Chip>) locationSelect.getSelectedChipList();
        mListener.onLocationSelected();
    }

}
