package com.theleafapps.pro.feathernotes.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.theleafapps.pro.feathernotes.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.logo_medium)
                .setDescription("Feather Notes\n\nFeel the lightness in the your note taking experience with feather notes.\n" +
                        "\n" +
                        "Keep your notes safe with passcode protection to open the app\n")
                .addItem(new Element().setTitle("Version 1.0"))
                .addGroup("Connect with us")
                .addEmail("getintouch@theleafapps.com")
                .addWebsite("http://theleafapps.com/")
                .addFacebook("theleafapps")
                .addTwitter("theleafapps")
                .addPlayStore("com.theleafapps.pro")
                .addInstagram("theleafapps")
                .create();

        setContentView(aboutPage);
    }
}
