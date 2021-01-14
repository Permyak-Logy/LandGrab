package ru.pyply.games.landgrab;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

public class LoginActivity extends Activity {
    public static final String email = "max@mail.ru";
    public static final String password = "qwerty123";

    public static final byte REQ_REGISTRATION_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onClickBtnLogin(View view) {
        TextView text_view_msg = (TextView) findViewById(R.id.textMessagesLogin);
        EditText text_edit_email = (EditText) findViewById(R.id.editTextEmailLogin);
        EditText text_edit_password = (EditText) findViewById(R.id.editTextPasswordLogin);

        String input_email = text_edit_email.getText().toString();
        String input_password = text_edit_password.getText().toString();

        if (input_email.equals(email) && password.equals(input_password)) {
            // При успешном входе мы говорим об успехе
            text_view_msg.setText(R.string.success_login_msg);
            text_view_msg.setTextColor(getColor(R.color.green));
        } else {
            text_view_msg.setText(R.string.fail_login_msg);
            text_view_msg.setTextColor(getColor(R.color.red));

            text_edit_email.setText("");
            text_edit_password.setText("");

            /* Временно заглушим этот вереход на регистрацию

            // Иначе отправляем на регистрацию
            Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivityForResult(i, REQ_REGISTRATION_CODE);
            */
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQ_REGISTRATION_CODE:
                switch (resultCode) {
                    case RESULT_OK:
                        EditText text_edit_email = (EditText) findViewById(R.id.editTextEmailLogin);
                        EditText text_edit_password = (EditText) findViewById(R.id.editTextPasswordLogin);

                        // Устанавливаем сразу зареганную почту и очищаем пароль
                        text_edit_email.setText(data.getStringExtra(RegistrationActivity.EXTRA_EMAIL_RES));
                        text_edit_password.setText("");
                        break;
                }
        }
    }
}