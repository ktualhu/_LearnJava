public class Dog implements Pet {
	private String name;
	
	public Dog(String name) {
		this.name = name;
	}
	
	@Override 
	public void makeSound() {
		System.out.println("Gav-gav-gav!");
	}
	
	@Override 
	public String getName() {
		return this.name;
	}
}