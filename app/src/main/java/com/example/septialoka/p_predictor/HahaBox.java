package com.example.septialoka.p_predictor;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rio.chandra.r on 12/16/17.
 */

public class HahaBox extends Dialog implements View.OnClickListener {

    Context context;
    OnARClickListener oacklik;
    Button btnGasoline, btnDistilled;
    TextView textTitle;
    TextView textData;

    public void setOacklik(OnARClickListener oacklik){
        this.oacklik = oacklik;
    }

    public HahaBox(@NonNull Context context) {
        super(context);
        this.context = context;
        initAll();
    }

    public HahaBox(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.context = context;
        initAll();
    }

    protected HahaBox(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
        initAll();
    }

    public void setTextData(String date){
        textData.setText(date);
    }

    public void setTextTitle(String date){
        textTitle.setText(date);
    }

    private void initAll(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_runner_confirmation_layout);
        btnGasoline = findViewById(R.id.button_gasoline);
        btnDistilled = findViewById(R.id.button_distilled);
        textData = findViewById(R.id.text_date);
        textTitle = findViewById(R.id.text_title);
        btnGasoline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(oacklik != null)
                    oacklik.onClickG();
            }
        });
        btnDistilled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(oacklik != null)
                    oacklik.onClickD();
            }
        });

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, @Nullable Menu menu, int deviceId) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public interface OnARClickListener{
        void onClickG();
        void onClickD();
    }
}
