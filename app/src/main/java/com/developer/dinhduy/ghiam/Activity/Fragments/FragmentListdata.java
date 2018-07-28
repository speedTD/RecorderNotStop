package com.developer.dinhduy.ghiam.Activity.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developer.dinhduy.ghiam.Activity.Adapter.ListFileAdapter;
import com.developer.dinhduy.ghiam.R;


public class FragmentListdata extends Fragment {
    private ListFileAdapter adapter;
    private RecyclerView recyclerView;
    public FragmentListdata() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_fragment_listdata, container, false);
        // Inflate the layout for this fragment
        recyclerView=(RecyclerView) view.findViewById(R.id.id_RecyclerviewFile);
        LinearLayoutManager manager=new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter=new ListFileAdapter(getContext(),manager);
        recyclerView.setAdapter(adapter);
        return  view;
    }

}
