package com.developer.dinhduy.ghiam.Activity;

import android.os.Parcel;
import android.os.Parcelable;

public class File implements Parcelable {
    private String NameFile;  // tên file
    private long DateCreateFile;  //Ngày Tạo file
    private String FilePath;  // Đường dẫn file
    private int TimeFile;  // độ dài của file
    private int mId; //id in database

    public File(String nameFile, long dateCreateFile, String filePath, int timeFile, int mId) {
        NameFile = nameFile;
        DateCreateFile = dateCreateFile;
        FilePath = filePath;
        TimeFile = timeFile;
        this.mId = mId;
    }

    public File() {
    }

    protected File(Parcel in) {
        NameFile = in.readString();
        DateCreateFile = in.readLong();
        FilePath = in.readString();
        TimeFile = in.readInt();
        mId = in.readInt();
    }

    public static final Creator<File> CREATOR = new Creator<File>() {
        @Override
        public File createFromParcel(Parcel in) {
            return new File(in);
        }

        @Override
        public File[] newArray(int size) {
            return new File[size];
        }
    };

    public String getNameFile() {
        return NameFile;
    }

    public void setNameFile(String nameFile) {
        NameFile = nameFile;
    }

    public long getDateCreateFile() {
        return DateCreateFile;
    }

    public void setDateCreateFile(long dateCreateFile) {
        DateCreateFile = dateCreateFile;
    }

    public String getFilePath() {
        return FilePath;
    }

    public void setFilePath(String filePath) {
        FilePath = filePath;
    }

    public int getTimeFile() {
        return TimeFile;
    }

    public void setTimeFile(int timeFile) {
        TimeFile = timeFile;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(NameFile);
        parcel.writeLong(DateCreateFile);
        parcel.writeString(FilePath);
        parcel.writeInt(TimeFile);
        parcel.writeInt(mId);
    }
}
