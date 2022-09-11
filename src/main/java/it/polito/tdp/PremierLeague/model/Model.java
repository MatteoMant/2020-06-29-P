package it.polito.tdp.PremierLeague.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	PremierLeagueDAO dao;
	private Graph<Match, DefaultWeightedEdge> grafo;
	private Map<Integer, Match> idMap;
	
	public Model() {
		dao = new PremierLeagueDAO();
		idMap = new HashMap<>();
		this.dao.listAllMatches(idMap);
	}
	
	public void creaGrafo(int mese, int minuti) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		// Aggiunta dei vertici
		Graphs.addAllVertices(this.grafo, this.dao.listAllMatchesWithMonth(mese));
		
		// Aggiunta degli archi
		for (Adiacenza a : this.dao.getAllAdiacenze(idMap, minuti, mese)){
			Graphs.addEdge(this.grafo, a.getM1(), a.getM2(), a.getPeso());
		}
		
	}
	
	public List<Adiacenza> getConnessioniMax() {
		int max = 0;
		List<Adiacenza> connessioniMax = new LinkedList<>();
		
		for (DefaultWeightedEdge e : this.grafo.edgeSet()) {
			if (this.grafo.getEdgeWeight(e) > max) {
				max = (int)this.grafo.getEdgeWeight(e);
			}
		}
		
		for (DefaultWeightedEdge e : this.grafo.edgeSet()) {
			if (this.grafo.getEdgeWeight(e) == max) {
				Match m1 = this.grafo.getEdgeSource(e);
				Match m2 = this.grafo.getEdgeTarget(e);
				int peso = (int)this.grafo.getEdgeWeight(e);
				connessioniMax.add(new Adiacenza(m1, m2, peso));
			}
		}
		
		return connessioniMax;
	}
	
	public int getNumVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNumArchi() {
		return this.grafo.edgeSet().size();
	}
	
}
