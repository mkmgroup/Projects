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
import com.example.voices.model.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterFragment extends Fragment {


    private RegisterOnFragmentInteractionListener mListener;
    TextInputLayout emailTextInputLayout;
    TextInputLayout passwordTextInputLayout;
    TextInputLayout repeatPasswordTextInputLayout;
    TextInputLayout nicknameTextInputLayout;
    TextInputLayout nameTextInputLayout;
    TextInputLayout lastNameTextInputLayout;
    Button regButton;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_register, container, false);
        emailTextInputLayout = (TextInputLayout) view.findViewById(R.id.emailTextInputLayoutRegiserFr);
        passwordTextInputLayout = (TextInputLayout) view.findViewById(R.id.passwordTextInputLayoutRegister);
        passwordTextInputLayout.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        repeatPasswordTextInputLayout = view.findViewById(R.id.repeatPasswordTextInputLayoutRegister);
        repeatPasswordTextInputLayout.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        regButton = view.findViewById(R.id.button_register_fragment);
        nicknameTextInputLayout = view.findViewById(R.id.nickTextInputLayoutRegiserFr);
        nameTextInputLayout = view.findViewById(R.id.nameTextInputLayoutRegiserFr);
        lastNameTextInputLayout = view.findViewById(R.id.lastNameTextInputLayoutRegiserFr);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean condiion = false;
                String pass = passwordTextInputLayout.getEditText().getText().toString();
                String rePass = repeatPasswordTextInputLayout.getEditText().getText().toString();
                String mail = emailTextInputLayout.getEditText().getText().toString();
                String name = nameTextInputLayout.getEditText().getText().toString();
                String lastName = lastNameTextInputLayout.getEditText().getText().toString();
                String nickname = nicknameTextInputLayout.getEditText().getText().toString();

                if (name.equals("")){
                    nameTextInputLayout.setError("Porfavor completa el campo");
                    condiion = false;
                }else {condiion = true;}
                if (lastName.equals("")){
                    lastNameTextInputLayout.setError("Porfavor completa el campo");
                    condiion = false;
                }else {condiion = true;}
                if (nickname.equals("")){
                    condiion = false;
                    nicknameTextInputLayout.setError("Porfavor completa el campo");
                }else {condiion = true;}
                if (pass.equals("")){
                    condiion = false;
                    passwordTextInputLayout.setError("Porfavor completa el campo");
                }else {condiion = true;}
                if (rePass.equals("")){
                    condiion = false;
                    repeatPasswordTextInputLayout.setError("Porfavor completa el campo");
                }else {condiion = true;}
                if (!isEmailValid(mail)){
                    condiion = false;
                    emailTextInputLayout.setError("No es un email valido");
                }else {condiion = true;}
                if (mail.equals("")){
                    condiion = false;
                    emailTextInputLayout.setError("Porfavor completa el campo");
                }else {condiion = true;}
                if (!pass.equals(rePass)){
                    condiion = false;
                    repeatPasswordTextInputLayout.setError("Tu contrasena no coincide");
                }else {condiion = true;}
                if (pass.length() < 6){
                    condiion = false;
                    passwordTextInputLayout.setError("Tu contrasen debe contener al menos 6 caracteres");
                }else {condiion = true;}
                if(!isValidWord(name)){
                    condiion = false;
                    nameTextInputLayout.setError("No es un nombre valido");
                }else {condiion = true;}
                if(!isValidWord(lastName)){
                    condiion = false;
                    lastNameTextInputLayout.setError("No es un apellido valido");
                }else {condiion = true;}

                /*if((!name.equals("")) && (!lastName.equals("")) && (!nickname.equals("")) && (!pass.equals("")) && (!rePass.equals("")) && (!mail.equals("")) && (isValidWord(mail))
                        && (pass.equals(rePass)) && (pass.length() >= 6) && (isValidWord(name)) && (isValidWord(lastName))){
                    User user = new User();
                    user.setName(name + " " + lastName);
                    user.setNickname(nickname);
                    user.setFirsTime(true);
                    mListener.registerOnFragmentInteraction(mail, pass, user);
                }*/

                if (condiion) {
                    User user = new User();
                    user.setName(name + " " + lastName);
                    user.setNickname(nickname);
                    user.setFirsTime(true);
                    user.setAvatarUrl("https://firebasestorage.googleapis.com/v0/b/voices-ad267.appspot.com/o/dj.png?alt=media&token=dcefe0dd-329d-40dc-b86a-5de04135ddae");
                    mListener.registerOnFragmentInteraction(mail, pass, user);
                }

            }
        });



        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RegisterOnFragmentInteractionListener) {
            mListener = (RegisterOnFragmentInteractionListener) context;
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


    public interface RegisterOnFragmentInteractionListener {
        // TODO: Update argument type and name
        void registerOnFragmentInteraction(String mail, String password, User user);
    }
    public boolean isEmailValid(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }
    public boolean isValidWord(String word) {

        return word.matches("[A-Za-z][^.]*");
    }
}
