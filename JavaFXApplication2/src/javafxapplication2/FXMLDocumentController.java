/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 *
 * @author Nate
 */
public class FXMLDocumentController implements Initializable {
    
    /* Load button on main app window */
    @FXML
    private Button loadButton;
    
    private File currentFile;
    @FXML
    private String currentFileName;
    
    @FXML
    private Button loadPrevButton;
    
    @FXML
    private Button selectMiceButton;
    
    Stage loadPrevStage;
    
    Stage miceStage;
    
    /* Buttons that are involved wsith saving */
    @FXML
    private Button saveButton; /* Save button on the main app window */
    @FXML
    private Button save; /* Save button on the SavePopUp Window */
    @FXML
    private Button cancelSave; /* Cancel button on the SavePopUP Window */
    @FXML
    private TextField saveNameField; /*  */
    @FXML
    private Label saveFileName; /*  */
    
    @FXML
    private Button generateButton;
    
    @FXML
    private Button saveMapButton;
    
    private String[] mice = null;
            
    private String[] selectedMice = null;
    
    /* The SavePopUp window */
    Stage saveStage;
    
    /* design for the stage */
    Parent root;
               
    /* Stage for the File Explorer window when loading a new file */
    @FXML
    private Window stage;
    
    /* Sets the currentFile from the Main window controller to the currentFile 
        in the SaveWindow controller */
    public void setFile(File currentFile){
        this.currentFile = currentFile;
    }
    
    /* Handles the functionality of buttons on the main app window */
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
       
        /* If the "Load" button on the main window was pressed */
        if(event.getSource()==loadButton){
            loadDataPopUp(); /* Method handles load */
        }
        
        /* If the "Save" button on the main window was pressed */
        else if(event.getSource()==saveButton){
           
            /* Checks if there is file loaded in */
            if(currentFile != null){
            
                /* Create a new stage for the SavePopUp */
                saveStage=new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SaveWindow.fxml"));
                
                /* Get the design for the SavePopUp from the SaveWindow.fxml file
                    and set it to root */
                root =(Parent) fxmlLoader.load();
                
                /* set the pop up scene to the design from root */
                saveStage.setScene(new Scene(root));

                FXMLDocumentController controller = fxmlLoader.getController(); 
                
                /* Passes currentFile from one controller to the other */
                controller.setFile(currentFile);
                
                /* Sets the label to the name of the file*/
                controller.saveFileName.setText(currentFile.getName());
                
                /* initializes pop up functionality */
                saveStage.initModality(Modality.APPLICATION_MODAL);

                /* sets the owner of the stage to the main window save button */
                saveStage.initOwner(saveButton.getScene().getWindow());
                
                /* Shows the pop up to the user and waits until they have closed
                    it out */
                saveStage.showAndWait();
        
            }else{
                System.out.println("No File to save");
            }
        }
        
        /* If the "Load Previous" button on the main window was pressed */
        else if(event.getSource()==loadPrevButton){
            
            File file = new File(".//savedData.txt");
            if(file.exists()){
                
             
                /* Create a new stage for the SavePopUp */
                loadPrevStage=new Stage();
                GridPane grid = new GridPane();
                grid.setAlignment(Pos.TOP_CENTER);
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(25, 25, 25, 25));

                Scene scene = new Scene(grid, 300, 575);
                loadPrevStage.setScene(scene);
                
                Text scenetitle = new Text("Load Previous:");
                scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
                grid.add(scenetitle, 0, 0, 1, 1);
             
                int x = 1;
                final ToggleGroup group = new ToggleGroup();
                
                try(BufferedReader br = new BufferedReader(new FileReader(file))) {
                    
                    for(String line; (line = br.readLine()) != null; ) {
                        RadioButton lbl = new RadioButton(line.substring(0,line.indexOf(";")));
                        lbl.setToggleGroup(group);
                        lbl.setUserData(line.substring(0,line.indexOf(";")));
                        grid.add(lbl, 0, x);
                        x++;
                    }
                    
                    
                br.close();
                }

                Button btn = new Button("Load");
                Button cBtn = new Button("Cancel");                
                grid.add(btn, 0, x);
                grid.add(cBtn, 1, x);
               
                btn.setOnAction((ActionEvent e) -> {
                    String loadPrevNm = group.getSelectedToggle().getUserData().toString();
  
                    String loadPrevNamePath = null;
                    
                    try(BufferedReader br2 = new BufferedReader(new FileReader(file))) {
                    
                        for(String line; (line = br2.readLine()) != null; ) {
                            String nm = line.substring(0, line.indexOf(";"));
                            
                            if(nm.equals(loadPrevNm)){
                                
                                  loadPrevNamePath = line.substring((line.indexOf(";")+2), line.length());
                                
                                  break;
                            }
                            
                        }
                                        
                        br2.close();
                        
                    }   catch (FileNotFoundException ex) {   
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    loadPrevStage.close();
                    File f = new File(loadPrevNamePath);

                    loadData(f);
                    
                });
                   
                                
                cBtn.setOnAction((ActionEvent e) -> {
                    System.out.println("Cancelling load previous.");
                    loadPrevStage.close();
                });
                    
                /* Shows the pop up to the user and waits until they have closed
                    it out */
                loadPrevStage.showAndWait();
            }
        }
        
