package blippy;

import java.util.HashMap;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This is the main class for the Blippy program,
 * it will initialize everything required for the
 * program to run.
 * 
 * @author Jared
 */
public class Blippy extends Application
{
    public static String screen1ID = "mStart";
    //public static String screen1File = "StartMenuFXML.fxml";
    public static String screen2ID = "mResults";
    //public static String screen2File = "TableFXML.fxml";

    private Stage mStage;

    private static Blippy mInstance;
    private final Start mStart;
    private final Game mGame;
    private final GameSetup mGameSetup;
    private GameHelper mGameHelper; // not final because it resets
    private final Results mResults;
    private String currentScene;

    private Boolean isColorBlind;
    private Boolean isSoundTest;

    private final HashMap<String, Scene> mScenes;

    /**
     * Getter for currentScene.
     * @return mGameSetup
     */
    public String getCurrentScene()
    {
        return currentScene;
    }
    
    /**
     * Constructor for Blippy.
     */
    public Blippy()
    {
        super();

        mInstance = this;
        isColorBlind = false;
        isSoundTest = false;

        mStart = new Start(this);
        mGame = new Game(this);
        mGameSetup = new GameSetup(this);
        mResults = new Results(this);
        mGameHelper = new GameHelper(this, mGame);
        mScenes = new HashMap<>();

    }

/////////////////////////////////////////////////////////// Getters
    /**
     * Getter for mStart.
     * 
     * @return mStart
     */
    public Start getStart()
    {
        return mStart;
    }

    /**
     * Getter for mGameSetup.
     * 
     * @return mGameSetup
     */
    public GameSetup getGameSetup()
    {
        return mGameSetup;
    }
    
    /**
     * Getter for mInstance.
     * 
     * @return mInstance
     */
    public static Blippy getInstance()
    {
        return mInstance;
    }

    /**
     * Getter for isColorBlind.
     *
     * @return isColorBlind
     */
    public Boolean getIsColorBlind()
    {
        return isColorBlind;
    }

    /**
     * Getter for isSoundTest.
     *
     * @return isSoundTest
     */
    public Boolean getIsSoundTest()
    {
        return isSoundTest;
    }

    /**
     * Getter for getFormatedResults.
     *
     * @return mGameHelper.getFormatedResults
     */
    public List<Double> getFormatedResults()
    {
        return mGameHelper.getFormatedResults();
    }

/////////////////////////////////////////////////////////// Setters
    /**
     * Setter for isColorBlind
     *
     * @param pIsColorBlind
     */
    public void setIsColorBlind(Boolean pIsColorBlind)
    {
        isColorBlind = pIsColorBlind;
    }

    /**
     * Setter for isSoundTest
     * 
     * @param pIsSoundTest
     */
    public void setIsSoundTest(Boolean pIsSoundTest)
    {
        isSoundTest = pIsSoundTest;
    }

////////////////////////////////////////////////////////// Main Functions
    /**
     * Calls pauseGame in mGameHelper
     *
     * @param pMessage
     */
    public void pauseGame(String pMessage)
    {
        mGameHelper.pauseGame(pMessage);
    }

    /**
     * Resets the game and resets the timer.
     */
    public void resetGame()
    {
        // Overwrites the scene in the hashmap
        addScene("mGame", mGame.setup());

        // Reset the game
        mGameHelper.killTimeline();
        mGameHelper = new GameHelper(this, mGame);
        setup("mGame");
    }

    /**
     * Builds the results screen.
     */
    public void generateResults()
    {
        mResults.setResults(mGameHelper.getFormatedResults());
        addScene("mResults", mResults.setup());
        setup("mResults");
    }

    /**
     *
     */
    private void initializeScenes()
    {
        // sets up the game screen
        addScene("mGame", mGame.setup());
        addScene("mStart", mStart.setup());
    }

    /**
     * Adds a scene.
     * @param pName - 
     * @param pScene
     */
    private void addScene(String pName, Scene pScene)
    {
        mScenes.put(pName, pScene);
    }

    /**
     * Builds all instances of all objects.
     * @param pName
     */
    public void setup(String pName)
    {
        Scene scene = mScenes.get(pName);
        mStage.setScene(scene);
        
        currentScene = pName;

        if( pName == "mGame" )
        {
            Platform.runLater(new Runnable()
            {
                @Override
                public void run()
                {
                    mGameHelper.game();
                }
            });
        }
    }

    /**
     * Start is the main start off point for blippy. It gets the stage, and
     * prepares all the other classes scenes. It grabs and displays the start
     * scene, and from there the user controls where it goes.
     *
     * @param primaryStage
     * @throws java.lang.InterruptedException
     */
    @Override
    public void start(Stage primaryStage) throws InterruptedException
    {
        mStage = primaryStage;

        initializeScenes();

        setup(screen1ID);

        mStage.setTitle("Blippy");
        mStage.show();
    }

    /**
     * The main() method is ignored in JavaFX applications.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
}
