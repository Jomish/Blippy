package blippy;

import java.util.*;
import javafx.animation.*;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * This will control the game.  It has a timer that will talk
 * to the game class to switch the circle, as well as send results
 * to the results class.
 * 
 * @author Jared
 */
public class GameHelper
{
    /**
    * Instance of the Blippy class being used.
    */
    private final Blippy mBlippy;
    
    /**
    * Instance of game interface.
    */
    private final Game mGame;
    
    /**
    * The reaction times in raw form.
    */
    private double mResults[];
    
    /**
    * The reaction time to two decimal places.
    */
    private List<Double> mFormatedResults;

    /**
    * DOCUMENT ME!
    */
    private long mStartTime;
    
    /**
    * DOCUMENT ME!
    */
    private long mEndTime;

    /**
    * Number of trials done.
    */
    private final int mGameIterations;

    /**
    * Value to determine if the circle was clicked.
    */
    private Boolean isClicked;
    
    /**
    * DOCUMENT ME!
    */
    private List<Long> mTimeSnapshots;
    
    /**
    * DOCUMENT ME!
    */
    private EventHandler<ActionEvent> mOnFinished;
    
    /**
    * DOCUMENT ME!
    */
    private int mCountChanges;
    
    /**
    * Time to wait between trials.
    */
    private static final int WAIT_PERIOD = 3000;

    /**
    * DOCUMENT ME!
    */
    private Timeline mTimeline;
    
    /**
    * DOCUMENT ME!
    */
    private Timeline clickTimer;
    
    /**
     * The error mp3 file.
     */
    private final Media error;

    /**
     * MediaPlayer to play error tone.
     */
    private MediaPlayer mp;

    /**
     * Constructor for gameHelper.
     * @param pBlippy - instance of main Blippy class
     * @param pGame   - instance of game interface.
     */
    public GameHelper(Blippy pBlippy, Game pGame)
    {
        mBlippy = pBlippy;
        mGame = pGame;
        mGameIterations = 7;
        mCountChanges = 0;
        mStartTime = 0;
        mEndTime = 0;
        mResults = new double[mGameIterations];
        mTimeSnapshots = new ArrayList<>();
        error = new Media(getClass().getResource("error.mp3").toString());
        mp = new MediaPlayer(error);
        mOnFinished = new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent e)
            {
                mGame.setIsReadyToBeClicked(true);
                mGame.swapCircle();
                mStartTime = System.nanoTime();
            }
        };
    }

    /**
     * Setter for isClicked.
     * @param pBool
     */
    public void setIsClicked(Boolean pBool)
    {
        isClicked = pBool;
    }

    /**
     * Getter for isClicked.
     * @return
     */
    public Boolean getIsClicked()
    {
        return isClicked;
    }

    /**
     * Getter for mFormatedResults
     * 
     * @return mFormatedResults
     */
    public List<Double> getFormatedResults()
    {
        return mFormatedResults;
    }

    /**
     *
     * @return
     */
    public boolean readFile()
    {
        return true;
    }

    /**
     *
     * @return
     */
    public boolean writeFile()
    {
        return true;
    }

    /**
     * Stops the time.
     */
    public void killTimeline()
    {
        mTimeline.stop();
        mTimeline = null;
    }

    /**
     * Pauses the game. First, it resets the circle to
     * mStart, 
     */
    public synchronized void pauseGame(String pMessage)
    {
        // if they were supposed to click,
        // reset the circle so the timer resets
        if( !mGame.getIsStartCircle() )
        {
            mGame.swapCircle();
        }

        // resets the mTimeline
        mTimeline.stop();

        final Stage errorWindow = new Stage(StageStyle.UNDECORATED);
        BorderPane pane = new BorderPane();
        Button btn = new Button("Resume Game");
        Text message = new Text(10, 10, pMessage);
        VBox box = new VBox();

        btn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                mTimeline.playFromStart();
                errorWindow.close();
            }
        });

        box.getChildren().addAll(message, btn);

        pane.setTop(message);
        pane.setCenter(btn);
        BorderPane.setAlignment(message, Pos.CENTER);
        errorWindow.setScene(new Scene(pane, 300, 150));

        errorWindow.show();
    }

    /**
     * synchronized because the Timeline.stop() method is not
     */
    public synchronized void game()
    {
        // The handler for the circle this is when they click
        EventHandler handler = new EventHandler<Event>()
        {
            @Override
            public void handle(Event e)
            {
                // Only allow if the game is ready
                if( mGame.isReadyToBeClicked() )
                {
                    // add time to the list (start timer)
                    mEndTime = System.nanoTime();
                    mResults[mCountChanges - 1]
                    = ((double) mEndTime - mStartTime) / 1000000000;
                    mStartTime = 0;
                    mEndTime = 0;
                    
                    // Set up handler
                    mGame.setIsReadyToBeClicked(false);
                    mGame.swapCircle();
                    
                    // ie. go to game
                    doRandomCircleChange();
                }
                else
                {
                    mp = new MediaPlayer(error);
                    mp.play();

                    pauseGame("Don't click before it changes dummy!");
                }
            }
        };

        mGame.getPane().addEventHandler(MouseEvent.MOUSE_PRESSED, handler);
        mGame.getPane().addEventHandler(KeyEvent.KEY_PRESSED, handler);
        mGame.getPane().requestFocus();

        // Start it off
        doRandomCircleChange();
    }

    /**
     * This will change the circle to mFinal after a random time
     * period.
     */
    private void doRandomCircleChange()
    {
        // Go through 7 times
        if( mCountChanges++ >= mGameIterations )
        {
            // Send captured times to results
            mFormatedResults = new ArrayList<>();
            
            for( double item : mResults )
            {
                mFormatedResults.add(item);
            }

            mBlippy.generateResults();

        }
        else
        {
            // Timer
            int x = new Random().nextInt(WAIT_PERIOD) + 2000;
            Duration duration = Duration.millis(x);
            KeyFrame keyFrame = new KeyFrame(duration, mOnFinished,
                                             (KeyValue[]) null);
            mTimeline = new Timeline();
            mTimeline.getKeyFrames().add(keyFrame);
            mTimeline.play();
        }
    }
}

