package com.fcfm.cambia_10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.fcfm.cambia_10.utilidades.utilidades;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class resumen extends AppCompatActivity {

    private ListView lvResumen;
    private resumen_result_adapter adapter;
    private List<resumen_result> mResumenList;
    private Button guardar;
    ArrayList<contact_result> Seleccion;
    ArrayList<contact_result> Respaldo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_resumen);
        getData();


    }

    public void getData(){

        lvResumen = findViewById(R.id.listView3);
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
        Respaldo = new ArrayList<>();
        Seleccion = (ArrayList<contact_result> ) getIntent().getSerializableExtra("Selecciones");

        contact_result resultR;
        String lastFourDigits;
        while (c.moveToNext()) {
            for (int i = Seleccion.size() - 1; i >= 0; i--) {
                resultR = Seleccion.get(i);
                if(resultR.getId() == c.getInt(0)){
                    Respaldo.add(new contact_result(c.getInt(0), c.getString(1), "", "", c.getString(2), false));
                }
            }
        }

        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);*/

        mResumenList = new ArrayList<>();
        contact_result result;
        for (int i = Seleccion.size() - 1; i >= 0; i--) {
            result = Seleccion.get(i);
            mResumenList.add(new resumen_result(result.getId(), result.getNombre(), "+52 "+ result.getNumero()));
        }
        //Init adapter
        adapter = new resumen_result_adapter(getApplicationContext(), mResumenList);
        lvResumen.setAdapter(adapter);

        lvResumen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                resumen_result result = mResumenList.get(i);
                //Update adapter
                adapter.UpdateRecords(mResumenList);
                //Display msg with id from view.getTag
                Toast.makeText(getApplicationContext(), "Contacto id: " + result.getId(), Toast.LENGTH_SHORT).show();
            }
        });
        guardar = findViewById(R.id.guardar);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    respaldoContactos();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                /*Intent res = new Intent(getApplicationContext(), fin.class);
                startActivity(res);*/
            }
        });

    }
    public void respaldoContactos() throws Exception{

        ComexionSQLiteHelper conn = new ComexionSQLiteHelper(getApplicationContext(),"bd_respaldo",null,1);
        SQLiteDatabase db = conn.getWritableDatabase();
        Long idResultante = null;
        resumen_result result;
        contact_result resultRespaldo;
        String lastFourDigits = null;
        String n_aguardar = null;
        for (int i = mResumenList.size() - 1; i >= 0; i--) {
            result = mResumenList.get(i);
            resultRespaldo = Respaldo.get(i);

            ContentValues values = new ContentValues();
            //Set values to table contact
            values.put(utilidades.CAMPO_ID, String.valueOf(resultRespaldo.getId()));
            values.put(utilidades.CAMPO_ID_CONTACT, String.valueOf(resultRespaldo.getId()));
            values.put(utilidades.CAMPO_NOMBRE, String.valueOf(resultRespaldo.getNombre()));
            values.put(utilidades.CAMPO_NUMERO, String.valueOf(resultRespaldo.getNumero()));
            values.put(utilidades.CAMPO_FECHA, String.valueOf(Calendar.getInstance().getTime()));
            idResultante = db.insert(utilidades.TABLA_CONTACTO, null, values);
            //set clean number to real contact app
            n_aguardar = result.getNumero();

            ArrayList<ContentProviderOperation> ops =
                    new ArrayList<ContentProviderOperation>();

            ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                    .withSelection(ContactsContract.Data._ID + "=?", new String[]{String.valueOf(result.getId())})
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, n_aguardar)
                    .build());
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        }
        db.close();
        Toast.makeText(getApplicationContext(), "idResgistro: "+idResultante,Toast.LENGTH_SHORT).show();
        if(idResultante != null){
            Intent res = new Intent(getApplicationContext(), fin.class);
            startActivity(res);
        }

    }
}