        else if(event.getSource()==selectMiceButton){
            
            if(mice == null){
                System.out.println("Load a File First.");
            }else{
                //System.out.println(mice[0]);
                System.out.println("Select Mice");
                
                /* Create a new stage for the SavePopUp */
                miceStage=new Stage();
                GridPane grid = new GridPane();
                grid.setAlignment(Pos.TOP_CENTER);
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(25, 25, 25, 25));

                Scene scene = new Scene(grid, 300, 575);
                miceStage.setScene(scene);
                
                Text scenetitle = new Text("Select Mice:");
                scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
                grid.add(scenetitle, 0, 0, 1, 1);
             
                int x = 1;
                final ToggleGroup group = new ToggleGroup();
                //ActionListener al = new checkbox_handler();
                
                CheckBox[] cBoxes = new CheckBox[mice.length];
                
                if(selectedMice == null){
                    
                    for(int y = 0; y < mice.length; y++){
                        CheckBox cb = new CheckBox(mice[y]);
                        cBoxes[y] = cb;
                        cb.setUserData(mice[y]);
                        grid.add(cb,0,x);
                        x++;
                    }
                
                }else{
                    for(int y = 0; y < mice.length; y++){
                        CheckBox cb = new CheckBox(mice[y]);
                        cBoxes[y] = cb;
                        cb.setUserData(mice[y]);
                        grid.add(cb,0,x);
                        x++;
                        for(int i = 0; i < selectedMice.length; i++){
                            if(mice[y].equals(selectedMice[i])){
                                cb.setSelected(true);
                            }
                        }
                    }
                    
                }
                
                Button btn = new Button("Update");
                Button cBtn = new Button("Cancel");                
                grid.add(btn, 0, x);
                grid.add(cBtn, 1, x);
               
                btn.setOnAction((ActionEvent e) -> {
                    //System.out.println("!");
                    String[] tempSelected = new String[cBoxes.length];
                    int z = 0;
                    for(int y = 0; y < cBoxes.length; y++){
                        if(cBoxes[y].isSelected()){
                            //if(selectedMice == null){
                                tempSelected[z] = cBoxes[y].getText();
                                z++;
                                //System.out.println("AH");
                            //}else{
                            //    selectedMice[selectedMice.length] = cBoxes[y].getText();
                            //}
             //               System.out.println(cBoxes[y].getText());
                        }
                        
                    }
                    
                    selectedMice = tempSelected;
                    
                    for(int y = 0; y< selectedMice.length; y++){
                        System.out.println(selectedMice[y]);
                    }
                    
                    miceStage.close();
                });
                
                cBtn.setOnAction((ActionEvent e) -> {
                    System.out.println("Cancelling Select Mice");
                    miceStage.close();
                });
                
                miceStage.showAndWait();
        
            }
        }
        
        else if(event.getSource()==generateButton){
            System.out.println("Generating Map!!!!");
            
        }
        
        else if(event.getSource()==saveMapButton){
            System.out.println("Save Map");
        }
        /*else{
            System.out.println("....");
        
        }*/
    //    label.setText("Hello World!");
    }
    
    /* Opens the SavePopUp Window and handles all events that occur in that 
        window */
    @FXML
    private void saveWindowPopUp(ActionEvent event) throws IOException{

        
        /* If the "Save" button was pressed in the SavePopUp */
        if(event.getSource()==save){
            
            System.out.println("Saving " + saveNameField.getText());
                
                String filePath = currentFile.getPath();
              
                String fileName = saveNameField.getText();

            /**/
            try {
                
                try (BufferedWriter writer = new BufferedWriter (new FileWriter(".\\savedData.txt",true))) {
                    String toWrite = fileName + "; " + filePath + "\n";
                    writer.write(toWrite);
                }
                
            } catch(IOException e){
            
            } 
            
            /* Set the save stage to the pop up */
            saveStage=(Stage) save.getScene().getWindow();            
            /* Close the pop up */
            saveStage.close();
        }
        
        /* If the "Cancel" button was pressed in the SavePopUp */
        else{
            
            System.out.println("Closing save window");
            
            /* Set the save stage to the pop up */
            saveStage=(Stage) cancelSave.getScene().getWindow();
            
            /* Close the pop up */
            saveStage.close();
        }
    }
    
    /* Opens the file explorer allowing the user to load their dataset */
    private void loadDataPopUp() throws FileNotFoundException, IOException{
        //System.out.println("load data method");
        /* Creates a new file chooser object */
        FileChooser fileChooser = new FileChooser();
        
        /* Sets the file chosen in the file chooser to "file" */
        File file = fileChooser.showOpenDialog(stage);
       
        loadData(file);
    }
    
    private void loadData(File file){   
        /* stores the file into current file */
        currentFile = file;
        //currentFileName = currentFile.getName();
        System.out.println(currentFile.getName());
        System.out.println(currentFile.getPath());
        
        String[] tempMice = {"Mouse 1","Mouse 2", "Mouse 3", "Omega Mouse"};
        mice = tempMice;
        
        
        
        /* Gets the file path of the file */
        String dataPath = file.getPath();
        
      //  parser.parse(dataPath);
        
        /* Gives the file path to the parser
            Parses the data
            Stores the parsed data into a array of strings */
     //   String[][] dataArray = parser.parse(dataPath);

        //System.out.println(dataArray[0][1]);
        //if (file.isFile()){
        //    System.out.println(file.getName());
            //System.out.println(file.lastModified());
        //}else{
        //    System.out.println("No file kiddo.");
        //}
        //check to see if a file was loaded properly
}
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
