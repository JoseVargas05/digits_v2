package com.fcfm.cambia_10;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
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
    private Button Ok;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_numeros_fijos);

        getData();


    }

    public void getData(){

        lvFijos = findViewById(R.id.listView2);
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

        mFijosList = new ArrayList<>();
        while (c.moveToNext()){
            String number = c.getString(2);
            String clean = number.replaceAll("[^\\d]", "" );
            int count =  clean.length();
            if(count <= 8){
                mFijosList.add(new fijo_result(c.getInt(0), c.getString(1),"044" , number, "Estado", false));
            }
        }
        SeleccionAnt = (ArrayList<contact_result> ) getIntent().getSerializableExtra("Selecciones");
        //Init adapter
        adapter2 = new fijo_result_adapter(getApplicationContext(), mFijosList);
        lvFijos.setAdapter(adapter2);
        lvFijos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                fijo_result result = mFijosList.get(i);
                if(result.isSelected()){
                    result.setSelected(false);
                }else{
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
                for (int i = mFijosList.size() - 1; i >= 0; i--){
                    result = mFijosList.get(i);
                    if(result.isSelected()){
                        SeleccionAnt.add(new contact_result(result.getId(), result.getNombre(), "n", "n", result.getNumero(), false));
                        result.setSelected(false);
                    }
                }
                Intent intent = new Intent(getApplicationContext(), resumen.class);
                intent.putExtra("Selecciones", (Serializable) SeleccionAnt);
                startActivity(intent);
            }
        });


    }
}
