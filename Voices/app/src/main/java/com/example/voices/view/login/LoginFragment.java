package com.example.voices.view.login;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.voices.R;

public class LoginFragment extends Fragment {

    private LoginOnFragmentInteractionListener mListener;
    TextInputLayout userTextInputLayout;
    TextInputLayout passwordTextInputLayout;
    Button loginButton;

    public LoginFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        userTextInputLayout = (TextInputLayout) view.findViewById(R.id.userTextInputLayoutLoginFr);
        passwordTextInputLayout = (TextInputLayout) view.findViewById(R.id.passwordTextInputLayoutLoginFr);
        passwordTextInputLayout.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        loginButton = view.findViewById(R.id.button_login_fragment);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.loginOnFragmentInteraction(userTextInputLayout.getEditText().getText().toString(), passwordTextInputLayout.getEditText().getText().toString());
            }
        });




        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginOnFragmentInteractionListener) {
            mListener = (LoginOnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RegisterOnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }




    public interface LoginOnFragmentInteractionListener {
        // TODO: Update argument type and name
        void loginOnFragmentInteraction(String username, String password);
    }
}
