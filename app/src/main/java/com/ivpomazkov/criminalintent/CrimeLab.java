package com.ivpomazkov.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.ivpomazkov.criminalintent.database.CrimeBaseHelper;
import com.ivpomazkov.criminalintent.database.CrimeCursorWrapper;
import com.ivpomazkov.criminalintent.database.CrimeDbSchema;
import com.ivpomazkov.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Ivpomazkov on 02.05.2016.
 */
public class CrimeLab {
    private  static CrimeLab sCrimeLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CrimeLab get(Context context){
            if (sCrimeLab == null){
                sCrimeLab = new CrimeLab(context);
            }
            return sCrimeLab;
    }

    private static ContentValues getContentValues(Crime crime){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CrimeTable.Cols.UUID, crime.getId().toString());
        contentValues.put(CrimeTable.Cols.TITLE, crime.getTitle());
        contentValues.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        contentValues.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        contentValues.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());
        return contentValues;
    }

    private CrimeLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();

    }
    public void addCrime(Crime mCrime){
        ContentValues contentValues = getContentValues(mCrime);
        mDatabase.insert(CrimeTable.NAME, null, contentValues);
    }
    public void updateCrime(Crime crime){
        String uuidString = crime.getId().toString();
        ContentValues contentValues = getContentValues(crime);
        mDatabase.update(CrimeTable.NAME, contentValues, CrimeTable.Cols.UUID + " = ?",
                new String[]{ uuidString });
    }
    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new CrimeCursorWrapper(cursor);
    }
    public List<Crime> getCrimes(){
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursor = queryCrimes(null, null);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }
        finally{
            cursor.close();
        }
        return crimes;
    }

    public Crime getCrime(UUID id){
        CrimeCursorWrapper cursor = queryCrimes(
                CrimeTable.Cols.UUID + " = ?",
                new String[]{ id.toString() }
        );

        try{
            if (cursor.getCount() == 0)
                return null;
            cursor.moveToFirst();
            return cursor.getCrime();
        }
        finally{
            cursor.close();
        }

    }
    public boolean deleteCrime(UUID id)
    {
        mDatabase.delete(CrimeTable.NAME, CrimeTable.Cols.UUID + " = ?", new String[]{id.toString()});
        return true;
    }

    public File getPhotoFile(Crime crime){
        File externalFileDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (externalFileDir == null) return null;
        return new File(externalFileDir, crime.getPhotoFilename());
    }
}
