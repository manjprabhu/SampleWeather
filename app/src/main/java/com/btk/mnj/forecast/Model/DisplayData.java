package com.btk.mnj.forecast.Model;

public class DisplayData {

    private final String TAG = DisplayData.class.getSimpleName();

    private String mCityName, mDescription, mCurrentTemp, mMaxTemp, mMinTemp;

    public void setmCityName(String mCityName) {
        this.mCityName = mCityName;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public void setmCurrentTemp(String mCurrentTemp) {
        this.mCurrentTemp = mCurrentTemp;
    }

    public void setmMaxTemp(String mMaxTemp) {
        this.mMaxTemp = mMaxTemp;
    }

    public void setmMinTemp(String mMinTemp) {
        this.mMinTemp = mMinTemp;
    }
}
