package application;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

public class IntermediatorConnection {
	
	public IntermediatorInterface intermediatorConnection;
	private String intermediatorUrl;
	
	public IntermediatorConnection() {
	}
	
	// Definir a url do intermediador
	public void setUrl(String ip, String port, String name) {
		String url = String.format("rmi://%s:%s/%s", ip, port, name);
		this.intermediatorUrl = url;
	}
	
	// Registrar o servidor logado no intermediador
	public void registerServer(String Name) {
		try {
			this.intermediatorConnection = (IntermediatorInterface) Naming.lookup(intermediatorUrl);
			this.intermediatorConnection.registerNewServer(Name);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
	}
	
	// Atualizar a lista de contatos do servidor com outra lista de outro servidor
	public List<Contact> updateServerContactList(String serverName){
		try {
			this.intermediatorConnection = (IntermediatorInterface) Naming.lookup(intermediatorUrl);
			return this.intermediatorConnection.updateServerContactList(serverName);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			return null;
		}
	}

}
