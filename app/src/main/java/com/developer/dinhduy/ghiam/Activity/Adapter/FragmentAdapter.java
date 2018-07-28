package com.developer.dinhduy.ghiam.Activity.Adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.developer.dinhduy.ghiam.Activity.Fragments.FragmentListdata;
import com.developer.dinhduy.ghiam.Activity.Fragments.FragmentRecorder;


public  class FragmentAdapter extends FragmentPagerAdapter {
    private static final int SO_LUONG_PAGE=2;

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    //Tên Các Page Tương Ứng Với Các Fragment Được chọn
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:  return " Phòng Ghi Âm";
            case 1:  return  "Dữ Liệu";
            default: return null;
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FragmentRecorder fragmentRecorder= new FragmentRecorder();
                return fragmentRecorder;
            case 1:
                FragmentListdata fragmentListdata= new FragmentListdata();
                return fragmentListdata;
            default:
            return null;
        }
    }

    // Trả Về  Tổng Số Lượng Fragment đang Muốn  Sử Dụng
    @Override
    public int getCount() {
        return SO_LUONG_PAGE;
    }
}
