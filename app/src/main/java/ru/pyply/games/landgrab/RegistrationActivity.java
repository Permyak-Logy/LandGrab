package ru.pyply.games.landgrab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class RegistrationActivity extends AppCompatActivity {
    public static final String EXTRA_EMAIL_RES = "ru.pyply.games.landgrab.EXTRA_EMAIL_RES";
    public static final String EXTRA_PASSWORD_RES = "ru.pyply.games.landgrab.EXTRA_PASSWORD_RES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        TextView text_view_msg_reg = (TextView) findViewById(R.id.textMessagesRegistration);
        text_view_msg_reg.setText(R.string.badPasswordRegPlease);
    }

    public void onClickBtnRegister(View view) {
        TextView text_view_msg_reg = (TextView) findViewById(R.id.textMessagesRegistration);

        EditText edit_text_email = (EditText) findViewById(R.id.editTextEmailRegistration);
        String email = edit_text_email.getText().toString();

        EditText edit_text_password = (EditText) findViewById(R.id.editTextPasswordRegistration);
        String password = edit_text_password.getText().toString();

        EditText edit_text_repeat_password = (EditText) findViewById(R.id.editTextPasswordRepeatRegistration);
        String repeat_password = edit_text_repeat_password.getText().toString();

        boolean has_errors = true; // Если после проверок мы никуда не зайдём то он станет false

        // Проверки
        if (email.isEmpty()) { // На невведённую почту
            text_view_msg_reg.setText(R.string.email_is_empty);
        } else if (password.isEmpty() || repeat_password.isEmpty()) { // На невведёные пароли
            text_view_msg_reg.setText(R.string.passwords_is_empty);
        } else if (!password.equals(repeat_password)) { // На несовпадающие пароли
            text_view_msg_reg.setText(R.string.passwords_is_not_equals);
        } else { // и если всё хорошо то всё без ошибок
            has_errors = false;
        }

        if (has_errors) { // При любой ошибке очищаем поля с паролями
            edit_text_password.setText("");
            edit_text_repeat_password.setText("");

        } else { // При успехе возвращаемся на страницу входа
            Intent i = new Intent();
            i.putExtra(EXTRA_EMAIL_RES, email);

            // TODO: Избавится после от отправки пароля в активность
            i.putExtra(EXTRA_PASSWORD_RES, password);

            setResult(RESULT_OK, i);
            finish();
        }

    }
}