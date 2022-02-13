package com.example.time.ui.help;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.example.time.R;
import com.example.time.Service_Block;
import com.example.time.View_Guid;

public class HelpFragment extends Fragment {


    TextView mail,replay;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_help, container, false);

        mail=root.findViewById(R.id.contact_write);
        replay=root.findViewById(R.id.replay);
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();

            }
        });
        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                startActivity(new Intent(getContext(), View_Guid.class));
            }
        });

        return root;
    }
    private void sendMail(){
        String mail_To="farhat.wissam@outlook.com";
        Intent intent=new Intent(Intent.ACTION_SENDTO);
        intent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] {mail_To});
        intent.putExtra(Intent.EXTRA_SUBJECT,"Contact");
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent,"Choose an email client "));
    }
}