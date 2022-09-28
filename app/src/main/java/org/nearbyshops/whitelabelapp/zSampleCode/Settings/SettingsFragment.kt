package org.nearbyshops.whitelabelapp.zSampleCode.Settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import org.nearbyshops.whitelabelapp.R



class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }




}