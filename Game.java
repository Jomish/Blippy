package blippy;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * This class will set up the game interface.  It contains 2
 * circles to switch to and from as directed to by the gameHelper
 * class. This is also where colorBlind and soundTest are handled.
 * 
 * @author Joshua
 */
class Game
   extends SceneChanger
{
   /**
    * DOCUMENT ME!
    */
   private Button mStartBtn;

   /**
    * The initial circle object. 
    * Blippy - red 
    * ColorBlind - black outline
    * SoundTest - invisible
    */
   private final Circle mStart;

   /**
    * The final circle object. 
    * Blippy - green 
    * ColorBlind - black
    * SoundTest - invisible
    */
   private final Circle mFinal;

   /**
    * Instance of the game object.
    */
   private final Game mInstance;

   /**
    * A value to determine if the circle is ready to change.
    */
   private Boolean isReady;

   /**
    * The beep mp3 file.
    */
   private final Media beep;

   /**
    * Will play the beep mp3.
    */
   private MediaPlayer mp;

   /**
    * A value to determine if circle is not ready to be clicked.
    */
   private Boolean isStartCircle;

   /**
    * Creates a new Game object.
    *
    * @param pBlippy - An object of the main game.

    */
   public Game(Blippy pBlippy)
   {
      super(pBlippy);
      mStart = new Circle();
      mFinal = new Circle();

      mInstance = this;

      isReady = false;

      beep = new Media(getClass().getResource("beep.mp3").toString());
      mp = new MediaPlayer(beep);
      isStartCircle = true;
   }

   /**
    * Setter for isReady
    * 
    * @param pReady
    */
   public void setIsReadyToBeClicked(boolean pReady)
   {
      isReady = pReady;
   }

   /**
    * Getter for isReady
    * @return isReady
    */
   public Boolean isReadyToBeClicked()
   {
      return isReady;
   }

   /**
    *
    * @return
    */
   public Game getInstance()
   {
      return mInstance;
   }

   /**
    * Getter for isStartCircle
    *
    * @return
    */
   public Boolean getIsStartCircle()
   {
      return isStartCircle;
   }

   /**
    * Will swap the circle from mStart to mFinal or from
    * mFinal to mStart.
    */
   public void swapCircle()
   {
      if ((mPane.getCenter() == mStart) && (mBlippy.getIsSoundTest() == true))
      {
         mp = new MediaPlayer(beep);
         mp.play();
         mPane.setCenter(mFinal);
      }
      else if (mPane.getCenter() == mStart)
      {
         mPane.setCenter(mFinal);
      }
      else
      {
         mPane.setCenter(mStart);
      }

      isStartCircle = ! isStartCircle;
   }

   /**
    * This will build the scene for sceneChanger, formatting
    * mStart and mFinal for all conditions.
    *
    * @return mScene - the scene of the game interface.
    */
   @Override
   public final Scene setup()
   {
      mPane = new BorderPane();
      
      mStart.setCenterX(100.0f);
      mStart.setCenterY(100.0f);
      mStart.setRadius(200.0f);

      mFinal.setCenterX(100.0f);
      mFinal.setCenterY(100.0f);
      mFinal.setRadius(200.0f);

      //set color of mStart and mFinal if Blippy
      mStart.setStroke(Color.BLACK);
      mStart.setFill(Color.RED);
      mFinal.setStroke(Color.BLACK);
      mFinal.setFill(Color.GREEN);
      mStart.setVisible(true);
      mFinal.setVisible(true);

      //set color of mStart and mFinal if colorBlind
      if (mBlippy.getIsColorBlind() == true)
      {
         mStart.setFill(Color.WHITE);
         mFinal.setFill(Color.BLACK);
      }

      //set color of mStart and mFinal if SoundTest
      if (mBlippy.getIsSoundTest() == true)
      {
         mStart.setVisible(false);
         mFinal.setVisible(false);
      }

      mPane.setCenter(mStart);
      mPane.setTop(mMenu);
      mScene = new Scene(mPane, Height, Length);

      return mScene;
   }
}
