import java.util.ArrayList;
import java.util.Scanner;

public class Clinic {
	private ArrayList<Client> clients;
	
	public Clinic() {
		this.clients = new ArrayList<>();
	}
	
	public void addClient(int position, Client client) {
		clients.add(position, client);
		System.out.println("Client " + client.getClientName() + " with pet " + client.getPetName() + " was successfully added!\n");
	}
	
	
	
	public void findClientByName(String clientName) {
		boolean find = false;
		int i = 0;
		for(Client client : clients) {
			if(client.getClientName() == clientName) {
				find = true;
				i++;
				if(find) {
					System.out.println("By your order was found " + i + " results!");
					System.out.println("Client id: " + client.getClientId());
					System.out.println("Client name: " + client.getClientName());
					System.out.println("Client pet name: " + client.getPetName());
					System.out.println("**********************");
					find = false;
					break;
				}
			}
		}
		if(i == 0) {
			System.out.println("Sorry! We could not find anything by your order called *"+ clientName +"*!\n");
		}
		i = 0;
		find = false;
	}
	
	public void findPetByName(String petName) {
		boolean find = false;
		int i = 0;
		for(Client client : clients) {
			if(client.getPetName() == petName) {
				find = true;
				i++;
				if(find) {
					System.out.println("We found pet called " + petName + "! His owner is " + client.getClientName() + "!");
					find = false;
					break;
				}
			}
		}
		if(i == 0) {
			System.out.println("Sorry! We could not find anything by your order called *"+ petName +"*!\n");
		}
		i = 0;
		find = false;
	}
	
	public void removeClient(int clientId) {
		clients.remove(clientId);
		System.out.println("Client with " + clientId + " was deleted!");
	}
	
	public void editClientName(int clientId) {
		if(clientId <= clients.size()) {
			clients.get(clientId).setClientName("Helen");
			
		}
		else {
			System.out.println("Client with this id is not found!");
		}
	}
	
	public void showClientBase() {
		int i = 0;
		for(Client client : clients) {
			System.out.println("Client id: " + i);
			System.out.println("Client name: " + client.getClientName());
			System.out.println("Client pet name: " + client.getPetName());
			System.out.println("**********************");
			i++;
		}
		i = 0;
		
	}
	
}