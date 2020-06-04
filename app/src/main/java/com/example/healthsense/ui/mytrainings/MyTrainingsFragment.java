package com.example.healthsense.ui.mytrainings;

import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.healthsense.R;

public class MyTrainingsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_trainings, container, false);



        //----programación de formato de la lista de trainings

        Space s0 = new Space(root.getContext());
        s0.setMinimumHeight(30);

        LinearLayout ll = new LinearLayout(root.getContext());
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setGravity(Gravity.CENTER_VERTICAL);

        TextView tv1 = new TextView(root.getContext());
        tv1.setText(Html.fromHtml("<b>Christian Bale Training</b><br><br> " +
                "Squats, Calisthenics, Weights and Cardio. Cardio: Complete 30-45 minutes of cardio from the selection below: High Incline Walk."));
        //tv1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_star, 0, 0, 30);
        tv1.setTextColor(root.getResources().getColor(R.color.GrayText));
        tv1.setPadding(30,30,30,30);

        ll.addView(tv1);

        LinearLayout ll1 = new LinearLayout(root.getContext());
        ll1.setOrientation(LinearLayout.HORIZONTAL);
        ll1.setGravity(Gravity.CENTER_VERTICAL);

        TextView tv3 = new TextView(root.getContext());
        tv3.setText(Html.fromHtml("<b>CREATED:</b> 10/1/2025&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>DIFFICULTY:</b> "));
        tv3.setTextColor(root.getResources().getColor(R.color.GrayText));
        tv3.setPadding(30,30,30,30);

        TextView tv4 = new TextView(root.getContext());
        tv4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_star, 0, 0, 0);
        tv4.setPadding(0,20,0,20);

        ll1.addView(tv3);
        ll1.addView(tv4);

        LinearLayout ll3 = new LinearLayout(root.getContext());
        ll3.setOrientation(LinearLayout.VERTICAL);
        ll3.setGravity(Gravity.CENTER_VERTICAL);

        ll3.setBackgroundResource(R.drawable.background_model_training);

        ll3.addView(ll);
        ll3.addView(ll1);

        LinearLayout lista = root.findViewById(R.id.list_trainings);

        lista.addView(s0);
        lista.addView(ll3);

        //------fin de programación del formato

        return root;
    }
}