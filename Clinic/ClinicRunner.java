public class ClinicRunner {
	public static void main(String[] args) {
		Clinic clinic = new Clinic();
		
		clinic.addClient(0,new Client("0","Anna", new Dog("Bork")));
		clinic.addClient(1,new Client("1","David", new Cat("Kitty")));
		clinic.addClient(2,new Client("2","Alex", new Cat("Jerry")));
		
		clinic.showClientBase();
		
		//clinic.findClientByName("Client");
		clinic.findClientByName("Anna");
		
		//clinic.findPetByName("Bork");
		//clinic.findPetByName("Kity");
		
		clinic.editClientName(0);
		
		
		//clinic.removeClient(1);
		
		clinic.showClientBase();
	}
}