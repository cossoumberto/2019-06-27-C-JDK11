package it.polito.tdp.crimes.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private EventsDao dao;
	private Graph<String , DefaultWeightedEdge> grafo;
	private List<String> bestPercorso;
	private Integer bestPeso;
	
	public Model() {
		dao = new EventsDao();
	}
	
	public List<String> getCategorie(){
		return dao.listCategorie();
	}
	
	public List<LocalDate> getGiorni(){
		return dao.listGiorni();
	}
	
	public void creaGrafo (String categoria, LocalDate giorno) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, dao.listTipologie(categoria, giorno));
		for(CoppiaTipologie c : dao.listCoppieTipologie(categoria, giorno))
			Graphs.addEdge(grafo, c.getT1(), c.getT2(), c.getPeso());
		System.out.println(grafo.vertexSet().size());
		System.out.println(grafo.edgeSet().size());
	}
	
	public List<CoppiaTipologie> getArchiStampa() {
		if(grafo.edgeSet().size()!=0) {
			List<CoppiaTipologie> list = new ArrayList<>();
			Double max = null;
			Double min = null;
			for(DefaultWeightedEdge e : grafo.edgeSet()) {
				if(max==null || grafo.getEdgeWeight(e)>max)
					max = grafo.getEdgeWeight(e);
				if(min==null || grafo.getEdgeWeight(e)<min)
					min = grafo.getEdgeWeight(e);
			}	
			Double mediano = (max + min)/2;
			for(DefaultWeightedEdge e : grafo.edgeSet()) {
			if(grafo.getEdgeWeight(e)<mediano)
				list.add(new CoppiaTipologie (grafo.getEdgeSource(e), Graphs.getOppositeVertex(grafo, e, grafo.getEdgeSource(e)),
						(int)grafo.getEdgeWeight(e)));
			}
			Collections.sort(list);
			return list;
		} else 
			return null;
	}
	
	public List<String> percorso(CoppiaTipologie ct){
		bestPercorso = null;
		bestPeso = 0;
		List<String> parziale = new ArrayList<>();
		parziale.add(ct.getT1());
		cerca(parziale, 0, ct.getT2());
		return bestPercorso;
	}

	private void cerca(List<String> parziale, int peso, String finale) {
		if(parziale.get(parziale.size()-1).equals(finale)) {
			if(peso>bestPeso) {
				bestPercorso = new ArrayList<>(parziale);
				bestPeso = peso;
			}
		return;
		} else {
			if(grafo.degreeOf(parziale.get(parziale.size()-1))>0)
				for(String s : Graphs.neighborListOf(grafo, parziale.get(parziale.size()-1)))
					if(!parziale.contains(s)) {
						parziale.add(s);
						cerca(parziale, peso + (int) grafo.getEdgeWeight(grafo.getEdge(parziale.get(parziale.size()-2), s)), finale);
						parziale.remove(parziale.size()-1);
					}
		}
	}
	
	
}


