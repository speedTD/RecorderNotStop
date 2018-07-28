package com.developer.dinhduy.ghiam.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.developer.dinhduy.ghiam.Activity.Fragments.SettingFragment;

public  class SettingActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content,new SettingFragment()).commit();
    }
}
