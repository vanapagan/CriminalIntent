package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {

    private static final String EXTRA_CRIME_ID = "com.bignerdranch.anroid.criminalintent.crime_id";

    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    private Button mFirstButton;
    private Button mLastButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        mViewPager = findViewById(R.id.crime_view_pager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener () {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (mViewPager.getCurrentItem() == 0) {
                    mFirstButton.setVisibility(View.INVISIBLE);
                } else {
                    mFirstButton.setVisibility(View.VISIBLE);
                }

                if(position == mViewPager.getAdapter().getCount() - 1) {
                    mLastButton.setVisibility(View.INVISIBLE);
                }

                if (mViewPager.getCurrentItem() == (mCrimes.size() - 1)) {
                    mLastButton.setVisibility(View.INVISIBLE);
                } else {
                    mLastButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });
        mFirstButton = findViewById(R.id.first_button);
        mLastButton = findViewById(R.id.last_button);

        mCrimes = CrimeLab.get(this).getCrimes();

        mFirstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentItem(0);
            }
        });

        mLastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentItem(mViewPager.getAdapter().getCount() - 1);
            }
        });

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

        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crimeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }

    }

    private boolean isFirstItem(UUID crimeId) {
        return isXthItem(crimeId, 0);
    }

    private boolean isLastItem(UUID crimeId) {
        return isXthItem(crimeId, mCrimes.size() - 1);
    }

    private boolean isXthItem(UUID crimeId, int position) {
        return mCrimes.get(position).getId() == crimeId;
    }

    private void setCurrentItem(int position) {
        if (mCrimes.size() > 0) {
            mViewPager.setCurrentItem(position);
        }
    }

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

}
