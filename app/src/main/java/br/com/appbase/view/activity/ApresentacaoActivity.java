package br.com.appbase.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import br.com.appbase.R;

public class ApresentacaoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apresentacao);

        ImageView imgSplash = findViewById(R.id.img_splash);

        Animation splashAnimation = AnimationUtils.loadAnimation(this, R.anim.transition_splash);
        imgSplash.startAnimation(splashAnimation);

        final Intent intent = new Intent(this, MainActivity.class);

        Thread timer = new Thread() {
            public void run() {

                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.getStackTrace();
                    e.getStackTrace();
                } finally {
                    startActivity(intent);
                    finish();
                }

            }

        };

        timer.start();
    }
}
