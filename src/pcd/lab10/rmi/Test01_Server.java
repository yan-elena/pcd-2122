package pcd.lab10.rmi;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

// QUANDO LANCIAMO L'APPLICAZIONE, ricordare di lanciare nella cartella build/classes/java/main perchè è qui che si trova
// la root di tutte le classi ->> comando: rmiregistry
// questa applicazione rimane in esecuzione perchè è un processo che fa da middleware
// lanciare il middleware prima di lanciare questo test!!!
public class Test01_Server  {
                
    public static void main(String args[]) {
        
        try {
            HelloService helloObj = new HelloServiceImpl();
            HelloService helloObjStub = (HelloService) UnicastRemoteObject.exportObject(helloObj, 0);

            Counter count = new CounterImpl(0);
            Counter countStub = (Counter) UnicastRemoteObject.exportObject(count, 0);
            
            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            
            registry.rebind("helloObj", helloObjStub);
            registry.rebind("countObj", countStub);
            
            System.out.println("Objects registered.");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}