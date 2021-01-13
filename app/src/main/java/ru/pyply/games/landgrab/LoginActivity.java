package ru.pyply.games.landgrab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    public static String email = "max@mail.ru";
    public static String password = "qwerty123";

    public static final byte REQ_REGISTRATION_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onClickBtnLogin(View view) {
        TextView text_view_msg = (TextView) findViewById(R.id.textMessagesLogin);
        EditText text_edit_email = (EditText) findViewById(R.id.editTextEmailLogin);
        EditText text_edit_password = (EditText) findViewById(R.id.editTextPasswordLogin);

        String input_email = text_edit_email.getText().toString();
        String input_password = text_edit_password.getText().toString();

        if (input_email.equals(email) && password.equals(input_password)) {
            text_view_msg.setText(R.string.success_login_msg);
        } else {
            System.out.printf("%s != %s | %s != %s\n", input_email, email, input_password, password);
            Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivityForResult(i, REQ_REGISTRATION_CODE);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_REGISTRATION_CODE:
                switch (resultCode) {
                    case RESULT_OK:
                        EditText text_edit_email = (EditText) findViewById(R.id.editTextEmailLogin);
                        EditText text_edit_password = (EditText) findViewById(R.id.editTextPasswordLogin);

                        // Устанавливаем сразу зареганную почту и очищаем пароль
                        text_edit_email.setText(data.getStringExtra(RegistrationActivity.EXTRA_EMAIL_RES));
                        text_edit_password.setText("");

                        // TODO: После убрать эти 2 ненужные вещи
                        email = data.getStringExtra(RegistrationActivity.EXTRA_EMAIL_RES);
                        password = data.getStringExtra(RegistrationActivity.EXTRA_PASSWORD_RES);
                        break;
                }
        }
    }
}