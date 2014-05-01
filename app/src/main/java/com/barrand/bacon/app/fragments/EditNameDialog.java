package com.barrand.bacon.app.fragments;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.barrand.bacon.app.R;
import com.barrand.bacon.app.model.Model;
// ...

public class EditNameDialog extends DialogFragment {

    private EditText mEditText;
    private Button okBtn;
    public String ssidLabel;

    public EditNameDialog() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_name, container);
        mEditText = (EditText) view.findViewById(R.id.txt_your_name);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Model.PREFS_NAME,0);
        mEditText.setText(sharedPreferences.getString(ssidLabel, ""));


        getDialog().setTitle("Enter SSID for " + ssidLabel);
        okBtn = (Button) view.findViewById(R.id.okbtn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getActivity().getSharedPreferences(Model.PREFS_NAME, 0);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(ssidLabel, mEditText.getText().toString());
                editor.commit();
                dismiss();
            }
        });

        return view;
    }
}