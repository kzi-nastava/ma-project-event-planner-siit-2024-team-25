package com.team25.event.planner.user.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.team25.event.planner.R;
import com.team25.event.planner.core.viewmodel.AuthViewModel;

import java.time.Duration;
import java.time.Instant;

public class SuspensionPage extends Fragment {
    private TextView timeRemaining;

    private Instant suspensionEndTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suspension_page, container, false);

        if (getArguments() != null) {
            suspensionEndTime = (Instant) getArguments().getSerializable("suspensionEndDateTime");
        }

        timeRemaining = view.findViewById(R.id.timeRemaining);

        if(suspensionEndTime != null){
            new CountDownTimer(Duration.between(Instant.now(), suspensionEndTime).toMillis(), 1000) {
                @SuppressLint("DefaultLocale")
                @Override
                public void onTick(long millisUntilFinished) {
                    Duration durationLeft = Duration.ofMillis(millisUntilFinished);

                    long days = durationLeft.toDays();
                    long hours = durationLeft.minusDays(days).toHours();
                    long minutes = durationLeft.minusDays(days).minusHours(hours).toMinutes();
                    long seconds = durationLeft.minusDays(days).minusHours(hours).minusMinutes(minutes).getSeconds();

                    timeRemaining.setText(String.format("Remaining time: %d days %02d h %02d m %02d s", days, hours, minutes, seconds));
                }

                @Override
                public void onFinish() {
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                     navController.navigate(R.id.loginFragment);
                }
            }.start();
        }
        return view;
    }

}