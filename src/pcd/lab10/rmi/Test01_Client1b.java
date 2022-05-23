package pcd.lab10.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

// se rilanciamo più volte il clientb vediamo che otteniamo il valore aggiornato, perchè il valore persiste nel remoto
// se abbiamo più client -> non è thread safe perchè rmi ha più thread, quindi se chiamano lo stesso oggetto abbiamo
// delle corse critiche, per evitare questa cosa dovremmo mettere tutte le cose di synchronized
public class Test01_Client1b {

    private Test01_Client1b() {}

    public static void main(String[] args) {

        String host = (args.length < 1) ? null : args[0];
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            Counter c = (Counter) registry.lookup("countObj");
            int value = c.getValue();
            System.out.println("> value "+value);
            c.inc();
            System.out.println("> value "+c.getValue());
            
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}