package application;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

//Implementação da interface de conexão RMI do servidor
public class Server extends UnicastRemoteObject implements ServerInterface{
	
	List<Contact> listOfContacts = new ArrayList<>();

	protected Server() throws RemoteException {
		super();
	}

	// Retornar a lista de contatos do servidor
	public List<Contact> returnContactList() throws RemoteException {
		return this.listOfContacts;
	}

	// Adicionar um novo contato a lista de contatos do servidor
	public Boolean addNewContact(Contact contact) throws RemoteException {
		
		Boolean hasContact = false;
		
		// Caso contato já exista, ele não é adicionado
		for(int i = 0; i < this.listOfContacts.size(); i++) {
			if(listOfContacts.get(i).getName().equals(contact.getName())) {
				hasContact = true;
				return hasContact;
			}
		}
		
		// Caso contato não exista, ele é adicionado
		this.listOfContacts.add(contact);
		return hasContact;
	}

	// Atualizar um contato na lista de contatos do servidor
	public Boolean updateContact(Contact oldContact, Contact newContact) throws RemoteException {
		
		Boolean hasContact = false;
		
		// Primeiro checar se o nome atualizado do contato já existe
		if(!oldContact.getName().equals(newContact.getName())) {
			for(int i = 0; i < this.listOfContacts.size(); i++) {
				if(listOfContacts.get(i).getName().equals(newContact.getName())) {
					hasContact = true;
					return hasContact;
				}
			}
		}
		
		// Se chegou aqui, o novo nome do contato não existe e o contato é atualizado
		for(int i = 0; i < this.listOfContacts.size(); i++) {
			if(listOfContacts.get(i).getName().equals(oldContact.getName())) {
				listOfContacts.get(i).setName(newContact.getName());
				listOfContacts.get(i).setNumber(newContact.getNumber());
				return hasContact;
			}
		}
		
		return hasContact;
		
	}

	// Deletar um contato da lista de contatos do servidor
	public void deleteContact(Contact contact) throws RemoteException {
		for(int i = 0; i < this.listOfContacts.size(); i++) {
			if(listOfContacts.get(i).getName().equals(contact.getName())) {
				listOfContacts.remove(i);
			}
		}
		
	}

}
