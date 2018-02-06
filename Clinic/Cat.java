public class Cat implements Pet {
	privatу String name;
	
	public Cat(String name) {
		this.name = name;
	}
	
	@Override 
	public void makeSound() {
		System.out.println("Myau-myau-myau!");
	}
	
	@Override 
	public String getName() {
		return this.name;
	}
}