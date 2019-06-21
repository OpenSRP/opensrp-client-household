package org.smartregister.family.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.vijay.jsonwizard.activities.JsonWizardFormActivity;
import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.family.FamilyLibrary;
import org.smartregister.family.R;
import org.smartregister.family.fragment.FamilyWizardFormFragment;
import org.smartregister.family.util.Constants;
import org.smartregister.family.util.JsonFormUtils;
import org.smartregister.util.LangUtils;

import java.util.List;

public class FamilyWizardFormActivity extends JsonWizardFormActivity {
    private String TAG = FamilyWizardFormActivity.class.getCanonicalName();

    private boolean enableOnCloseDialog = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        enableOnCloseDialog = getIntent().getBooleanExtra(Constants.WizardFormActivity.EnableOnCloseDialog, true);
    }

    @Override
    public void initializeFormFragment() {
        initializeFormFragmentCore();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setConfirmCloseTitle(getString(R.string.confirm_form_close));
        setConfirmCloseMessage(getString(R.string.confirm_form_close_explanation));

        try {
            JSONObject form = new JSONObject(currentJsonState());
            String et = form.getString(JsonFormUtils.ENCOUNTER_TYPE);
            if (et.trim().toLowerCase().contains("update")) {
                setConfirmCloseMessage(getString(R.string.any_changes_you_make));
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        if (toolbar != null) {
            toolbar.setContentInsetStartWithNavigation(0);
        }
        super.setSupportActionBar(toolbar);
    }

    protected void initializeFormFragmentCore() {
        FamilyWizardFormFragment familyWizardFormFragment = FamilyWizardFormFragment.getFormFragment(JsonFormConstants.FIRST_STEP_NAME);
        getSupportFragmentManager().beginTransaction()
                .add(com.vijay.jsonwizard.R.id.container, familyWizardFormFragment).commit();
    }

    @Override
    public void writeValue(String stepName, String parentKey, String childObjectKey, String childKey, String value, String openMrsEntityParent, String openMrsEntity, String openMrsEntityId, boolean popup) throws JSONException {
        super.writeValue(stepName, parentKey, childObjectKey, childKey, value, openMrsEntityParent, openMrsEntity, openMrsEntityId, popup);
        if (FamilyLibrary.getInstance().metadata().formWizardValidateRequiredFieldsBefore) {
            validateActivateNext();
        }
    }

    @Override
    public void writeValue(String stepName, String key, String value, String openMrsEntityParent, String openMrsEntity, String openMrsEntityId, boolean popup) throws JSONException {
        super.writeValue(stepName, key, value, openMrsEntityParent, openMrsEntity, openMrsEntityId, popup);
        if (FamilyLibrary.getInstance().metadata().formWizardValidateRequiredFieldsBefore) {
            validateActivateNext();
        }
    }

    @Override
    public void writeValue(String stepName, String key, String value, String openMrsEntityParent, String openMrsEntity, String openMrsEntityId) throws JSONException {
        super.writeValue(stepName, key, value, openMrsEntityParent, openMrsEntity, openMrsEntityId);
        if (FamilyLibrary.getInstance().metadata().formWizardValidateRequiredFieldsBefore) {
            validateActivateNext();
        }
    }

    @Override
    public void writeValue(String stepName, String parentKey, String childObjectKey, String childKey, String value, String openMrsEntityParent, String openMrsEntity, String openMrsEntityId) throws JSONException {
        super.writeValue(stepName, parentKey, childObjectKey, childKey, value, openMrsEntityParent, openMrsEntity, openMrsEntityId);
        if (FamilyLibrary.getInstance().metadata().formWizardValidateRequiredFieldsBefore) {
            validateActivateNext();
        }
    }

    public void validateActivateNext() {
        Fragment fragment = getVisibleFragment();
        if (fragment != null && fragment instanceof FamilyWizardFormFragment) {
            ((FamilyWizardFormFragment) fragment).validateActivateNext();
        }
    }

    public Fragment getVisibleFragment() {
        List<Fragment> fragments = this.getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    /**
     * Conditionaly display the confirmation dialog
     */
    @Override
    public void onBackPressed() {
        if (enableOnCloseDialog) {
            super.onBackPressed();
        } else {
            FamilyWizardFormActivity.this.finish();
        }
    }

    @Override
    protected void attachBaseContext(android.content.Context base) {
        // get language from prefs
        String lang = LangUtils.getLanguage(base.getApplicationContext());
        super.attachBaseContext(LangUtils.setAppLocale(base, lang));
    }
}

