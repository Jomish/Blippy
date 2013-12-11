package blippy;

import java.text.DecimalFormat;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.geometry.Insets;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Builder for results scene.  Takes data from gameHelper
 * @author Jared
 */
class Results
   extends SceneChanger
{
   /**
    * Button to go back to start menu.
    */
   private Button mStartBtn;

   /**
    * Table to hold results.
    */
   private ListView mTable;

   /**
    * Data for the table.
    */
   private ObservableList<String> mData;

   /**
    * Creates a new Results object.
    *
    * @param pBlippy
    */
   public Results(Blippy pBlippy)
   {
      super(pBlippy);
   }

   /**
    * Setter the results.
    *
    * @param pData - data to be put in the table.
    */
   public void setResults(List<Double> pData)
   {
      mData = FXCollections.observableArrayList();

      DecimalFormat formater = new DecimalFormat("0.00");
      int i = 0;
      String clickNumber;

      for (Double item : pData)
      {
         i++;

         if (i < 10)
         {
            clickNumber = "# ";
         }
         else
         {
            clickNumber = "#";
         }

         clickNumber += Integer.toString(i);

         mData.add(" Click " + clickNumber + "                  " +
            formater.format(item) + " seconds");
      }
   }

   /**
    * Builds the table.
    */
   private void initializeTable()
   {
      mTable = new ListView(mData);
      mTable.setPrefSize(200, 250);
      mTable.setEditable(false);
   }

   /**
    * Builds the scene with the table and data.
    *
    * @return scene.
    */
   @Override
   public final Scene setup()
   {
      mPane = new BorderPane();

      initializeTable();

      mStartBtn = new Button("to start");
      mStartBtn.setOnAction(new EventHandler<ActionEvent>()
         {
            @Override
            public void handle(ActionEvent event)
            {
               mBlippy.setup("mStart");
            }
         });

      Button resetButton = new Button("Restart Game");
      resetButton.setOnAction(new EventHandler<ActionEvent>()
         {
            @Override
            public void handle(ActionEvent event)
            {
               mBlippy.resetGame();
            }
         });

      mStartBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
      resetButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
      mStartBtn.setMinWidth(Control.USE_PREF_SIZE);
      resetButton.setMinWidth(Control.USE_PREF_SIZE);

      VBox box = new VBox(10);
      box.getChildren().addAll(mStartBtn, resetButton);
      box.setSpacing(10);
      box.setPadding(new Insets(10, 20, 10, 20));

      mPane.setTop(mMenu);
      mPane.setRight(box);

      final VBox vbox = new VBox();
      vbox.setSpacing(5);
      vbox.setPadding(new Insets(10, 0, 0, 10));
      vbox.getChildren().addAll(mTable);

      mPane.setCenter(vbox);
      mScene = new Scene(mPane, Height, Length);
      mScene.getStylesheets()
            .add(Blippy.class.getResource("Style.css").toExternalForm());

      return mScene;
   }
}
