package com.team25.event.planner.product_service.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.team25.event.planner.FragmentTransition;
import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentAddServiceBinding;

import java.util.Calendar;

public class AddServiceFragment extends DialogFragment {

    private FragmentAddServiceBinding binding;

    private Button buttonDate;
    private Button buttonTime;

    public AddServiceFragment(){

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Base_Theme_EventPlanner);

    }

    public void setCancelationDateText(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     binding = FragmentAddServiceBinding.inflate(inflater,container,false);

        SeekBar seekBar = binding.priceSeekBar;
        TextView seekBarValue = binding.seekBarValue;

        seekBar.setProgress(0);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }


        });

        SeekBar seekBar2 = binding.priceSeekBar2;
        TextView seekBarValue2 = binding.seekBarValue2;

        seekBar2.setProgress(0);

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar2, int progress, boolean fromUser) {
                seekBarValue2.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }


        });

        Button saveButton = binding.button2;
        Button cancelButton = binding.button;
        saveButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setTitle("New service")
                        .setMessage("You are successfully added a new service!")


                        .create();

                alertDialog.show();
                dismiss();
            }
        });

        cancelButton.setOnClickListener(v -> {
            // Dismiss the dialog when cancel is pressed
            dismiss(); // This closes the dialog
        });



        buttonDate = binding.buttonDate;
        buttonTime = binding.buttonTime;
        TextView textView = binding.cancelationDateText;
        TextView textView2 = binding.durationTimeText;

        // Datum Picker
        buttonDate.setOnClickListener(v -> {
            // Pronađite trenutni datum
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Kreirajte DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        // Formatirajte odabrani datum
                        String date = "Selected Date: " + (monthOfYear + 1) + "/" + dayOfMonth + "/" + year1;

                        // Prikazivanje datuma u TextView
                        textView.setText(date); // Postavite datum u TextView

                        // (Opcionalno) Prikazivanje poruke u Toast-u
                        Toast.makeText(getContext(), date, Toast.LENGTH_SHORT).show();
                    }, year, month, day);

            // Prikazivanje DatePickerDialog
            datePickerDialog.show();
        });

        // Vreme Picker
        buttonTime.setOnClickListener(v -> {
            // Pronađite trenutni sat i minut
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            // Kreirajte TimePickerDialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                    (view, hourOfDay, minute1) -> {
                        // Formatirajte odabrano vreme
                        String time = "Selected Time: " + hourOfDay + ":" + String.format("%02d", minute1); // Format za minut (dva broja)

                        // Prikazivanje vremena u TextView
                        textView2.setText(time);  // Postavite vreme u TextView

                        // (Opcionalno) Prikazivanje vremena u Toast-u
                        Toast.makeText(getContext(), time, Toast.LENGTH_SHORT).show();
                    }, hour, minute, true);  // true za 24-časovni format

            // Prikazivanje TimePickerDialog
            timePickerDialog.show();
        });
     return binding.getRoot();
    }
}