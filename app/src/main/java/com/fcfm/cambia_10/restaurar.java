package com.fcfm.cambia_10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.RemoteException;
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
import java.util.List;

public class restaurar extends AppCompatActivity {

    private ListView lvRestaurar;
    private restaurar_result_adapter adapter;
    private List<restaurar_result> mRestauraList;
    private List<restaurar_result> seleccion;
    private Button restaurar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_restaurar);

        getData();
    }

    public void getData(){

        lvRestaurar = findViewById(R.id.listView2);
        //Add sample data for list or DB, webservice, etc.

        /*String[] projection = new String [] {ContactsContract.Data._ID, ContactsContract.Data.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
        String selectionClause = ContactsContract.Data.MIMETYPE + "='"+
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "' AND "
                + ContactsContract.CommonDataKinds.Phone.NUMBER + " IS NOT NULL";
        String sortOrder = ContactsContract.Data.DISPLAY_NAME + " ASC";

        Cursor c = getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                projection,
                selectionClause,
                null,
                sortOrder);*/

        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);*/

        mRestauraList = new ArrayList<>();
        ComexionSQLiteHelper conn = new ComexionSQLiteHelper(getApplicationContext(),"bd_respaldo",null,1);
        SQLiteDatabase db = conn.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from contact",null);
        if (cursor.moveToFirst()) {
            String lastFourDigits;
            while (!cursor.isAfterLast()) {
                String number = cursor.getString(3);
                String clean = number.replaceAll("[^\\d]", "" );
                    lastFourDigits = clean.substring(clean.length() - 10);
                mRestauraList.add(new restaurar_result(cursor.getInt(1), cursor.getString(2), "+52 ("+lastFourDigits.substring(0,2)+")"+" "+lastFourDigits.substring(2,6)+" "+lastFourDigits.substring(6,10), "", false));
               /* mRestauraList.add(new restaurar_result(cursor.getInt(1), cursor.getString(2), cursor.getString(3), "", false));*/
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
      /*  while (c.moveToNext()){
            String number = c.getString(2);
            String clean = number.replaceAll("[^\\d]", "" );
            int count =  clean.length();
                mRestauraList.add(new restaurar_result(c.getInt(0), c.getString(1), number, "Restaurado", false));
        }*/
        //Init adapter
        adapter = new restaurar_result_adapter(getApplicationContext(), mRestauraList);
        lvRestaurar.setAdapter(adapter);

        lvRestaurar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                restaurar_result result = mRestauraList.get(i);
                if(result.isSelected()){
                    result.setSelected(false);
                }else{
                    result.setSelected(true);
                }
                mRestauraList.set(i, result);

                //Update adapter
                adapter.UpdateRecords(mRestauraList);
                //Display msg with id from view.getTag
                Toast.makeText(getApplicationContext(), "Contacto id: " + result.getId(), Toast.LENGTH_SHORT).show();
            }
        });
        restaurar = findViewById(R.id.restaurar);
        restaurar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccion = new ArrayList<>();
                restaurar_result result_restaurar;
                String n_aguardar = null;
                for (int i = mRestauraList.size() - 1; i >= 0; i--) {
                    result_restaurar = mRestauraList.get(i);
                    if (result_restaurar.isSelected()) {
                        n_aguardar = result_restaurar.getNumero();

                        ComexionSQLiteHelper connG = new ComexionSQLiteHelper(getApplicationContext(),"bd_respaldo",null,1);
                        SQLiteDatabase dbG = connG.getWritableDatabase();
                        Cursor cursor = dbG.rawQuery("select * from contact",null);
                        if (cursor.moveToFirst()) {
                            String lastFourDigits;
                            while (!cursor.isAfterLast()) {
                                String idDB = cursor.getString(1);
                                String idList = String.valueOf(result_restaurar.getId());

                                if( idDB.equals(idList)){
                                    n_aguardar = cursor.getString(3);
                                    ArrayList<ContentProviderOperation> ops =
                                            new ArrayList<ContentProviderOperation>();

                                    ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                                            .withSelection(ContactsContract.Data._ID + "=?", new String[]{idList})
                                            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, n_aguardar)
                                            .build());
                                    try {
                                        getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                                    } catch (OperationApplicationException e) {
                                        e.printStackTrace();
                                    } catch (RemoteException e) {
                                        e.printStackTrace();
                                    }
                                }

                                /*mRestauraList.add(new restaurar_result(cursor.getInt(1), cursor.getString(2),cursor.getString(3) , "", false));*/
                                cursor.moveToNext();
                            }
                            dbG.close();
                        }


                        ComexionSQLiteHelper conn = new ComexionSQLiteHelper(getApplicationContext(), "bd_respaldo", null, 1);
                        SQLiteDatabase db = conn.getWritableDatabase();
                        db.rawQuery("select * from contact", null);
                         int elimino = db.delete(utilidades.TABLA_CONTACTO, utilidades.CAMPO_ID_CONTACT + "=" + result_restaurar.getId(), null);
                         if(elimino > 0){
                             result_restaurar.setEstado("Restaurado");
                             mRestauraList.set(i, result_restaurar);
                             adapter.UpdateRecords(mRestauraList);
                             db.close();
                         }
                    }

                }

            }
        });

    }
}
