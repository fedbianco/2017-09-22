package it.polito.tdp.formulaone.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.formulaone.db.FormulaOneDAO;

public class Model {
	
	private FormulaOneDAO dao;
	private List<Integer> anni;
	private List<Race> races;
	Graph< Race ,DefaultWeightedEdge> graph = null; 

	public Model() {
		this.dao = new FormulaOneDAO();
		this.anni = this.dao.getAllYears();
		
	}
	
	public List<Integer> getAllYears(){
		return anni;
	}
	public List<String> getAllRaces(int myYear){
		return this.dao.getAllNameRaces(myYear);
	}
	
	
	public void creaGrafo(int myYear) {
		graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		races = this.dao.getAllRacesForYear(myYear);
		Graphs.addAllVertices(graph, races);
		for(Race r : this.races) {
			for(Race ra : this.races) {
				if(this.dao.getWeight(myYear, r, ra)!=0 && r.getRaceId()!= ra.getRaceId()) 
					Graphs.addEdge(graph, r, ra, this.dao.getWeight(myYear, r, ra));
			
			}
		}
		System.out.println("Grafo creato! (year: " + myYear + ")");
		System.out.println("# Vertici: " + graph.vertexSet().size());
		for(DefaultWeightedEdge s : this.graph.edgeSet()) {
			System.out.println("# Archi: " + s + graph.getEdgeWeight(s) + "\n");
		}
		System.out.println("# Archi: " + graph.edgeSet().size());
	}
	
	public List<RaceAndWeight> getMax() {
		
		int max = 0;
		Race race1 = null;
		Race race2 = null;
		List<RaceAndWeight> raceAndWeight = new ArrayList<RaceAndWeight>();
		List<RaceAndWeight> result = new ArrayList<RaceAndWeight>();
		
		for(DefaultWeightedEdge e : this.graph.edgeSet()) {
			if(this.graph.getEdgeWeight(e)>= max) {
				max = (int) this.graph.getEdgeWeight(e);
				race1 = this.graph.getEdgeSource(e);
				race2 = this.graph.getEdgeTarget(e);
				raceAndWeight.add(new RaceAndWeight(race1.getName(),race2.getName(),max));
			}
		}
		Collections.sort(raceAndWeight);
		
		for(RaceAndWeight rw : raceAndWeight) {
			if(rw.getPeso() == raceAndWeight.get(0).getPeso()) {
				result.add(rw);
			}
		}
		
		return result;
			
		
	}


}
