package com.nordicpeak.flowengine.queries.basemapquery.test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import se.riges.lm.rmi.ValidateLM;
import se.riges.lm.rmi.exceptions.LMAccountException;
import se.riges.lm.rmi.exceptions.LMUnavailableException;
import se.riges.lm.rmi.interfaces.IAddress;
import se.riges.lm.rmi.interfaces.IEstate;

public class ValidateLMTestClient {

	public static void main(String[] args) throws RemoteException, NotBoundException, LMAccountException, LMUnavailableException {
		
		String lmRMIServerAddress = "gisserver";
		int lmRMIServerPort = 1099;
		String lmRMIServerName = "searchlm";
		String pud = "Sundsvall GRANLO 5:8";
		
		Registry registry = LocateRegistry.getRegistry(lmRMIServerAddress,lmRMIServerPort);

		ValidateLM searchLM = (ValidateLM) registry.lookup(lmRMIServerName);
		
		IEstate estate = searchLM.validateEstate(pud, "sundsvall");
		
		if(estate != null) {
			
			System.out.println("PUD " + estate.getEstateName() + " (" + estate.getEstateID() + ") is valid");
			
		} else {
			
			System.out.println("PUD " + pud + " is not valid");
			
		}
		
		String addrStr = "Sundsvall Skanörvägen 12";
		
		IAddress address = searchLM.validateAddress(addrStr, "sundsvall");
		
		if(address != null) {
			
			System.out.println("Address " + addrStr + ", " + address.getEstateName() + " (" + address.getEstateID() + ") is valid");
			
		} else {
			
			System.out.println("Address " + addrStr + " is not valid");
			
		}
		 
		

	}

}
