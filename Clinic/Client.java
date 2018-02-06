public class Client {
	private String id;
	private String clientName;
	private Pet petName;
	
	public Client(String id, String clientName, Pet petName) {
		this.id = id;
		this.clientName = clientName;
		this.petName = petName;
	}
	
	public String getClientId() {
		return this.id;
	}
	
	public String getClientName() {
		return this.clientName;
	}
	
	public String getPetName() {
		return this.petName.getName();
	}
	
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
}