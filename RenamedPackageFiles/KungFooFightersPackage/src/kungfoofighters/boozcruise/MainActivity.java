package kungfoofighters.boozcruise;

import kungfoofighters.boozcruise.R;
import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

	//r is a random object that is used to generate the random numbers for the logic test
    Random r = new Random();
    //i1 and i2 hold a ranomd number each. Qnumber is the question number. NumPassed is the number of questions the user has passed. CorrectAnswer
    //is the current solution to the question. NumCorrect is the number of correct answers the user has entered. NumWrong is the number of wrong answers
    //the user has entered.
	public int i1, i2, Qnumber, NumPassed, CorrectAnswer, NumCorrect, NumWrong;
	//MathRunTime is the amount of time the logic test has been running in the foreground. MathStartTime is the time at which the logic test was openend.
	public int MathRunTime = 0, MathStartTime = 0;
	//MathSessionTime this is the maximum duration of the logic test
	public int MathSessionTime = 30;
	//MathRemainingTime holds the amount of time that the user has left to complete the test
	public int MathRemainingTime = MathSessionTime;
	//userAnswer is used to hold the answer that the user inputs for the given question
	public int userAnswer;
	//max1 and max2 store the range of values from which the random numbers are drawn
	public int max1 = 300;
	public int max2 = 100;
	//xpix and ypix store the current screensize of the phone
	public int xpix, ypix;
	//MathScore is the users score. The user gets 15 points for a correct answer, -5 points for wrong answers and 0 points for a passed question. 
	//the users score is not allowed to go below zero.
	public int MathScore = 0, correctMultiplier = 15, wrongMultiplier = -5, passedMultiplier = 0;
	//These objects set up the timing system for the activity.
	Timer MathTimer = null;
	TimerTask MathTask = null;
	
	
	//function to obtain the dimensions of the screen
	private void setPix() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		ypix = metrics.heightPixels;
		xpix = metrics.widthPixels;
	}
	//generate two random integers and increment the question number
    public void generateRandInt() {
    	this.i1 = r.nextInt(this.max1+1);
    	this.i2 = r.nextInt(this.max2+1);
    	this.Qnumber = Qnumber + 1;
    }
    //update the question number field that the user sees
    public void updateQNumberField() {
    	TextView qnumfield = (TextView)findViewById(R.id.question_number);
        qnumfield.setText("Question #" + Integer.toString(this.Qnumber) + ":" );
    }
    //update the question that is displayed to the user
    public void updateQField() {
    	TextView qfield = (TextView)findViewById(R.id.question_field);     	
    	generateRandInt();
    	qfield.setText(Integer.toString(this.i1) + " + " + Integer.toString(this.i2) + " = ?");
    	qfield.setHeight(ypix/6+20);
    }
    //update the number of correct/wrong/passed questions the user has
    public void updateStatsField(String s) {
    	TextView pfield = (TextView)findViewById(R.id.stats_field);
    	pfield.setText("Skipped: " + Integer.toString(NumPassed) + ", " + "Correct: " + Integer.toString(NumCorrect) + ", " + "Wrong: " + Integer.toString(NumWrong));
    }
    //clear the contents of the users editable text field
    public void clearAnswerField() {
    	EditText editText = (EditText)findViewById(R.id.answer_field);
    	editText.setText("");
    }
    //calculate the correct answer for the current question
    public void updateCorrectAnswer() {
    	this.CorrectAnswer = this.i1 + this.i2;
    }
    //tell the user he/she was correct if the answer was correct
    public void updateValidityCorrect() {
    	TextView correctValidity = (TextView)findViewById(R.id.validity);
    	correctValidity.setText("CORRECT!");
    }
    //tell the user he/she was wrong if the answer was wrong
    public void updateValidityWrong() {
    	TextView wrongValidity = (TextView)findViewById(R.id.validity);
    	wrongValidity.setText("Sorry, wrong answer!");
    }
    //tell the user some other message
    public void updateValidityGeneric(String s) {
    	TextView emptyValidity = (TextView)findViewById(R.id.validity);
    	emptyValidity.setText(s);
    }
    //get the answer that the user input
    public int getUserAnswer() {
    	EditText userAnswer = (EditText)findViewById(R.id.answer_field);
    	String userAnswerString = userAnswer.getText().toString();
    	int userAnswerInt = Integer.parseInt(userAnswerString);
    	return userAnswerInt;
    }
    //update the users score
    public void updateScoreField() {
    	TextView scField = (TextView)findViewById(R.id.scoreField);
    	scField.setText("Score : " + Integer.toString(MathScore));
    }
    //update the users remaining time
    public void updateTimeField() {
    	TextView tField = (TextView)findViewById(R.id.timeField);
    	tField.setText("Time left: " + Integer.toString(MathRemainingTime));
    }
    //this is for the beginning of the app - gives the user 5 seconds to get ready
    public void updateGetReadyField(int countdown) {
    	TextView getReadyField = (TextView)findViewById(R.id.getReadyField);
    	getReadyField.setText("Get Ready! " + Integer.toString(countdown));
    	getReadyField.setHeight(ypix/6+15);
    }
    //get rid of the get ready message
    public void hideGetReady() {
    	TextView getReadyField = (TextView)findViewById(R.id.getReadyField);
    	getReadyField.setText("");
    	getReadyField.setBackgroundResource(R.color.transparent);
    }
    //things that need to be done upon opening the application
    
    //This is the function that is called when the application is opened.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //the screensize is obtained
    	setPix();
    	// the number of questions passed, correctly answered and incorrectly answered are set to zero.
    	this.NumPassed = 0; this.NumCorrect = 0; this.NumWrong = 0;
        setContentView(R.layout.activity_main);
        
        //The editable text field for the user to enter answers is set to only accept numbers
        EditText answerField = (EditText)findViewById(R.id.answer_field);
        answerField.setInputType(InputType.TYPE_CLASS_NUMBER);
        
        //The users score is updated
        updateScoreField();
        //The time remaining is updated (should be the maximum time)
        updateTimeField();
        
        //The question to be answered is updated. (This will be hidden until the get ready message is hidden).
        updateQField();
        //for every new question, the correct answer is updated
        updateCorrectAnswer();
        //ever time a new question is generated, the question number is incremented
        updateQNumberField();
        //The users statistics (correct,wrong,passed) are updated
        updateStatsField(Integer.toString(this.MathRemainingTime));
        //Initialize the starting time for the app
        this.MathStartTime = (int) System.currentTimeMillis()/1000;
        //set up a timer to determine how much time the user has left
        MathTimer = new Timer();
        MathTimer.schedule (new TimerTask() {
        	@Override
        	public void run() {
        		TimerMethod();
        	}
        }, 0, 1000);
    }
    
    //This runs the timer on the UI thread
    private void TimerMethod() {
    	this.runOnUiThread(Timer_Tick);
    }
    
    //things to do based on the running clock/timer
    private Runnable Timer_Tick = new Runnable () {
    	public void run() {
    		//update the users score constantly
    		TextView sField = (TextView)findViewById(R.id.scoreField);
    		//score calculated as 15 for correct answers, -5 for wrong answers and 0 for passed questions
    		MathScore = correctMultiplier*NumCorrect + wrongMultiplier*NumWrong + passedMultiplier*NumPassed;
    		SharedData.shared3 = (int) MathScore ;
    		//do not allow the users score to go below zero. Set the text color to red if the users score is zero
    		if (MathScore <= 0) {
    			sField.setTextColor(getResources().getColor(R.color.red));
    			MathScore = 0;
    		}
    		//if the users sccore is not zero, set the text color to black
    		else {
    			sField.setTextColor(getResources().getColor(R.color.black));
    		}
    		//after calculating the score and changing the text color accordingly, display the new sccore
    		updateScoreField();
    		//Need to update the remaining time constantly
    		TextView tField = (TextView)findViewById(R.id.timeField);
    		//calculate the amount of time the logic test has been running
    		MathRunTime = (int) System.currentTimeMillis()/1000 - MathStartTime;
    		//calculate the amount of time the user has left
    		MathRemainingTime = MathSessionTime - MathRunTime + 5;
    		//for the first five seconds after the app is opened, the get ready message is displayed and the question is hidden
    		if (MathRunTime < 5 && MathRemainingTime > 0) {
    			MathRunTime = (int) System.currentTimeMillis()/1000 - MathStartTime;
    			updateGetReadyField(5 - (int)MathRunTime);
    		}
    		//if the first five seconds after the app was opened are over, the get ready message is hidden and the question becomes visible. The time
    		//also starts updating.
    		else if (MathRunTime >= 5 && MathRemainingTime > 0) {
    			hideGetReady();
    			//If the user has more than 10 seconds left, the text color is blacl
    			if (MathRemainingTime > 10) {
    				tField.setTextColor(getResources().getColor(R.color.black));
    				updateTimeField();
    			}
    			//if the user has less than or equal to 10 seconds left, the text color becomes red
    			else {
    				tField.setTextColor(getResources().getColor(R.color.red));
    				updateTimeField();
    			}
    		}
    		//if the remaining time is equal to or less than zero, quit the app
    		else {
    			MainActivity.this.finish();
    		}
    	}
    };
    
    //on resumption of the app, things to be done
    @Override
    protected void onResume(){
    	super.onResume();
    	//update the amount of time the app has been running
    	this.MathRunTime = (int) System.currentTimeMillis()/1000 - this.MathStartTime;
    	this.MathRemainingTime = this.MathSessionTime - this.MathRunTime;
    	//update the users statistics
    	updateStatsField(Integer.toString(MathRemainingTime));
    }
    //what happens when the user presses pass
    public void passQuestion(View view) {    	
    	// Tell user that a question was passed    	
    	this.NumPassed = this.NumPassed + 1;
    	updateStatsField(Integer.toString(this.MathRemainingTime));
    	
    	// Refresh the question in response to the pass button
    	updateQField();
    	updateCorrectAnswer();
    	
    	// Update the question number field
    	updateQNumberField();
    	
    	// Clear the users previous input
    	clearAnswerField();
    	
    }
    
    //check if the user entered the correct/wrong answer
    /** Called when the user clicks the Enter button */
    public void evaluateAnswer(View view) {
    	//string to be displayed if the user hits enter without entering an answer
    	String s = "You did not enter a number!";
    	EditText editText = (EditText)findViewById(R.id.answer_field);
    	String enteredText = editText.getText().toString();
    	//if the user pressed enter without typing anything, display a message to tell the user so
    	if (enteredText.matches("")){
    		updateValidityGeneric(s);
    	}
    	//if the user enters the correct answer ...
    	else {
    		this.userAnswer = getUserAnswer();
    		if (this.userAnswer == this.CorrectAnswer) {
    			//tell the user that their answer was correct
    			updateValidityCorrect();
    			//change the question
    			updateQField();
    			//change the question number
    			updateQNumberField();
    			//update the correct answer for the new question
    			updateCorrectAnswer();
    			//increment the number of correct answers the user has entered
    			this.NumCorrect = this.NumCorrect + 1;
    			//update the users statistics
    			updateStatsField(Integer.toString(this.MathRemainingTime));
    		}
    		//if the user enters the wrong answer ...
    		else if (this.userAnswer != this.CorrectAnswer){
    			//tell the user the answer was wrong
    			updateValidityWrong();
    			//change the question (the user does not get a second try at the same question)
    			updateQField();
    			//update the question number
    			updateQNumberField();
    			//update the correct answer for the new question
    			updateCorrectAnswer();
    			//increment the number of questions this user has answered incorrectly
    			this.NumWrong = this.NumWrong + 1;
    			//update the statistics for the user
    			updateStatsField(Integer.toString(this.MathRemainingTime));
    		}
    		else {
    			//tell the user he/she did not enter anything
    			updateValidityGeneric(s);  
    		}
    	}
    	//}
    	//after the user enters an answer, clear the editable text field to allow for the next input
    	clearAnswerField();
    	
    		
    		
    	
    }
    
    //this function is called when the user presses the menu button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	//add a button called "Return to Main Menu"
    	menu.add("Return to Main Menu");
        return super.onCreateOptionsMenu(menu);
    }
    
    //this function is called if a menu item was clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getTitle() == "Return to Main Menu")
    			//if a menu item named "Return to Main Menu was clicked, the app is finished and the user is returned to the main menu
    		 	MainActivity.this.finish();
   		return super.onOptionsItemSelected(item);     
    }
    
    
}
