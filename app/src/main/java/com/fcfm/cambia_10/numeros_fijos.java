package com.fcfm.cambia_10;

import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class numeros_fijos extends AppCompatActivity {

    private ListView lvFijos;
    private fijo_result_adapter adapter2;
    private List<fijo_result> mFijosList;
    ArrayList<contact_result> SeleccionAnt;
    private Button continuar;
    private Button selectEstado;
    private Button ok;
    AutoCompleteTextView list_estados;
    ImageView flecha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_numeros_fijos);

        getData();


    }

    public void getData() {

        lvFijos = findViewById(R.id.listView2);
        //Add sample data for list or DB, webservice, etc.

        String[] projection = new String[]{ContactsContract.Data._ID, ContactsContract.Data.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
        String selectionClause = ContactsContract.Data.MIMETYPE + "='" +
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "' AND "
                + ContactsContract.CommonDataKinds.Phone.NUMBER + " IS NOT NULL";
        String sortOrder = ContactsContract.Data.DISPLAY_NAME + " ASC";

        Cursor c = getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                projection,
                selectionClause,
                null,
                sortOrder);

        mFijosList = new ArrayList<>();
        while (c.moveToNext()) {
            String number = c.getString(2);
            String clean = number.replaceAll("[^\\d]", "");
            int count = clean.length();
            if (count <= 8) {
                mFijosList.add(new fijo_result(c.getInt(0), c.getString(1), "", number, "", false));
            }
        }
        SeleccionAnt = (ArrayList<contact_result>) getIntent().getSerializableExtra("Selecciones");
        //Init adapter
        adapter2 = new fijo_result_adapter(getApplicationContext(), mFijosList);
        lvFijos.setAdapter(adapter2);
        lvFijos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                fijo_result result = mFijosList.get(i);
                if (result.isSelected()) {
                    result.setSelected(false);
                } else {
                    result.setSelected(true);
                }
                mFijosList.set(i, result);

                //DETECTA EL LISTBOX Y DEPENDIENDO DEL VALOR AGREGA A LOS SELECCIONADOS ESE ESTADO QUE SELECCIONO, AQUITAR = LADA 3 O 4 DIGITOS
                //DESPUES DESELECCIONA TODOS ASIGNANDOLES EL VALOR SELECTED A FALSE
                //Update adapter
                adapter2.UpdateRecords(mFijosList);
                //Display msg with id from view.getTag
                Toast.makeText(getApplicationContext(), "Contacto id: " + result.getId(), Toast.LENGTH_SHORT).show();
            }
        });
        continuar = findViewById(R.id.continuar);
        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fijo_result result;
                //OBTIENE LOS FIJOS QUE SU AQUITAR != "", IF SELECTED NO SIRVE
                //DETECTAR SI ES DE 7 U 8 DIGITOS PARA SOLO AGREGAR NUMERO SIN EL 52 Y PASARLO A RESUMEN
                //AQUI VA EL FORMATEO DEL NUMERO A LOCAL CONCATENANDO LA LADA
                for (int i = mFijosList.size() - 1; i >= 0; i--) {
                    result = mFijosList.get(i);
                    if (result.isSelected()) {
                        SeleccionAnt.add(new contact_result(result.getId(), result.getNombre(), "n", "n", result.getNumero(), false));
                        result.setSelected(false);
                    }
                }
                Intent intent = new Intent(getApplicationContext(), resumen.class);
                intent.putExtra("Selecciones", (Serializable) SeleccionAnt);
                startActivity(intent);
            }
        });
        selectEstado = findViewById(R.id.selectEst);
        selectEstado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog_1(numeros_fijos.this);
            }
        });


    }

    public void openDialog_1(Activity activity) {

        final Dialog dialog;
        dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_spinner);

        Bitmap map = takeScreenShot(activity);

        Bitmap fast = fastblur(map, 20);
        final Drawable draw = new BitmapDrawable(getResources(), fast);
        dialog.getWindow().setBackgroundDrawable(draw);
        list_estados = dialog.findViewById(R.id.estado);
        list_estados.setThreshold(2);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, estados_lista);
        list_estados.setAdapter(adapter);
        ok = dialog.findViewById(R.id.aceptar);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fijo_result result;
                for (int i = mFijosList.size() - 1; i >= 0; i--){
                    result = mFijosList.get(i);
                    if(result.isSelected()){
                        result.setAgregar("81 ");
                        result.setEstado("Nuevo León");
                        mFijosList.set(i, result);
                    }
                }
                //Update adapter
                adapter2.UpdateRecords(mFijosList);
                dialog.dismiss();
            }
        });
        flecha = dialog.findViewById(R.id.arrow);
        flecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list_estados.showDropDown();
            }
        });

        dialog.show();

    }
    private static final String[] estados_lista =
            new String[]{"Acapulco","Aguascalientes","Apizaco","Atlixco","Campeche","Cancún","Celaya","Ciudad Cuauhtémoc",
                    "Ciudad de México","Ciudad del Carmen","Ciudad Delicias","Ciudad Guzmán","Ciudad Juárez","Ciudad Lázaro Cárdenas",
                    "Ciudad Mante","Ciudad Obregón","Ciudad Valles","Ciudad Victoria","Coatzacoalcos","Colima","Córdoba",
                    "Cuautla","Cuernavaca","Culiacán","Chetumal","Chihuahua","Chilpacingo","Chinameca",
                    "Durango","Ensenada","Fresnillo","Guadalajara","Guanajuato","Guaymas","Hermosillo","Irapuato","Ixtepec","Jalapa","Jiménez",
                    "La Paz","La Piedad","Lagos de Moreno","León","Lerma","Los Mochis","Manzanillo","Matamoros","Mayanalán","Mazatlán","Mérida",
                    "Mexicali","Monclova","Monterrey","Morelia","Moroleón","Navojao","Nogales","Nuevo Laredo","Oaxaca",
                    "Ocotlán","Orizaba","Pachuca","Parral","Polotitlán","Poza Rica","Puebla","Puerto Vallarta","Querétaro",
                    "Reynosa","Sabinas","Sahuayo","Salamanca","Saltillo","San José del Cabo","San Luis Potosí",
                    "San Luis Río Colorado","San Marcos Nepantla","San Martín Texmelucan","San Miguel de Allende","Singuilucan",
                    "Tampico","Tapachula","Taxco","Tehuacán","Tepatitlan","Tepeji del Rio","Tepic","Texcoco","Tijuana",
                    "Tlaxcala","Toluca","Torreón","Tuxpan","Tuxtla Gutiérrez","Uruapan","Veracruz","Villahermosa","Zacatecas",
                    "Zacatepec","Zamora","Zihuatanejo"};
    private static final String[] estados_ladas =
            new String[]{"Acapulco","Aguascalientes","Apizaco","Atlixco","Campeche","Cancún","Celaya","Ciudad Cuauhtémoc",
                    "Ciudad de México","Ciudad del Carmen","Ciudad Delicias","Ciudad Guzmán","Ciudad Juárez","Ciudad Lázaro Cárdenas",
                    "Ciudad Mante","Ciudad Obregón","Ciudad Valles","Ciudad Victoria","Coatzacoalcos","Colima","Córdoba",
                    "Cuautla","Cuernavaca","Culiacán","Chetumal","Chihuahua","Chilpacingo","Chinameca",
                    "Durango","Ensenada","Fresnillo","Guadalajara","Guanajuato","Guaymas","Hermosillo","Irapuato","Ixtepec","Jalapa","Jiménez",
                    "La Paz","La Piedad","Lagos de Moreno","León","Lerma","Los Mochis","Manzanillo","Matamoros","Mayanalán","Mazatlán","Mérida",
                    "Mexicali","Monclova","Monterrey","Morelia","Moroleón","Navojao","Nogales","Nuevo Laredo","Oaxaca",
                    "Ocotlán","Orizaba","Pachuca","Parral","Polotitlán","Poza Rica","Puebla","Puerto Vallarta","Querétaro",
                    "Reynosa","Sabinas","Sahuayo","Salamanca","Saltillo","San José del Cabo","San Luis Potosí",
                    "San Luis Río Colorado","San Marcos Nepantla","San Martín Texmelucan","San Miguel de Allende","Singuilucan",
                    "Tampico","Tapachula","Taxco","Tehuacán","Tepatitlan","Tepeji del Rio","Tepic","Texcoco","Tijuana",
                    "Tlaxcala","Toluca","Torreón","Tuxpan","Tuxtla Gutiérrez","Uruapan","Veracruz","Villahermosa","Zacatecas",
                    "Zacatepec","Zamora","Zihuatanejo"};

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
}
