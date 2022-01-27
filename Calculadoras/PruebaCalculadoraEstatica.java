public class PruebaCalculadoraEstatica{ 
	//Para java es mejor trabajar con instancias que con metodos estaticos
	public static void main(String[] args){
		
		//Out es objeto estatico declarado en la clase System  
		System.out.println("La suma es: "+ CalculadoraEstatica.suma(20, 30));
		System.out.println("La suma es: "+ CalculadoraEstatica.resta(20, 30));
		System.out.println("El producto es: "+ CalculadoraEstatica.multiplicacion(20, 30));
		System.out.println("La division es: "+ CalculadoraEstatica.division(20, 30));
	}
}