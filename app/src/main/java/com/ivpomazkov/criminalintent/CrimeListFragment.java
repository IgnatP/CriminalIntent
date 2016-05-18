package com.ivpomazkov.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

/**
 * Created by Ivpomazkov on 04.05.2016.
 */
public class CrimeListFragment extends Fragment{

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private static UUID mItemChecked;

    public static UUID getItemChecked() {
        return mItemChecked;
    }

    public static void setItemChecked(UUID itemChecked) {
        mItemChecked = itemChecked;
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Crime mCrime;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;

        public CrimeHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_title_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_date_text_view);
            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_crime_solved_check_box);
        }
        public void bindCrime(Crime crime){
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }
        @Override
        public void onClick(View view){
            Intent intent = new Intent(getActivity(),CrimepagerActivity.class);
            CrimeListFragment.setItemChecked(mCrime.getId());
            Log.d("info","CrimeListFragment->startActivity(CrimePagerActivity)");
            startActivity(CrimepagerActivity.newIntent(getActivity(),mCrime.getId()));
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{
        private List<Crime> mCrimes;
        public CrimeAdapter(List<Crime> crimes){
            mCrimes = crimes;
        }
        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int ViewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
            return new CrimeHolder(view);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position){
            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);
        }
        @Override
        public int getItemCount(){
            return mCrimes.size();
        }

        public void setCrimes(List<Crime> crimes){
            mCrimes = crimes;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
       super.onCreate(savedInstanceState);
        Log.d("info","CrimeListFragment in onCreate()");
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        Log.d("info","CrimeListFragment in onCreateView()");
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        Log.d("info","CrimeListFragment in onCreateOptionsMenu()");
        inflater.inflate(R.menu.fragment_crime_list, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = CrimepagerActivity.newIntent(getActivity(), crime.getId());
                startActivity(intent);
                return true;
            case R.id.menu_item_show_subtitle:
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void onResume(){
        super.onResume();
        Log.d("info","CrimeListFragment in onResume()");
        updateUI();
    }
    private void updateUI(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        }
        else{
            mAdapter.setCrimes(crimes);
            mAdapter.notifyDataSetChanged();
        }
    }



    public void updateSubtitle(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle = getString(R.string.subtitle_format, crimeCount);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }
}
