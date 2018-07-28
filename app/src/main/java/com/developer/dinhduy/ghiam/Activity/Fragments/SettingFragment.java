package com.developer.dinhduy.ghiam.Activity.Fragments;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;

import com.developer.dinhduy.ghiam.Activity.Databases.MySharedPreferences;
import com.developer.dinhduy.ghiam.R;

public class SettingFragment extends PreferenceFragment{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        final SwitchPreference mSwitch=(SwitchPreference) findPreference("example_switch");
        mSwitch.setChecked(MySharedPreferences.getPrefHighQuality(getActivity()));
        //setup sự kiện xem người dùng có thay đổi không
        mSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
              MySharedPreferences.setPrefHighQuality(getActivity(),(boolean)o);
                return true;
            }
        });
        //EditText chắc không làm gì  vứt đó đã
        Preference aboutPref = findPreference("example_text");

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
