package com.example.voices.view.upload;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.voices.R;
import com.example.voices.model.OtherUser;
import com.pchmn.materialchips.ChipView;
import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.Chip;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import gun0912.tedbottompicker.TedBottomPicker;

public class UploadDetailsFragment extends Fragment {


    ChipsInput hashChipsInput;
    ChipsInput contactsChipsInput;
    ChipsInput locationChipsInput;
    UploadActivity uploadActivity;

    ChipView addHashChip;
    ChipView addTagChip;
    ChipView addLocationChip;

    LinearLayout linearHash;
    LinearLayout linearUsers;
    LinearLayout linearLocation;

    ImageView imageView;

    Button uploadButton;

    private UploadDetailsFragmentInteractionListener mListener;

    public UploadDetailsFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_details, container, false);



        linearHash = view.findViewById(R.id.linear_tags_hash);
        linearUsers = view.findViewById(R.id.linear_tags_users);
        linearLocation = view.findViewById(R.id.linear_tags_location);
        imageView = view.findViewById(R.id.djImge);

        addHashChip = view.findViewById(R.id.addHashChip);
        addTagChip = view.findViewById(R.id.addTagChip);
        addLocationChip = view.findViewById(R.id.addLocationChip);

        uploadButton = view.findViewById(R.id.upload_button);

        if (uploadActivity.imageUri != null){
            imageView.setImageURI(uploadActivity.imageUri);
        }

        addHashChip.setOnChipClicked(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.uploadDetailsFragmentOnClickLinear(0);
            }
        });
        addTagChip.setOnChipClicked(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.uploadDetailsFragmentOnClickLinear(1);
            }
        });
        addLocationChip.setOnChipClicked(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.uploadDetailsFragmentOnClickLinear(2);
            }
        });


        for (Chip chip : uploadActivity.hashesSelected) {
            ChipView chipView = new ChipView(getContext());
            chipView.setDeletable(true);
            chipView.setHasAvatarIcon(false);
            chipView.setChip(chip);
            chipView.inflate(chip);




            LinearLayout divider = new LinearLayout(getContext());
            LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(50, 1);
            divider.setLayoutParams(LLParams);
            chipView.setOnDeleteClicked(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uploadActivity.hashesSelected.remove(chip);
                    chipView.setVisibility(View.GONE);
                    divider.setVisibility(View.GONE);
                }
            });

            linearHash.addView(divider);
            linearHash.addView(chipView);
        }


        for (OtherUser chip : uploadActivity.usersSelected) {
            ChipView chipView = new ChipView(getContext());
            chipView.setDeletable(true);
            chipView.setHasAvatarIcon(false);
            chipView.setChip(chip);
            chipView.inflate(chip);




            LinearLayout divider = new LinearLayout(getContext());
            LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(50, 1);
            divider.setLayoutParams(LLParams);
            chipView.setOnDeleteClicked(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uploadActivity.usersSelected.remove(chip);
                    chipView.setVisibility(View.GONE);
                    divider.setVisibility(View.GONE);
                }
            });

            linearUsers.addView(divider);
            linearUsers.addView(chipView);
        }

        for (Chip chip : uploadActivity.locationSelected) {
            ChipView chipView = new ChipView(getContext());
            chipView.setDeletable(true);
            chipView.setHasAvatarIcon(false);
            chipView.setChip(chip);
            chipView.inflate(chip);



            LinearLayout divider = new LinearLayout(getContext());
            LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(50, 1);
            divider.setLayoutParams(LLParams);


            chipView.setOnDeleteClicked(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uploadActivity.locationSelected.remove(chip);
                    chipView.setVisibility(View.GONE);
                    divider.setVisibility(View.GONE);
                }
            });

            linearLocation.addView(divider);
            linearLocation.addView(chipView);
        }

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.uploadDetailsFragmentUpload();
            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*mListener.getImage();*/
                TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(uploadActivity)
                        .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                            @Override
                            public void onImageSelected(Uri uri) {
                                uploadActivity.imageUri = uri;
                                imageView.setImageURI(uri);
                            }
                        })
                        .create();

                tedBottomPicker.show(getChildFragmentManager());

            }
        });

        return view;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.uploadDetailsFragmentUpload();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UploadDetailsFragmentInteractionListener) {
            mListener = (UploadDetailsFragmentInteractionListener) context;
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


    public interface UploadDetailsFragmentInteractionListener {
        // TODO: Update argument type and name
        void uploadDetailsFragmentUpload();
        void uploadDetailsFragmentOnClickLinear(int i);
        void getImage();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (uploadActivity.imageUri != null){
            imageView.setImageURI(uploadActivity.imageUri);
        }
    }
}
