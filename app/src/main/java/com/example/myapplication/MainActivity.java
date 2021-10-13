package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAdd, btnClear;
    EditText etName, etEmail, etBookName, etBookAuthor, etBookId;

    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener((View.OnClickListener) this);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener((View.OnClickListener) this);

        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etBookId = (EditText) findViewById(R.id.etBookId);
        etBookName = (EditText) findViewById(R.id.etBookName);
        etBookAuthor = (EditText) findViewById(R.id.etBookAuthor);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        UpdateTable();
    }

    public void UpdateTable(){
        Cursor cursor = database.query(DBHelper.TABLE_LIBRARY, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int emailIndex = cursor.getColumnIndex(DBHelper.KEY_MAIL);
            int bookIdIndex = cursor.getColumnIndex(DBHelper.KEY_BOOK_ID);
            int bookNameIndex = cursor.getColumnIndex(DBHelper.KEY_BOOK_NAME);
            int bookAuthorIndex = cursor.getColumnIndex(DBHelper.KEY_BOOK_AUTHOR);
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

                TextView outputMail = new TextView(this);
                params.weight = 2.0f;
                outputMail.setLayoutParams(params);
                outputMail.setText(cursor.getString(emailIndex));
                dbOutputRow.addView(outputMail);

                TextView outputBookIdIndex = new TextView(this);
                params.weight = 2.0f;
                outputMail.setLayoutParams(params);
                outputMail.setText(cursor.getString(bookIdIndex));
                dbOutputRow.addView(outputBookIdIndex);

                TextView outputBookNameIndex = new TextView(this);
                params.weight = 2.0f;
                outputMail.setLayoutParams(params);
                outputMail.setText(cursor.getString(bookNameIndex));
                dbOutputRow.addView(outputBookNameIndex);

                TextView outputBookAuthorIndex = new TextView(this);
                params.weight = 2.0f;
                outputMail.setLayoutParams(params);
                outputMail.setText(cursor.getString(bookAuthorIndex));
                dbOutputRow.addView(outputBookAuthorIndex);

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

            case R.id.btnAdd:
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String bID = etBookId.getText().toString();
                String bName = etBookName.getText().toString();
                String bAuthor = etBookAuthor.getText().toString();
                contentValues = new ContentValues();
                contentValues.put(DBHelper.KEY_NAME, name);
                contentValues.put(DBHelper.KEY_MAIL, email);
                contentValues.put(DBHelper.KEY_BOOK_ID, bID);
                contentValues.put(DBHelper.KEY_BOOK_NAME, bName);
                contentValues.put(DBHelper.KEY_BOOK_AUTHOR, bAuthor);

                database.insert(DBHelper.TABLE_LIBRARY, null, contentValues);
                etEmail.setText(null);
                etName.setText(null);
                etBookAuthor.setText(null);
                etBookName.setText(null);
                etBookId.setText(null);
                UpdateTable();
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
                    int emailIndex = cursorUpd.getColumnIndex(DBHelper.KEY_MAIL);
                    int bookIdIndex = cursorUpd.getColumnIndex(DBHelper.KEY_BOOK_ID);
                    int bookNameIndex = cursorUpd.getColumnIndex(DBHelper.KEY_BOOK_NAME);
                    int bookAuthorIndex = cursorUpd.getColumnIndex(DBHelper.KEY_BOOK_AUTHOR);
                    int realId = 1;
                    do{
                        if(cursorUpd.getInt(idIndex)>realId){
                            contentValues.put(DBHelper.KEY_ID, realId);
                            contentValues.put(DBHelper.KEY_NAME, cursorUpd.getString(nameIndex));
                            contentValues.put(DBHelper.KEY_MAIL, cursorUpd.getString(emailIndex));
                            contentValues.put(DBHelper.KEY_BOOK_ID, cursorUpd.getString(bookIdIndex));
                            contentValues.put(DBHelper.KEY_BOOK_NAME, cursorUpd.getString(bookNameIndex));
                            contentValues.put(DBHelper.KEY_BOOK_AUTHOR, cursorUpd.getString(bookAuthorIndex));
                            database.replace(DBHelper.TABLE_LIBRARY, null, contentValues);
                        }
                        realId++;
                    }while (cursorUpd.moveToNext());
                    if (cursorUpd.moveToLast() && v.getId()!=realId){
                        database.delete(DBHelper.TABLE_LIBRARY, DBHelper.KEY_ID + " = ?", new String[]{cursorUpd.getString(idIndex)});
                    }
                    UpdateTable();
                }
                break;
        }
        dbHelper.close();
    }
}