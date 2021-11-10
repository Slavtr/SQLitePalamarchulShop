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

public class DataBase extends AppCompatActivity implements View.OnClickListener {

    Button btnAdd, btnBack;
    EditText etNome, etProce;

    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base);

        btnAdd = (Button) findViewById(R.id.botonAdd);
        btnAdd.setOnClickListener((View.OnClickListener) this);

        btnBack = (Button) findViewById((R.id.botonBack));
        btnBack.setOnClickListener((View.OnClickListener) this);

        etNome = (EditText) findViewById(R.id.etNome);
        etProce = (EditText) findViewById(R.id.etProce);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        UpdateTable();
    }

    public void UpdateTable() {
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

                Button deleteButton = new Button(this);
                params.weight = 0.5f;
                deleteButton.setOnClickListener(this);
                deleteButton.setLayoutParams(params);
                deleteButton.setText("Удалить запись");
                deleteButton.setId(cursor.getInt(idIndex));

                dbOutputRow.addView(deleteButton);

                dbOutput.addView(dbOutputRow);
            } while (cursor.moveToNext());
        }

        cursor.close();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.botonAdd:
                String name = etNome.getText().toString();
                String price = etProce.getText().toString();
                contentValues = new ContentValues();
                contentValues.put(DBHelper.KEY_NAME, name);
                contentValues.put(DBHelper.KEY_PRICE, price);

                database.insert(DBHelper.TABLE_LIBRARY, null, contentValues);
                etProce.setText(null);
                etNome.setText(null);
                UpdateTable();
                break;
            case R.id.botonBack:
                Intent ontent = new Intent(this, MainActivity.class);
                startActivity(ontent);
                break;
            default:
                View butPr = (View) v.getParent();
                ViewGroup botonPorent = (ViewGroup) butPr.getParent();
                botonPorent.removeView(butPr);
                botonPorent.invalidate();

                database.delete(DBHelper.TABLE_LIBRARY, DBHelper.KEY_ID + " = ?", new String[]{String.valueOf((v.getId()))});
                Cursor cursorUpd = database.query(DBHelper.TABLE_LIBRARY, null, null, null, null, null, null);

                if (cursorUpd.moveToFirst()) {
                    int idIndex = cursorUpd.getColumnIndex(DBHelper.KEY_ID);
                    int nameIndex = cursorUpd.getColumnIndex(DBHelper.KEY_NAME);
                    int priceIndex = cursorUpd.getColumnIndex(DBHelper.KEY_PRICE);
                    int realId = 1;
                    do {
                        if (cursorUpd.getInt(idIndex) > realId) {
                            contentValues.put(DBHelper.KEY_ID, realId);
                            contentValues.put(DBHelper.KEY_NAME, cursorUpd.getString(nameIndex));
                            contentValues.put(DBHelper.KEY_PRICE, cursorUpd.getString(priceIndex));
                            database.replace(DBHelper.TABLE_LIBRARY, null, contentValues);
                        }
                        realId++;
                    } while (cursorUpd.moveToNext());
                    if (cursorUpd.moveToLast() && v.getId() != realId) {
                        database.delete(DBHelper.TABLE_LIBRARY, DBHelper.KEY_ID + " = ?", new String[]{cursorUpd.getString(idIndex)});
                    }
                    UpdateTable();
                }
                break;
        }
    }
}
