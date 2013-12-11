package blippy;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * This will change the scenes.
 * @author Jared
 */
public abstract class SceneChanger
{
   /**
    * instance of the main Blippy class.
    */
   protected Blippy mBlippy;

   /**
    * String of instructions.
    */
   protected String mInstructions;

   /**
    * DOCUMENT ME!
    */
   protected Scene mScene;

   /**
    * DOCUMENT ME!
    */
   protected Group mRoot;

   /**
    * DOCUMENT ME!
    */
   protected BorderPane mPane;

   /**
    * Menu bar.
    */
   protected VBox mMenu;

   /**
    * DOCUMENT ME!
    */
   protected final int Height;

   /**
    * DOCUMENT ME!
    */
   protected final int Length;

   /**
    * Creates a new SceneChanger object.
    *
    * @param pBlippy DOCUMENT ME!
    */
   public SceneChanger(Blippy pBlippy)
   {
      Height = 700;
      Length = 700;
      mBlippy = pBlippy;
      mRoot = new Group();
      mPane = new BorderPane();
      mMenu = new VBox();
      initializeRoot();
      initializeMenu();
   }

   /**
    * Getter for the scene.
    *
    * @return mScene
    */
   public Scene getScene()
   {
      return mScene;
   }
   
   /**
    * Getter for mPane.
    *
    * @return mPane
    */
   public BorderPane getPane()
   {
      return mPane;
   }

   /**
    * Builds the menu bar.
    */
   private void initializeMenu()
   {
      mMenu = new VBox();

      MenuBar menuBar = new MenuBar();

      // --------------------------------------- Menu File
      Menu menuFile = new Menu("File");

      //---------------------------------------- File Items
      Menu newG = new Menu("New");
      MenuItem siBlippy = new MenuItem("Blippy");
      siBlippy.setOnAction(new EventHandler<ActionEvent>()
         {
            public void handle(ActionEvent t)
            {
               mBlippy.setIsSoundTest(false);
               mBlippy.setIsColorBlind(false);
               mBlippy.resetGame();
            }
         });

      //MenuItem siLCurve = new MenuItem("Learning Curve");
      //siLCurve.setOnAction(new EventHandler<ActionEvent>()
         //{
            //public void handle(ActionEvent t)
            //{
             //  System.out.println("HI");
            //}
         //});
      //add all submenus to New
      newG.getItems().addAll(siBlippy);

      MenuItem pause = new MenuItem("Pause");
      pause.setOnAction(new EventHandler<ActionEvent>()
         {
            public void handle(ActionEvent t)
            {
               if (mBlippy.getCurrentScene() == "mGame")
               {
                  mBlippy.pauseGame("~ PAUSED ~");
               }
            }
         });

      MenuItem exit = new MenuItem("Exit");
      exit.setOnAction(new EventHandler<ActionEvent>()
         {
            public void handle(ActionEvent t)
            {
               System.exit(0);
            }
         });

      //add all items to menu
      menuFile.getItems().addAll(newG, pause, exit);

      //---------------------------------------- Menu Options
      Menu menuOption = new Menu("Options");

      //---------------------------------------- Options Items
      final CheckMenuItem colorBlind = new CheckMenuItem("Colorblind");
      colorBlind.setSelected(mBlippy.getIsColorBlind());
      colorBlind.setOnAction(new EventHandler<ActionEvent>()
         {
            public void handle(ActionEvent t)
            {
               mBlippy.setIsColorBlind(! mBlippy.getIsColorBlind());
               colorBlind.setSelected(mBlippy.getIsColorBlind());

               if (mBlippy.getCurrentScene() == "mGame")
               {
                  mBlippy.resetGame();
               }
            }
         });

      final CheckMenuItem siSound = new CheckMenuItem("Sound Test");
      siSound.setSelected(mBlippy.getIsSoundTest());
      siSound.setOnAction(new EventHandler<ActionEvent>()
         {
            public void handle(ActionEvent t)
            {
               mBlippy.setIsSoundTest(! mBlippy.getIsSoundTest());
               siSound.setSelected(mBlippy.getIsSoundTest());

               if (mBlippy.getCurrentScene() == "mGame")
               {
                  mBlippy.resetGame();
               }
            }
         });

      //adds all options to the Options menu
      menuOption.getItems().addAll(colorBlind, siSound);

      //Add all menus to menu bar
      menuBar.getMenus().addAll(menuFile, menuOption);

      mMenu.getChildren().addAll(menuBar);
   }

   /**
    * DOCUMENT ME!
    */
   private void initializeRoot()
   {
   }

   /**
    *
    * @return
    */
   public abstract Scene setup();


}
