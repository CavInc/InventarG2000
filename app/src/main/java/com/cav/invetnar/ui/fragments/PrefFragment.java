package com.cav.invetnar.ui.fragments;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import com.cav.invetnar.R;
import com.cav.invetnar.data.managers.PreManager;

/**
 * Created by cav on 07.08.19.
 */

public class PrefFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    private PreManager mPrefManager;

    private Preference mDelim;
    private ListPreference mCodeFile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);

        mPrefManager = new PreManager();

        mDelim = findPreference("load_delim");
        mDelim.setSummary(mPrefManager.getDelimLoadFile());
        mDelim.setOnPreferenceChangeListener(this);

        mCodeFile = (ListPreference) findPreference("file_code");
        mCodeFile.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        if (preference.getKey().equals("load_delim")) {
            mDelim.setSummary((String) value);
        }
        if (preference.getKey().equals("file_code")){
            String l = (String) value;
            mPrefManager.setCodeFile(Integer.valueOf(l));
            String[] hL = getResources().getStringArray(R.array.code_entries);
            mCodeFile.setSummary(hL[Integer.valueOf(l)-1]);
        }
        return false;
    }
}
