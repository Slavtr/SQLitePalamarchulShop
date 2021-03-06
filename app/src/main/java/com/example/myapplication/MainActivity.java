package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnBuy, btnDB;
    EditText etSommo;

    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;
    int Sommo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBuy = (Button) findViewById(R.id.botonBuy);
        btnBuy.setOnClickListener((View.OnClickListener) this);

        btnDB = (Button) findViewById((R.id.botonDB));
        btnDB.setOnClickListener((View.OnClickListener) this);

        etSommo = (EditText) findViewById(R.id.etSommo);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        UpdateTable();
    }

    public void UpdateTable(){
        Cursor cursor = database.query(DBHelper.TABLE_LIBRARY, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int priceIndex = cursor.getColumnIndex(DBHelper.KEY_PRICE);
            TableLayout dbOutput = findViewById(R.id.dbOutput);
            dbOutput.removeAllViews();
            do {
                TableRow dbOutputRow = new TableRow(this);
                dbOutputRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

                TextView outputId = new TextView(this);
                params.weight = 1.0f;
                outputId.setLayoutParams(params);
                outputId.setText(cursor.getString(idIndex));
                dbOutputRow.addView(outputId);

                TextView outputName = new TextView(this);
                params.weight = 2.0f;
                outputName.setLayoutParams(params);
                outputName.setText(cursor.getString(nameIndex));
                dbOutputRow.addView(outputName);

                TextView outputProce = new TextView(this);
                params.weight = 2.0f;
                outputProce.setLayoutParams(params);
                outputProce.setText(cursor.getString(priceIndex));
                dbOutputRow.addView(outputProce);

                Button cartBoton = new Button(this);
                params.weight = 0.5f;
                cartBoton.setOnClickListener(this);
                cartBoton.setLayoutParams(params);
                cartBoton.setText("?? ??????????????");
                cartBoton.setId(cursor.getInt(idIndex));

                dbOutputRow.addView(cartBoton);

                dbOutput.addView(dbOutputRow);
            } while (cursor.moveToNext());
        }

        cursor.close();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.botonBuy:
                if(etSommo.getText().toString() == "Name" || etSommo.getText().toString() == null){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "???? ???????????? ???? ????????????.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    etSommo.setText(null);
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "???? ???????????????? ???? " + String.valueOf(Sommo), Toast.LENGTH_SHORT);
                    toast.show();
                }
                Sommo = 0;
                break;
            case R.id.botonDB:
                Intent ontont = new Intent(this, DataBase.class);
                startActivity(ontont);
                break;
            default:
                View butPr = (View) v.getParent();
                ViewGroup botonPorent = (ViewGroup) butPr.getParent();
                //botonPorent.removeView(butPr);
                botonPorent.invalidate();
                Sommo += Integer.valueOf(((TextView)((TableRow)v.getParent()).getChildAt(2)).getText().toString());
                etSommo.setText(String.valueOf(Sommo));

                /*database.delete(DBHelper.TABLE_LIBRARY, DBHelper.KEY_ID + " = ?", new String[]{String.valueOf((v.getId()))});
                Cursor cursorUpd = database.query(DBHelper.TABLE_LIBRARY, null, null, null, null, null, null);

                if (cursorUpd.moveToFirst()) {
                    int idIndex = cursorUpd.getColumnIndex(DBHelper.KEY_ID);
                    int nameIndex = cursorUpd.getColumnIndex(DBHelper.KEY_NAME);
                    int priceIndex = cursorUpd.getColumnIndex(DBHelper.KEY_PRICE);
                    int realId = 1;
                    do{
                        if(cursorUpd.getInt(idIndex)>realId){
                            contentValues.put(DBHelper.KEY_ID, realId);
                            contentValues.put(DBHelper.KEY_NAME, cursorUpd.getString(nameIndex));
                            contentValues.put(DBHelper.KEY_PRICE, cursorUpd.getString(priceIndex));
                            database.replace(DBHelper.TABLE_LIBRARY, null, contentValues);
                        }
                        realId++;
                    }while (cursorUpd.moveToNext());
                    if (cursorUpd.moveToLast() && v.getId()!=realId){
                        database.delete(DBHelper.TABLE_LIBRARY, DBHelper.KEY_ID + " = ?", new String[]{cursorUpd.getString(idIndex)});
                    }
                    UpdateTable();
                }*/
                break;
        }
    }
}