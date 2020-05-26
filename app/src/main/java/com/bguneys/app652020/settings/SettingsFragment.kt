package com.bguneys.app652020.settings

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.bguneys.app652020.R


class SettingsFragment : PreferenceFragmentCompat() {
    /**
     * Called during [.onCreate] to supply the preferences for this fragment.
     * Subclasses are expected to call [.setPreferenceScreen] either
     * directly or via helper methods such as [.addPreferencesFromResource]
     */
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {

        if (preference?.key.equals(getString(R.string.preference_disclaimer))) {
            findNavController().navigate(R.id.action_action_settings_to_disclaimerFragment)
        }

        if (preference?.key.equals(getString(R.string.preference_about))) {
            //Navigate to DisclaimerFragment
            findNavController().navigate(R.id.action_action_settings_to_aboutFragment)
        }

        return super.onPreferenceTreeClick(preference)
    }


}
