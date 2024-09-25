package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class ServerRegister extends Application{
	
	private int port;
	private String name;
	private IntermediatorConnection intermediatorConnection = new IntermediatorConnection();
	private Server server;
	
	// Criar página de login no intermediador
    private Parent createIntermediatorLogin() {
    	
    	Pane root = new Pane();
    	
    	BackgroundFill backgroundFill = new BackgroundFill(Color.valueOf("#E27D60"), new CornerRadii(10), new Insets(10));

    	Background background = new Background(backgroundFill);
    	
    	root.setBackground(background);
    	
    	root.setPrefSize(544, 492);
    	
    	Label intermediatorLabel = new Label("LOGIN NO INTERMEDIADOR");
    	intermediatorLabel.setFont(new Font("Monaco",36));
    	intermediatorLabel.setLayoutX(50);
    	intermediatorLabel.setLayoutY(40);
    	
    	Label title = new Label("Insira o nome do servidor, o ip do intermediador a se \n conectar, o nome do intermediador e a porta. \n Então clique no botão abaixo para fazer o login.");
    	title.setFont(new Font("Arial",18));
    	title.setLayoutX(65);
    	title.setLayoutY(120);
    	title.setTextAlignment(TextAlignment.CENTER);
    	
    	Label serverName = new Label("Nome :");
    	serverName.setFont(new Font("Arial",13));
    	serverName.setLayoutX(165);
    	serverName.setLayoutY(225);
    	
    	TextField serverNameInput = new TextField("agenda1");
    	serverNameInput.setLayoutX(215);
    	serverNameInput.setLayoutY(220);
    	serverNameInput.setMinWidth(220);
    	
    	Label intermediatorIp = new Label("Ip do Intermediador :");
    	intermediatorIp.setFont(new Font("Arial",13));
    	intermediatorIp.setLayoutX(88);
    	intermediatorIp.setLayoutY(265);
    	
    	TextField intermediatorIpInput = new TextField("192.168.0.14");
    	intermediatorIpInput.setLayoutX(215);
    	intermediatorIpInput.setLayoutY(260);
    	intermediatorIpInput.setMinWidth(220);
    	
    	Label intermediatorName = new Label("Nome do Intermediador :");
    	intermediatorName.setFont(new Font("Arial",13));
    	intermediatorName.setLayoutX(65);
    	intermediatorName.setLayoutY(305);
    	
    	TextField intermediatorNameInput = new TextField("Intermediator");
    	intermediatorNameInput.setLayoutX(215);
    	intermediatorNameInput.setLayoutY(300);
    	intermediatorNameInput.setMinWidth(220);
    	
    	Label intermediatorPort = new Label("Porta do Intermediador :");
    	intermediatorPort.setFont(new Font("Arial",13));
    	intermediatorPort.setLayoutX(68);
    	intermediatorPort.setLayoutY(345);
    	
    	TextField intermediatorPortInput = new TextField("6000");
    	intermediatorPortInput.setLayoutX(215);
    	intermediatorPortInput.setLayoutY(340);
    	intermediatorPortInput.setMinWidth(220);
    	
    	Button loginButton = new Button("Fazer Login no Intermediador");
    	loginButton.setLayoutX(185);
    	loginButton.setLayoutY(410);
    	loginButton.setMinWidth(150);
    	loginButton.setOnAction(event -> {
    		
    		this.name = serverNameInput.getText();
    		this.port = Integer.valueOf(intermediatorPortInput.getText());
    		this.intermediatorConnection.setUrl(intermediatorIpInput.getText(), intermediatorPortInput.getText(), intermediatorNameInput.getText());
    		
    		// Criar o registro RMI do servidor no servidor de nomes
    		try {
    			
				Registry rmiRegistry = LocateRegistry.getRegistry(Integer.valueOf(intermediatorPortInput.getText()));
				server = new Server();
				rmiRegistry.rebind(serverNameInput.getText(), server);
				
			} catch (RemoteException e) {
				e.printStackTrace();
			}
    		
    		// Registrar o servidor no intermediador de mensagens e resgatar uma lista atualizada
    		// de contatos dele
    		this.intermediatorConnection.registerServer(serverNameInput.getText());
    		this.server.listOfContacts = this.intermediatorConnection.updateServerContactList(serverNameInput.getText());
    		
        	Stage window = (Stage)loginButton.getScene().getWindow();
        	Scene scene = new Scene(createScene());
        	scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        	window.setScene(scene);
        	window.setResizable(false);
			
        });
    	
    	root.getChildren().addAll(intermediatorLabel, title, serverName, serverNameInput, intermediatorIp, intermediatorIpInput, intermediatorName, intermediatorNameInput,
    			intermediatorPort, intermediatorPortInput, loginButton);
    	
    	return root;
    }
	
    // Criar página "Servidor Online"
	private Parent createScene() {
    	Pane root = new Pane();
    	
    	BackgroundFill backgroundFill = new BackgroundFill(Color.valueOf("#5CDB95"), new CornerRadii(10), new Insets(10));

    	Background background = new Background(backgroundFill);
    	
    	root.setBackground(background);
    	
    	root.setPrefSize(504, 322);
    	              	
    	Label server = new Label("SERVIDOR ONLINE");
    	server.setFont(new Font("Monaco",36));
    	server.setLayoutX(100);
    	server.setLayoutY(55);
    	server.setTextAlignment(TextAlignment.CENTER);
    	
    	String serverName = String.format("NOME: %s", this.name.toUpperCase());
    	Label serverNameLabel = new Label(serverName);
    	serverNameLabel.setFont(new Font("Monaco",20));
    	serverNameLabel.setLayoutX(100);
    	serverNameLabel.setLayoutY(145);
    	
    	String serverPort = String.format("PORTA: %d", this.port);
    	Label serverPortLabel = new Label(serverPort);
    	serverPortLabel.setFont(new Font("Monaco",20));
    	serverPortLabel.setLayoutX(100);
    	serverPortLabel.setLayoutY(205);
    	serverPortLabel.setTextAlignment(TextAlignment.CENTER);
  	
    	root.getChildren().addAll(server, serverNameLabel, serverPortLabel);
    	
    	return root;
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			Scene loginScene = new Scene(createIntermediatorLogin());
			primaryStage.setTitle("Servidor");;
			primaryStage.setScene(loginScene);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}	
	}
	
	public static void main(String[] args) throws Exception{
		launch(args);
	}

}

