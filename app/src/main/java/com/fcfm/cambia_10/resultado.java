package com.fcfm.cambia_10;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Dialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class resultado extends AppCompatActivity {
    private ListView listview;
    private ArrayList<String> names;
    private String tipo;

    private ListView lvContact;
    private contact_result_adapter adapter;
    private List<contact_result> mContactList;
    private List<contact_result> seleccion;
    private Button convertir;
    private Button selectAll;
    private Button addClave;
    private Button addLater;
    private Button Ok;
    private Button Ok2;
    private Dialog dialogRef;
    private Button closes;
    private Button closes2;
    private Button closes3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        getData();
        convertir = findViewById(R.id.convertir);
        convertir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog_1(resultado.this);
            }
        });

    }

    public void openDialog_1(Activity activity){

        final Dialog dialog;
        dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_dialog_1);

        Bitmap map = takeScreenShot(activity);

        Bitmap fast=fastblur(map, 20);
        final Drawable draw = new BitmapDrawable(getResources(),fast);
        dialog.getWindow().setBackgroundDrawable(draw);

        addLater = dialog.findViewById(R.id.masTarde);
        addLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog3;
                dialog3 = new Dialog(resultado.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                dialog3.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog3.setCancelable(false);
                dialog3.setContentView(R.layout.layout_dialog_2);

                dialog3.getWindow().setBackgroundDrawable(draw);

                Ok2 = dialog3.findViewById(R.id.entendido);
                Ok2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        seleccion = new ArrayList<>();
                        contact_result result;
                        for (int i = mContactList.size() - 1; i >= 0; i--){
                            result = mContactList.get(i);
                            if(result.isSelected()){
                                if(result.getNacional().equals("")){
                                    seleccion.add(new contact_result(result.getId(), result.getNombre(), "", "", result.getNumero(), false));
                                }else{
                                    seleccion.add(new contact_result(result.getId(), result.getNombre(), "Lada sin costo", "", result.getNumero(), false));
                                }
                                result.setSelected(false);
                            }
                        }
                        dialog.dismiss();
                        dialog3.dismiss();
                        Intent intent3 = new Intent(getApplicationContext(), resumen.class);
                        intent3.putExtra("Selecciones", (Serializable) seleccion);
                        startActivity(intent3);
                    }
                });

                dialog3.show();
            }
        });
        addClave = dialog.findViewById(R.id.addClave);
        addClave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog2;
                dialog2 = new Dialog(resultado.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog2.setCancelable(false);
                dialog2.setContentView(R.layout.layout_dialog_2);


                dialog2.getWindow().setBackgroundDrawable(draw);

                Ok = dialog2.findViewById(R.id.entendido);
                Ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        seleccion = new ArrayList<>();
                        contact_result result;
                        for (int i = mContactList.size() - 1; i >= 0; i--){
                            result = mContactList.get(i);
                            if(result.isSelected()){
                                if(result.getNacional().equals("")){
                                    seleccion.add(new contact_result(result.getId(), result.getNombre(), "", "", result.getNumero(), false));
                                }else{
                                    seleccion.add(new contact_result(result.getId(), result.getNombre(), "Lada sin costo", "", result.getNumero(), false));
                                }
                                result.setSelected(false);
                            }
                        }
                        dialog.dismiss();
                        dialog2.dismiss();
                        Intent intent3 = new Intent(getApplicationContext(), numeros_fijos.class);
                        intent3.putExtra("Selecciones", (Serializable) seleccion);
                        startActivity(intent3);
                    }
                });

                dialog2.show();

            }
        });
        closes = dialog.findViewById(R.id.close_ico);
        closes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private static Bitmap takeScreenShot(Activity activity)
    {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();

        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height  - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    public Bitmap fastblur(Bitmap sentBitmap, int radius) {
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = ( 0xff000000 & pix[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }

    public void getData(){

        lvContact = findViewById(R.id.listView);
        //Add sample data for list or DB, webservice, etc.

        String[] projection = new String [] {ContactsContract.Data._ID, ContactsContract.Data.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
        String selectionClause = ContactsContract.Data.MIMETYPE + "='"+
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "' AND "
                + ContactsContract.CommonDataKinds.Phone.NUMBER + " IS NOT NULL";
        String sortOrder = ContactsContract.Data.DISPLAY_NAME + " ASC";

        Cursor c = getContentResolver().query(
          ContactsContract.Data.CONTENT_URI,
          projection,
          selectionClause,
          null,
          sortOrder);

        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);*/



        mContactList = new ArrayList<>();
        String lastFourDigits;
        while (c.moveToNext()){
            String number = c.getString(2);
            String clean = number.replaceAll("[^\\d]", "" );
            int count =  clean.length();
            if(count > 10 ){
                lastFourDigits = clean.substring(clean.length() - 10);
                if(clean.substring(0, 2).equals("52")
                        || clean.substring(0,3).equals("044")
                        || clean.substring(0,3).equals("045")
                        || clean.substring(0,2).equals("01")
                        || clean.substring(0,1).equals("1")) {
                    if (clean.substring(0, 2).equals("52")) {
                        if (clean.substring(0, 5).equals("52044")) {
                            //tipo = "  Celular México";
                            mContactList.add(new contact_result(c.getInt(0), c.getString(1), "+52 ", "044 ", "("+lastFourDigits.substring(0,2)+")"+" "+lastFourDigits.substring(2,6)+" "+lastFourDigits.substring(6,10), false));
                        } else if (clean.substring(0, 5).equals("52045")) {
                            //tipo = "  Celular México";
                            mContactList.add(new contact_result(c.getInt(0), c.getString(1), "+52 ", "045 ", "("+lastFourDigits.substring(0,2)+")"+" "+lastFourDigits.substring(2,6)+" "+lastFourDigits.substring(6,10), false));
                        }  else if (clean.substring(0, 4).equals("5201")) {
                            //tipo = "  Celular México";
                            mContactList.add(new contact_result(c.getInt(0), c.getString(1), "+52 ", "01 ", "("+lastFourDigits.substring(0,2)+")"+" "+lastFourDigits.substring(2,6)+" "+lastFourDigits.substring(6,10),false));
                        } else if (clean.substring(0, 3).equals("521")) {
                            //tipo = "  Celular México";
                            mContactList.add(new contact_result(c.getInt(0), c.getString(1), "+52 ", "1 ", "("+lastFourDigits.substring(0,2)+")"+" "+lastFourDigits.substring(2,6)+" "+lastFourDigits.substring(6,10),false));
                        }
                    } else if (clean.substring(0, 3).equals("044")) {
                        //tipo = "  Celular México";
                        mContactList.add(new contact_result(c.getInt(0), c.getString(1), "+52 ", "044 ", "("+lastFourDigits.substring(0,2)+")"+" "+lastFourDigits.substring(2,6)+" "+lastFourDigits.substring(6,10), false));
                    } else if (clean.substring(0, 3).equals("045")) {
                        //tipo = "  Celular México";
                        mContactList.add(new contact_result(c.getInt(0), c.getString(1), "+52 ", "045 ", "("+lastFourDigits.substring(0,2)+")"+" "+lastFourDigits.substring(2,6)+" "+lastFourDigits.substring(6,10), false));
                    } else if (clean.substring(0, 2).equals("01")) {
                        if(clean.substring(0, 5).equals("01800") || clean.substring(0, 5).equals("01900")){
                            mContactList.add(new contact_result(c.getInt(0), c.getString(1), "LC ", "01 ", "("+lastFourDigits.substring(0,3)+")"+" "+lastFourDigits.substring(3,7)+" "+lastFourDigits.substring(7,10), false));
                        }else{
                            //tipo = "  Celular México";
                            mContactList.add(new contact_result(c.getInt(0), c.getString(1), "+52 ", "01 ", "("+lastFourDigits.substring(0,2)+")"+" "+lastFourDigits.substring(2,6)+" "+lastFourDigits.substring(6,10), false));
                        }

                    } else if (clean.substring(0, 1).equals("1")) {
                       //tipo = "  Celular México";
                        mContactList.add(new contact_result(c.getInt(0), c.getString(1), "+52 ", "1 ", "("+lastFourDigits.substring(0,2)+")"+" "+lastFourDigits.substring(2,6)+" "+lastFourDigits.substring(6,10), false));
                    }

                }
            }

        }
        //Init adapter
        adapter = new contact_result_adapter(getApplicationContext(), mContactList);
        lvContact.setAdapter(adapter);

        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                contact_result result = mContactList.get(i);
                if(result.isSelected()){
                    result.setSelected(false);
                }else{
                    result.setSelected(true);
                }
                mContactList.set(i, result);

                //Update adapter
                adapter.UpdateRecords(mContactList);
                //Display msg with id from view.getTag
                //Toast.makeText(getApplicationContext(), "Contacto id: " + result.getId(), Toast.LENGTH_SHORT).show();
            }
        });
        selectAll = findViewById(R.id.selectAll);
        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               contact_result resultSelectAll;
                for (int i = mContactList.size() - 1; i >= 0; i--){
                    resultSelectAll = mContactList.get(i);
                    resultSelectAll.setSelected(true);
                    mContactList.set(i, resultSelectAll);
                }
                //Update adapter
                adapter.UpdateRecords(mContactList);
            }
        });

    }

}
