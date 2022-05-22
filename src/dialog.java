package com.example.sloje;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class dialog extends DialogFragment {
    TextView text;
    TextView title;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().getDecorView().setBackgroundResource(Color.TRANSPARENT);

        title = v.findViewById(R.id.title);
        text = v.findViewById(R.id.text);

        Bundle data = getArguments();

        title.setText(data.getString("title"));
        text.setText(data.getString("text"));
        return v;
    }
}
