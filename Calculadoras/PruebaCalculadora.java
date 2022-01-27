public class PruebaCalculadora{
	//Este es el metodo main
	public static void main(String[] args){
	//Esta es una instancia	
		Calculadora c = new Calculadora();
		
		System.out.println("La suma es: "+ c.suma(20, 30));
		System.out.println("La suma es: "+ c.resta(20, 30));
		System.out.println("El producto es: "+ c.multiplicacion(20, 30));
		System.out.println("La division es: "+ c.division(20, 30));
	}
}