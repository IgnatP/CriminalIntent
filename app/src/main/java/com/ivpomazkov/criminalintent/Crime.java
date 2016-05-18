package com.ivpomazkov.criminalintent;

import android.util.Log;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Ivpomazkov on 02.05.2016.
 */
public class Crime {
    private UUID mId;
    private String mTitle;
    private String mSuspect;
    private Date mDate;
    private boolean mSolved;

    public Crime(){
        this(UUID.randomUUID());
    }
    public Crime (UUID id){
        mId = id;
        mDate = new Date();
    }
    public UUID getId() {
        return mId;
    }
    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String mTitle) { this.mTitle = mTitle; }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean mSolved) {
        this.mSolved = mSolved;
    }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    public String getPhotoFilename(){
        String photoFileName = "IMG_" + getId().toString() + ".jpg";
        return photoFileName;
    }
}
