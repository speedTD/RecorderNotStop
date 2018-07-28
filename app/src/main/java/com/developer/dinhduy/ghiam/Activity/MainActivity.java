package com.developer.dinhduy.ghiam.Activity;

import android.content.Intent;
import android.graphics.Point;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;

import com.developer.dinhduy.ghiam.Activity.Adapter.FragmentAdapter;
import com.developer.dinhduy.ghiam.Activity.Fragments.SettingFragment;
import com.developer.dinhduy.ghiam.R;

public class MainActivity extends AppCompatActivity {
    private ViewPager mviewPager;
    private TabLayout mtabLayout;
    private FragmentPagerAdapter fAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mviewPager=(ViewPager) findViewById(R.id.id_viewPager);
        mtabLayout=(TabLayout) findViewById(R.id.id_Tablayout);
        // get dữ liệu cho adapter
        fAdapter=new FragmentAdapter(getSupportFragmentManager());
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.d("365", "width: "+width+" :height"+height);
        mviewPager.setAdapter(fAdapter);


        mtabLayout.setupWithViewPager(mviewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.id_menu_caidat){
            startActivity(new Intent(MainActivity.this,SettingActivity.class));
        }
        return super.onOptionsItemSelected(item);

    }
}
