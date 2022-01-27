public class Calculadora{
	//Si suma es privado no podre acceder al ejecutarlo, pero si esta en publico o protegido no habra problema  
	int suma(int n1, int n2){
		return n1 + n2;
	}
	int resta(int n1, int n2){
		return n1 - n2;
	}
	int multiplicacion(int n1, int n2){
		return n1 * n2;
	}
	int division(int n1, int n2){
		return n1 / n2;
	}
	public static void main(String[] args){
	//Esta es una signatura	
		Calculadora c = new Calculadora();
		
		System.out.println("La suma es: "+ c.suma(20, 30));
		System.out.println("La suma es: "+ c.resta(20, 30));
		System.out.println("El producto es: "+ c.multiplicacion(20, 30));
		System.out.println("La division es: "+ c.division(20, 30));
	}
}