import javafx.application.*;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import java.nio.ByteBuffer;
import java.util.*;
import cam.Frames;

public class JavaFXApp extends Application {

	private static final int FRAME_WIDTH  = 640;
	 private static final int FRAME_HEIGHT = 480; 
    

    GraphicsContext gc;
    Canvas canvas;
    byte buffer[];  
    PixelWriter pixelWriter; 
    PixelFormat<ByteBuffer> pixelFormat;
    Frames frames;    
    
    Stage stage;
    int zoom=1;
    public static void main(String[] args) {
    	// System.loadLibrary("frames");
         launch(args);
	        }

//    public void start(Stage primaryStage) 
//    {
//     int result;
//     
//     result = open_shm("/frames");      
//
//
//     canvas = new Canvas(650, 490);
//     gc = canvas.getGraphicsContext2D();
//
//    }
//    
@Override
 public void start(Stage primaryStage) {
	
	int result;
    
    frames = new Frames();
    result = frames.open_shm("/frames"); 

	primaryStage.setTitle("Controller");
  
  stage = primaryStage;
  



  Menu menu1 = new Menu("File");
  
  final Stage secondaryStage = new Stage();  
  ImageView iv1 = new ImageView();
//microscop
 
  

  
  MenuItem menuItem2 = new MenuItem("Exit");
  
  menuItem2.setOnAction(e -> {
                              System.out.println("Exit Selected");
                              
                              exit_dialog();

                             });
  
  menu1.getItems().add(menuItem2);

  
  //btn_off_off.setOnAction(this::play);
  MenuBar menuBar = new MenuBar();
 
  
  menuBar.getMenus().add(menu1);
  
  
  
  VBox vBox = new VBox(menuBar);
  
  // Przyciski On oraz Off
  Button btn_on = new Button();
  Button btn_off = new Button();
  btn_off.setDisable(true);
  
  btn_on.setText("On");
  btn_on.setMaxWidth(80);
  btn_on.setOnAction(e->{
	
	 final Screen secondaryScreen = Screen.getPrimary();
	 configureAndShowStage("Microscop", secondaryStage, secondaryScreen,iv1); 
	 btn_on.setDisable(true);
	 btn_off.setDisable(false);
  });
  
  btn_off.setText("Off");
  btn_off.setMaxWidth(80);
  btn_off.setOnAction( e->{
	  
	  off(btn_on, btn_off, secondaryStage);
  });
  
  secondaryStage.setOnCloseRequest(e->{
	  
	  off(btn_on, btn_off, secondaryStage);
  });
  
  HBox hBox_buttons = new HBox();
  HBox.setHgrow(btn_on, Priority.ALWAYS);
  HBox.setHgrow(btn_off, Priority.ALWAYS);
  hBox_buttons.getChildren().add(btn_on);
  hBox_buttons.getChildren().add(btn_off);
  HBox.setMargin(btn_on, new Insets(20,0,0,30));
  HBox.setMargin(btn_off, new Insets(20,0,0,30));
  
  vBox.getChildren().add(hBox_buttons);
  
  // Colorblind mode
  
  RadioButton cb_btn  = new RadioButton();
  cb_btn.setText("Colorblind mode");
  VBox.setMargin(cb_btn, new Insets(30,0,0,30));
  cb_btn.setOnAction( e-> { colorblind_mode(iv1);
	  
	  
  });
  
  //cb_btn,setOnAction
  vBox.getChildren().add(cb_btn);

  // Size manipulating
  Label size_label = new Label("Microscop window size:");
  size_label.setFont(new Font(15));
  size_label.setStyle("-fx-font-weight: bold");
  vBox.getChildren().add(size_label);
  VBox.setMargin(size_label, new Insets(30,0,0,30));
  
  HBox hBox_size = new HBox();
  TextField width = new TextField();
  TextField height = new TextField();
  Label width_label = new Label("Width:");
  Label height_label = new Label("Height:");

  
  hBox_size.getChildren().add(width_label);  
  hBox_size.getChildren().add(width);
  hBox_size.getChildren().add(height_label);  
  hBox_size.getChildren().add(height);
  hBox_size.setSpacing(8.0d);

  vBox.getChildren().add(hBox_size);
  VBox.setMargin(hBox_size, new Insets(10,0,0,30));

//Pzyciski Change
  
  Button btn_change = new Button();
  btn_change.setText("Change");
  btn_change.setMaxWidth(80);
  btn_change.setOnAction( e-> {
	  change(width, height, secondaryStage, iv1);
  });
  
  vBox.getChildren().add(btn_change);
  VBox.setMargin(btn_change, new Insets(30,0,0,230));
 
  
  //Przyciski do zoooma
  
  
  Label zoom_label = new Label("Zoom:");
  zoom_label.setFont(new Font(15));
  zoom_label.setStyle("-fx-font-weight: bold");
  final ToggleGroup group = new ToggleGroup ();

  RadioButton rb1 = new RadioButton("1x");
  rb1.setToggleGroup(group);
  rb1.setSelected(true);

  RadioButton rb2 = new RadioButton("2x");
  rb2.setToggleGroup(group);
   
  RadioButton rb3 = new RadioButton("4x");
  rb3.setToggleGroup(group);
  
  RadioButton rb4 = new RadioButton("8x");
  rb4.setToggleGroup(group);
  
  vBox.getChildren().add(zoom_label);
  vBox.getChildren().addAll(rb1, rb2, rb3, rb4);
  
  VBox.setMargin(zoom_label, new Insets(30,0,0,30));
  VBox.setMargin(rb1, new Insets(10,0,0,30));
  VBox.setMargin(rb2, new Insets(5,0,0,30));
  VBox.setMargin(rb3, new Insets(5,0,0,30));
  VBox.setMargin(rb4, new Insets(5,0,0,30));

  
  RadioButton array [] = {rb1, rb2, rb3, rb4};

  rb1.setOnAction(e->{
	  zoom =  zoom(array, secondaryStage, iv1);

  });
  rb2.setOnAction(e->{
	  
	  zoom = zoom(array, secondaryStage, iv1);

  });
  rb3.setOnAction(e->{
	  
	  zoom = zoom(array, secondaryStage, iv1);

  });
  rb4.setOnAction(e->{
	  
	  zoom = zoom(array, secondaryStage, iv1);
  });
  Scene scene = new Scene(vBox, 600, 500);
							
  primaryStage.setScene(scene);
  primaryStage.setOnCloseRequest(e -> {
                                       e.consume();
                                       exit_dialog();
                                      });
  
  primaryStage.show();
 }

private int zoom(RadioButton rb[], Stage stage, ImageView iv1) {
	int zoom = 0;
	for(RadioButton r : rb) {
		if(r.isSelected()) {
			
			zoom=Integer.parseInt(r.getText().substring(0,1));
			change_zoom(iv1,stage.getWidth(), stage.getHeight(), zoom);
			break;
		}
	}
	return zoom;
	
}

private void colorblind_mode(ImageView iv1) {
	


    if(iv1.getEffect() == null) {
	    ColorAdjust color_adjust = new ColorAdjust();
	
	    // set hue, saturation, brightness, and contrast
	    color_adjust.setHue(0.4);
	    color_adjust.setBrightness(0.6);
	    color_adjust.setContrast(0.8);
	    color_adjust.setSaturation(0.1);
	    // set effect
	    iv1.setEffect(color_adjust);
    }    
    else {
    	iv1.setEffect(null);
    }
    
	
}
private void configureAndShowStage(final String name, final Stage stage, final Screen screen,ImageView iv1) {  
    StackPane root = new StackPane();  
    Scene scene = new Scene(root, 500, 400);  
    
    stage.setScene(scene);  
    stage.setTitle(name);  
    
    canvas = new Canvas(650, 490);
    gc = canvas.getGraphicsContext2D();
    
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setPrefSize(scene.getWidth(), scene.getHeight());
    scrollPane.setContent(canvas);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);
    HBox box = new HBox();
    box.getChildren().add(scrollPane);
    disp_frame();
    root.getChildren().add(canvas);
    root.getChildren().add(box);
    Rectangle2D bounds = screen.getBounds();  
    //stage.setX(bounds.getMinX() + (bounds.getWidth() - 300) / 2);  
   // stage.setY(bounds.getMinY() + (bounds.getHeight() - 200) / 2);  
    stage.show();  

}  


 private void disp_frame()
{
 pixelWriter = gc.getPixelWriter();
 pixelFormat = PixelFormat.getByteRgbInstance();
 buffer = frames.get_frame();      
 pixelWriter.setPixels(5, 5, FRAME_WIDTH, FRAME_HEIGHT,   
                       pixelFormat, buffer, 0, 
                       FRAME_WIDTH*3);
}
 public void item_1()
  {
   System.out.println("item 1");
  } 
 
 public void off(Button btn_on, Button btn_off, Stage stage) {
	 btn_on.setDisable(false);
	 btn_off.setDisable(true);
	 stage.close();
 }
 
 public void change(TextField width, TextField height, Stage stage, ImageView iv1) {
	 
 try {
   int width_int = Integer.parseInt(width.getText()); 
   int height_int = Integer.parseInt(height.getText());

   if(width_int <=0 || height_int <=0) {
	   warning_alert("SIZE CANNOT BE NEGATIVE");
	   return;
   }
   stage.setWidth(width_int);
   stage.setHeight(height_int);
   iv1.setFitWidth(stage.getWidth()*zoom);
   iv1.setFitHeight(stage.getHeight()*zoom);
 }
   catch(NumberFormatException x) 
 	{
	   	warning_alert("CHANGE CANNOT BE DONE.");
 	}

   
 }
 
 
 
 public void change_zoom(ImageView iv1,double width, double height, int zoom) {

	 iv1.setFitWidth(width*zoom);
	 iv1.setFitHeight(height*zoom);

 }
 public void warning_alert(String message) {

	   Alert alert = new Alert(AlertType.WARNING,
             message, 
	    ButtonType.OK);

	   alert.setResizable(true);
	   Optional<ButtonType> result = alert.showAndWait();
 }
 public void exit_dialog()
  {
   System.out.println("exit dialog");

   Alert alert = new Alert(AlertType.CONFIRMATION,
                           "Do you really want to exit the program?.", 
 			    ButtonType.YES, ButtonType.NO);

   alert.setResizable(true);
   alert.onShownProperty().addListener(e -> { 
                                             Platform.runLater(() -> alert.setResizable(false)); 
                                            });

  Optional<ButtonType> result = alert.showAndWait();
  if (result.get() == ButtonType.YES)
   {
    Platform.exit();
   } 
  else 
   {
   }

  }
}
