//Activity A is the main Menu. It contains the buttons to go to each of the other activity
//This is startup screen and specified by the action keyword in the Android Manifest XML file
//Note that this was HEAVILY modified from the tutorial code

/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//

package kungfoofighters.boozcruise;

import android.app.Activity;

import kungfoofighters.boozcruise.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.android.lifecycle.util.StatusTracker;
import com.example.android.lifecycle.util.Utils;

/**
 * Example Activity to demonstrate the lifecycle callback methods.
 */


public class MainMenu extends Activity {
	
       
    
    
    //Creates the layout from activity_a xml with the buttons..
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
        //mActivityName = getString(R.string.activity_a);
        //mStatusTracker.setStatus(mActivityName, getString(R.string.on_create));
        
    }

    
    
    @Override
    protected void onStart() {
        super.onStart();
       
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    ;
    }

    @Override
    protected void onResume() {
        super.onResume();
       
    }

    @Override
    protected void onPause() {
        super.onPause();
    
    }

    @Override
    protected void onStop() {
        super.onStop();
        //mStatusTracker.setStatus(mActivityName, getString(R.string.on_stop));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // mStatusTracker.setStatus(mActivityName, getString(R.string.on_destroy));
       
    }

    //The following functions are called from pressing the button on the menu
    //The xml links a button press to the intent of moving to another page
    public void startDialog(View v) {
        Intent intent = new Intent(MainMenu.this, Instructions.class);
        startActivity(intent);
    }

    public void startScoreDisplay(View v) {
        Intent intent = new Intent(MainMenu.this, ScoreDisplay.class);
        startActivity(intent);
    }
    
    public void startActivityB(View v) {
        Intent intent = new Intent(MainMenu.this, BalanceBall.class);
        startActivity(intent);
    }

    public void startActivityC(View v) {
        Intent intent = new Intent(MainMenu.this, ReactionTime.class);
        startActivity(intent);
    }
    
 
   

    public void startMainActivity(View v){
    	Intent intent = new Intent(MainMenu.this, MainActivity.class);
    	startActivity(intent);
    }
    
    //Thises closes Activity A at the exit
    public void finishActivityA(View v) {
        MainMenu.this.finish();
        SharedData.shared3 =0;
        SharedData.shared2 =0;
        SharedData.shared1 =0;
    }
    
   
    

    
    
}
