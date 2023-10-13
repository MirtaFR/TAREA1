package MIRTA.ActividadUno;

import org.bson.Document;

public class Cliente {

	String nombre;
	int numero_cliente;
	int tipo_cuenta;
	
	String _id;
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getNumero_cliente() {
		return numero_cliente;
	}
	public void setNumero_cliente(int numero_cliente) {
		this.numero_cliente = numero_cliente;
	}
	public int getTipo_cuenta() {
		return tipo_cuenta;
	}
	public void setTipo_cuenta(int tipo_cuenta) {
		this.tipo_cuenta = tipo_cuenta;
	}
	
	 @Override
    public String toString() {
        return "Cliente [\n  nombre=" + nombre + ",\n  número de cliente=" + numero_cliente + ",\n  tipo de cuenta=" + tipo_cuenta + "\n]";
    }
	
}
