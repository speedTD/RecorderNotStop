package com.developer.dinhduy.ghiam.Activity.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;

import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.dinhduy.ghiam.Activity.Databases.DBSaveFile;
import com.developer.dinhduy.ghiam.Activity.Databases.OnDatabaseChangedListener;
import com.developer.dinhduy.ghiam.Activity.File;
import com.developer.dinhduy.ghiam.Activity.Fragments.FragmentPlayMusic;
import com.developer.dinhduy.ghiam.R;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ListFileAdapter extends RecyclerView.Adapter<ListFileAdapter.ClassHoder> implements OnDatabaseChangedListener{
    private File fileclass;
    private LinearLayoutManager layout;
    private Context context;
    private DBSaveFile mdb;

    public ListFileAdapter(Context context, LinearLayoutManager manager) {
        mdb=new DBSaveFile(context);
        mdb.setOnDatabaseChangedListener(this);
        layout=manager;
        this.context = context;
    }

    @NonNull
    @Override
    public ClassHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.custom_item_cardview,parent,false);
        return new ClassHoder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull final ClassHoder holder, int position) {
        fileclass =getItem(position);
        long Legth_file=fileclass.getTimeFile();

        long phut= TimeUnit.MILLISECONDS.toMinutes(Legth_file);
        long giay=TimeUnit.MILLISECONDS.toSeconds(Legth_file)- TimeUnit.MINUTES.toSeconds(phut);

        holder.mNameFile.setText(fileclass.getNameFile());
        holder.mDateFile.setText(DateUtils.formatDateTime(
                context,
                fileclass.getDateCreateFile(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_YEAR
        ));
        holder.mTimeFile.setText("Thời lượng "+String.format("%02d:%02d",phut,giay));


        //Sự kiện click Item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    FragmentPlayMusic playbackFragment =
                            new FragmentPlayMusic().NhanItem(getItem(holder.getPosition()));

                    android.support.v4.app.FragmentTransaction transaction = ((FragmentActivity) context)
                            .getSupportFragmentManager()
                            .beginTransaction();
                    playbackFragment.show(transaction, "ok");

                }catch (Exception e){
                    Log.d("365","Faild Show dilog");
                }


            }
        });

        //sự kiện log click
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ArrayList<String> entrys = new ArrayList<String>();
                entrys.add(context.getString(R.string.dialog_file_share));
                entrys.add(context.getString(R.string.dialog_file_rename));
                entrys.add(context.getString(R.string.dialog_file_delete));

                final CharSequence[] items = entrys.toArray(new CharSequence[entrys.size()]);


                // File delete confirm
                AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.Theme_Dialog_main);
                builder.setTitle(context.getString(R.string.dialog_title_options));
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            shareFileDialog(holder.getPosition());
                        } if (item == 1) {
                            renameFileDialog(holder.getPosition());
                        } else if (item == 2) {
                            Log.d("365","Delete");
                            deleteFileDialog(holder.getPosition());
                        }
                    }
                });
                builder.setCancelable(true);
                builder.setNegativeButton(context.getString(R.string.dialog_action_cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
                return false;
            }
        });
    }

    @SuppressLint("StringFormatInvalid")
    public void remove(int position) {
        java.io.File file = new java.io.File(getItem(position).getFilePath());
        file.delete();
        Log.d("365","Deleted");
        Toast.makeText(
                context,
                String.format(
                        context.getString(R.string.toast_file_dcelete),
                        getItem(position).getNameFile()
                ),
                Toast.LENGTH_SHORT
        ).show();

        mdb.DeleteData(getItem(position).getmId());
        notifyItemRemoved(position);
    }
    public void rename(int position,String newName){
        // đổi tên mới
        String filepath= Environment.getExternalStorageDirectory().getAbsolutePath();
        filepath+="/GHI ÂM/"+newName;
        java.io.File file=new java.io.File(filepath);

        if(file.exists()&&!file.isDirectory()){
            Toast.makeText(context, "File Đã Tồn Tại Vui lòng đổi tên khác", Toast.LENGTH_SHORT).show();
        }

        // lấy tên file cũ ra đổi đường dẫn
        java.io.File oldfile=new java.io.File(getItem(position).getFilePath());
        oldfile.renameTo(file);
        //đổi luôn trong database nhé
        mdb.UpdateData(getItem(position),newName,filepath);
        notifyItemChanged(position);
    }
    public void Share(int position){
        Intent sendIntent = new Intent();

        sendIntent.setAction(Intent.ACTION_SEND);

        sendIntent.putExtra(Intent.EXTRA_STREAM,  Uri.fromFile(new java.io.File(getItem(position).getFilePath())));

        sendIntent.setType("Audio/mp3");

        context. startActivity(Intent.createChooser(sendIntent,"Gửi File nè :))" ));
    }

    private void deleteFileDialog(final int position) {
        final AlertDialog.Builder builder=new AlertDialog.Builder(context,R.style.Theme_Dialog_main);
        builder.setTitle("Delete File !");
        builder.setCancelable(true);
        builder.setMessage("Vẫn Muốn Xóa Chứ :V ");
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                remove(position);
                dialogInterface.dismiss();
            }

        });
        builder.setNegativeButton("Không đâu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create();
        builder.show();
    }

    private void  renameFileDialog(final int position) {
        //tạo dialog có editText

        AlertDialog.Builder builder=new AlertDialog.Builder(context,R.style.Theme_Dialog);
        LayoutInflater inflater = null;
        View view=inflater.from(context).inflate(R.layout.dialog_rename,null);
        final TextInputEditText medt=(TextInputEditText)  view.findViewById(R.id.id_dialog_mini_editText);
        final TextView txt=(TextView)  view.findViewById(R.id.id_txt_namemp3);
        builder.setTitle("Đổi Tên");
        builder.setMessage("Muốn Đổi Tên gì ?");
        builder.setCancelable(true);
        builder.setPositiveButton("Đồng Ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String newname=medt.getText().toString().trim()+".mp3";
                rename(position,newname);
                Toast.makeText(context, "Đổi Tên Thành Công", Toast.LENGTH_SHORT).show();
                dialogInterface.dismiss();
            }
        });

        builder.setNegativeButton("Không :(", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
// nhớ add view nhé hihih
        builder.setView(view);
        builder.create();
        builder.show();


    }

    private void  shareFileDialog(final int position) {
     Share(position);
    }

    public File getItem(int position) {
        return mdb.GetItem(position);
    }

    @Override
    public int getItemCount() {
        return mdb.getCount();
    }

    @Override
    public void onNewDatabaseEntryAdded() {
        notifyItemInserted(getItemCount() - 1);
        layout.scrollToPosition(getItemCount() - 1);
    }

    @Override
    public void onDatabaseEntryRenamed() {

    }

    public class ClassHoder extends RecyclerView.ViewHolder {
        private TextView mNameFile,mDateFile,mTimeFile;
        private ImageView mImage;
        public ClassHoder(View itemView) {
            super(itemView);
            mNameFile=(TextView) itemView.findViewById(R.id.id_NameFile);
            mTimeFile=(TextView) itemView.findViewById(R.id.id_time_count_file);
            mDateFile=(TextView) itemView.findViewById(R.id.id_đate_create);
            mImage=(ImageView) itemView.findViewById(R.id.img);

        }
    }
}
