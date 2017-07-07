package com.example.imm.citi.agents;

import android.app.Activity;

import com.example.imm.citi.technicalClasses.Service;

import java.util.ArrayList;

public abstract class FactoryAgent {
	Service service;
	Activity parent;
    ArrayList<Agent> agents = new ArrayList<>(), remoteAgents;


    public FactoryAgent(Service serv, Activity act, ArrayList<Agent> agents){
        service = serv;
        parent = act;
		remoteAgents = agents;
    }

    public abstract void fetchAgents();

    public void finishFetch(){
        ArrayList<ArrayList<Agent>> unsortedAgents = new ArrayList<>();
        unsortedAgents.add(agents);
        unsortedAgents.add(remoteAgents);

        agents = service.sortResult(unsortedAgents);
        service.showResult(agents);
    }

	ArrayList<LocalAgent> search(ArrayList<android.support.v4.util.Pair<String, String>> chosenOptions){
		
		return null;
	}
	
	ArrayList<LocalAgent> sort(ArrayList<LocalAgent> unsortedAgents){
		
		return null;
	}
}