package com.example.myapplication;

import static android.icu.text.MessagePattern.ArgType.SELECT;

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
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Auth extends AppCompatActivity implements View.OnClickListener {

    Button btnRegistor, btnLogin;
    EditText etLogin, etPossword;

    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        btnRegistor = findViewById(R.id.botonRegister);
        btnRegistor.setOnClickListener((View.OnClickListener) this);

        btnLogin = findViewById(R.id.botonLogin);
        btnLogin.setOnClickListener((View.OnClickListener) this);

        etLogin = findViewById(R.id.etLogin);
        etPossword = findViewById(R.id.etPossword);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.botonRegister:
                if(etPossword.getText().toString() != "" && etLogin.getText().toString() != ""){
                    String name = etPossword.getText().toString();
                    String price = etLogin.getText().toString();
                    contentValues = new ContentValues();
                    contentValues.put(DBHelper.U_PASSWORD, name);
                    contentValues.put(DBHelper.U_LOGIN, price);

                    database.insert(DBHelper.TABLE_USERS, null, contentValues);
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Вы не ввели данные", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
            case R.id.botonLogin:
                Cursor cursorUpd = database.query(DBHelper.TABLE_USERS, new String[]{DBHelper.U_ID},DBHelper.U_LOGIN + " == ? and " + DBHelper.U_PASSWORD + " == ?"
                        , new String[]{etLogin.getText().toString(), etPossword.getText().toString()}, null, null, null);

                if (cursorUpd.getCount() > 0) {
                    cursorUpd.close();
                    Intent ontont = new Intent(this, MainActivity.class);
                    startActivity(ontont);
                }
                break;
            default:
                break;
        }
    }
}