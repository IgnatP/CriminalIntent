package com.ivpomazkov.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.List;
import java.util.UUID;

/**
 * Created by Ivpomazkov on 05.05.2016.
 */
public class CrimepagerActivity extends AppCompatActivity {
    private static final String EXTRA_CRIME_ID = "com.ivpomazkov.criminalintent.crime_id";
    private ViewPager mViewPager;
    private List<Crime> mCrimes;

    public static Intent newIntent(Context packageContext, UUID crimeId){
        Intent intent = new Intent(packageContext, CrimepagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        Log.d("info","CrimePagerActivity in newIntent()");
        return intent;
    }

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d("info","CrimePagerActivity in onCreate()");
        setContentView(R.layout.activity_crime_pager);

        UUID crimeId = (UUID)getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_crime_pager_view_pager);
        mCrimes = CrimeLab.get(this).getCrimes();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });
        for(int i = 0; i < mCrimes.size(); i++)
            if (mCrimes.get(i).getId().equals(crimeId)){
                mViewPager.setCurrentItem(i);
                break;
            }
    }


}
