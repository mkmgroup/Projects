package com.example.voices.view.upload;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.voices.R;
import com.pchmn.materialchips.ChipView;
import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.Chip;
import com.pchmn.materialchips.model.ChipInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SelectHashFragment extends Fragment {

    ChipsInput selectHash;
    UploadActivity uploadActivity;
    ChipView createNewChip;
    String text;
    private SelectHashFragmentInteractionListener mListener;

    public SelectHashFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_hash, container, false);
        Button buttonSelect = view.findViewById(R.id.select_hash_button);
        selectHash = view.findViewById(R.id.tags_hash_input_select);
        createNewChip = view.findViewById(R.id.addNewHashChip);

        createNewChip.setOnChipClicked(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectHash.addChip(text, "");
            }
        });

        selectHash.addChipsListener(new ChipsInput.ChipsListener() {
            @Override
            public void onChipAdded(ChipInterface chipInterface, int i) {

            }

            @Override
            public void onChipRemoved(ChipInterface chipInterface, int i) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence) {
                text = charSequence.toString().trim();

            }
        });
        List<Chip> chips = new ArrayList<>();
        long i = 0;
        for (Map.Entry<String, Boolean> hash : uploadActivity.user.getFollowingHashes().entrySet()) {
                Chip chip = new Chip(hash.getKey(),"");
                chips.add(chip);
            i++;
        }

        selectHash.setFilterableList(chips);
        buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectHash();
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.selectHashFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SelectHashFragmentInteractionListener) {
            mListener = (SelectHashFragmentInteractionListener) context;
            uploadActivity = (UploadActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SelectHashFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface SelectHashFragmentInteractionListener {
        // TODO: Update argument type and name
        void selectHashFragmentInteraction(Uri uri);
        void onHashSelected();
    }
    public void onSelectHash(){
        uploadActivity.hashesSelected = (List<Chip>) selectHash.getSelectedChipList();
        mListener.onHashSelected();
    }
}
