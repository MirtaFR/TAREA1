package MIRTA.ActividadUno;


import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.bson.Document;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.lt;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;

/**
 * Hello world!
 *
 */
public class App 
{
	
	String base="EmpresaUno";
	String coleccion="Clientes";
	String uri = "mongodb://localhost:27017";
    Bson projectionFields = Projections.fields(
            Projections.include("nombre", "numero_cliente", "tipo_cuenta"),
            Projections.excludeId());
    
	CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
    CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
	Scanner sc= new Scanner(System.in);
	
	public App() {
		
		int opcion=0;
		
		try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase(base).withCodecRegistry(pojoCodecRegistry);
            MongoCollection<Cliente> collection = database.getCollection(coleccion,Cliente.class);
           // Cliente movie = collection.find(eq("title", "Back to the Future")).first();
            //System.out.println(movie);
            
            do {
    			System.out.println("Selecciona una opción\n"
    					+ "1: Mostrar todos\n"
    					+ "2: Eliminar Varios\n"
    					+ "3: Eliminar uno\n"
    					+ "4: Actualizar varios\n"
    					+ "5: Actualizar uno\n"
    					+ "6: Buscar uno\n"
    					+ "7: Buscar Varios\n"
    					+ "8: Insertar uno\n"
    					+ "9: Insertar varios\n"
    					+ "0: Salir\n");
    			
    			opcion=Integer.parseInt(sc.nextLine());
    			
    			switch(opcion)
    			{
    				case 1:
    					mostrarTodos(collection);
    					break;
    				case 2:
    					eliminarVarios(collection);
    					break;
    				case 3:
    					eliminarUno(collection);
    					break;
    				case 4:
    					actualizarVarios(collection);
    					break;
    				case 5:
    					actualizarUno(collection);
    					break;
    				case 6: 
    					buscarUno(collection);
    					break;
    				case 7:
    					buscarVarios(collection);
    					break;
    				case 8:
    					insertarUno(database);
    					break;
    				case 9:
    					insertarVarios(database);
    					break;
    					
    			}			
    			
    		}while(opcion!=0);
        
        }
		
		
		
		

		
	}
	public void mostrarTodos(MongoCollection<Cliente> collection) {
            //collection.find(eq("nombre", "cesar")) para aplicar un filtro
            MongoCursor<Cliente> cursor = collection.find()
                    .projection(projectionFields)
                    .sort(Sorts.descending("nombre"))
                    .iterator();
            try {
                while(cursor.hasNext()) {
                    System.out.println(cursor.next().toString());
                }
            } finally {
                cursor.close();
            }
        
		
	}
	public void eliminarVarios(MongoCollection<Cliente> collection){
		System.out.println("Ingresa el tipo de cuenta para eliminar \n");
		int cuenta = Integer.parseInt(sc.nextLine());

	    try {
	    	DeleteResult result = collection.deleteMany(Filters.eq("tipo_cuenta",cuenta));
	    	
	    	 System.out.println("Registros eliminados: "+ result.getDeletedCount() +"\n");
	    } catch (MongoException me) {
            System.err.println("Unable to update due to an error: " + me + "\n");
        }
		
	}
	public void eliminarUno(MongoCollection<Cliente> collection){

		System.out.println("Ingresa el numero de cliente\n");
		int numero_cliente = Integer.parseInt(sc.nextLine());
		
	    try {
	    	DeleteResult result = collection.deleteOne(Filters.eq("numero_cliente",numero_cliente));
	    	
	    	 System.out.println("Registro eliminado"+"\n");
	    } catch (MongoException me) {
            System.err.println("Unable to update due to an error: " + me+"\n");
        }
		
	}
	public void actualizarVarios(MongoCollection<Cliente> collection){
		
		
		System.out.println("Ingresa el tipo de cuenta a modificar \n");
		int cuenta = Integer.parseInt(sc.nextLine());
		
		//System.out.println("Ingresa el numero de cliente\n");
		//int numero_cliente = Integer.parseInt(sc.nextLine());
		
		
		
		System.out.println("Ingresa el nuevo tipo de cuenta\n");
		int  tipo_cuenta = Integer.parseInt(sc.nextLine());
	    try {
	    	UpdateResult result = collection.updateMany(Filters.eq("tipo_cuenta",cuenta), 
				Updates.combine(Updates.set("tipo_cuenta",tipo_cuenta)));
	    	System.out.println("registros modificados: " + result.getModifiedCount()+"\n");
	    } catch (MongoException me) {
            System.err.println("Unable to update due to an error: " + me+"\n");
        }
	}
	public void actualizarUno(MongoCollection<Cliente> collection){
		System.out.println("Ingresa el numero de cliente\n");
		int numero_cliente = Integer.parseInt(sc.nextLine());
		
		System.out.println("Ingresa el nuevo nombre\n");
		String nombre = sc.nextLine();
		
		System.out.println("Ingresa el nuevo tipo de cuenta\n");
		int  tipo_cuenta = Integer.parseInt(sc.nextLine());
	    try {
	    	UpdateResult result = collection.updateOne(Filters.eq("numero_cliente",numero_cliente), 
				Updates.combine(Updates.set("nombre",nombre),Updates.set("tipo_cuenta",tipo_cuenta)));
	    	System.out.println("registros modificados: " + result.getModifiedCount()+"\n");
	    } catch (MongoException me) {
            System.err.println("Unable to update due to an error: " + me+"\n");
        }
		
	}
	public void buscarUno(MongoCollection<Cliente> collection){
		System.out.println("Ingresa el nombre\n");
		String nombre = sc.nextLine();
		Cliente cliente = collection.find(eq("nombre", nombre)).first();
		if(cliente!=null)
			System.out.println(cliente+"\n");
		else
	        System.out.println("registo no encontrado");
	}
	public void buscarVarios(MongoCollection<Cliente> collection){
		System.out.println("Ingresa el nombre\n");
		String nombre = sc.nextLine();
		MongoCursor<Cliente> cursor = collection.find(eq("nombre", nombre))
                .projection(projectionFields)
                .sort(Sorts.descending("nombre"))
                .iterator();
        try {
            while(cursor.hasNext()) {
                System.out.println(cursor.next().toString());
            }
        } finally {
            cursor.close();
        }
	}
	public void insertarUno(MongoDatabase database){
		
		MongoCollection<Document> collection = database.getCollection(coleccion);
		
		System.out.println("Ingresa el nombre\n");
		String nombre = sc.nextLine();
		
		System.out.println("Ingresa el numero de cliente\n");
		int numero_cliente = Integer.parseInt(sc.nextLine());
		
		System.out.println("Ingresa el tipo de cuenta\n");
		int  tipo_cuenta = Integer.parseInt(sc.nextLine());
		try {
            InsertOneResult result = collection.insertOne(new Document()
                    .append("_id", new ObjectId())
                    .append("nombre", nombre)
                    .append("numero_cliente", numero_cliente)
                    .append("tipo_cuenta", tipo_cuenta));
            System.out.println("Success! Inserted document id: " + result.getInsertedId()+"\n");
        } catch (MongoException me) {
            System.err.println("Unable to insert due to an error: " + me+"\n");
        }
		
	}
	public void insertarVarios(MongoDatabase database){
		
		MongoCollection<Document> collection = database.getCollection(coleccion);
		
		System.out.println("Capture la información de los 3 clientes \n");
	
		List<Document> clientes =new ArrayList<>();
		
		
		for(int i=0; i<3;i++)
		{
				System.out.println("Ingresa el nombre\n");
				String nombre = sc.nextLine();
				
				System.out.println("Ingresa el numero de cliente\n");
				int numero_cliente = Integer.parseInt(sc.nextLine());
				
				System.out.println("Ingresa el tipo de cuenta\n");
				int  tipo_cuenta = Integer.parseInt(sc.nextLine());
				
				clientes.add( new Document()
                    .append("_id", new ObjectId())
                    .append("nombre", nombre)
                    .append("numero_cliente", numero_cliente)
                    .append("tipo_cuenta", tipo_cuenta));
		}
		
		 try {
             InsertManyResult result = collection.insertMany(clientes);
             System.out.println("Inserted document ids: " + result.getInsertedIds()+"\n");
         } catch (MongoException me) {
             System.err.println("Unable to insert due to an error: " + me+"\n");
         }
	}
	
	
    public static void main( String[] args )
    {
        App programa = new App();
        
    }
}


        
