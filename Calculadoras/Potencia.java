public class Potencia{
	double cubo(double n1, double n2){
		return n1 ^ n2;
	}
	double cuadrado(double n1, double n2){
		return n1 ^ n2;
	}
	public static void main(double[] args){
		Potencia c = new Potencia();
		
		System.out.println("El Cubo es: "+ c.cubo(2, 3));
		System.out.println("El Cuadrado es: "+ c.cuadrado(3, 2));
	}
}