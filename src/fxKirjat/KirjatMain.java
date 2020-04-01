package fxKirjat;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.application.Platform;
import kirjasto.Kirjakokoelma;


/**
 * @author antontuominen
 * @version 28 Jan 2020
 *
 */
public class KirjatMain extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
		    final FXMLLoader ldr = new FXMLLoader(getClass().getResource("KirjatGUIView.fxml"));
            final Pane root = (Pane)ldr.load();

		    final KirjatGUIController kirjakokoelmaCtrl = (KirjatGUIController)ldr.getController();
		    
		    final Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("kirjat.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Kirjakokoelma");

			primaryStage.setOnCloseRequest((event) -> {
                if ( !kirjakokoelmaCtrl.voikoSulkea() ) event.consume();
            });
        
            Kirjakokoelma kirjakokoelma = new Kirjakokoelma();  
            kirjakokoelmaCtrl.setKirjakokoelma(kirjakokoelma); 
        
            primaryStage.show();

            Application.Parameters params = getParameters(); 
            if ( params.getRaw().size() > 0 ) 
                kirjakokoelmaCtrl.lueTiedosto(params.getRaw().get(0));  
            else
                if ( !kirjakokoelmaCtrl.avaa() ) Platform.exit();


		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param args ei käytössä
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
