package com.bruynhuis.amnesia;
  
import com.bruynhuis.galago.android.AbstractGameActivity;
 
public class MainActivity extends AbstractGameActivity {

    @Override
    protected void preload() {
        APP_PATH = "za.co.bruynhuis.ld45.MainApplication";
        PLAYSTORE_URL = "https://play.google.com/store/apps/details?id=com.bruynhuis.amnesia";
        MOREAPPS_URL = "https://play.google.com/store/apps/developer?id=bruynhuis";
        
//        usePlayServices = true;
//        
//        splashPicID = R.drawable.splash;
    }

    @Override
    protected void init() {

        
    }

    @Override
    protected void postLoad() {

        
    }
     
}
