package com.api.grafo.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.grafo.grafo.Dijkstra;
import com.api.grafo.model.Grafo;
import com.api.grafo.model.Rotas;
import com.api.grafo.model.dto.RotaDTO;
import com.api.grafo.model.dto.responseRotaDTO;
import com.api.grafo.repository.GrafoRepository;

@Service
public class GrafoService {

	@Autowired
	private GrafoRepository repository;

	public Rotas salvar(Rotas rotas) {
		Rotas grafo = this.repository.save(rotas);
		return grafo;
	}

	public Rotas buscar(Long graphId) {
		Rotas grafo = this.repository.findById(graphId).get();
		return grafo;
	}

	public List<responseRotaDTO> listarRotas(String town1, String town2, Integer maxStops, Rotas rotas) {
		Dijkstra dijkstra = new Dijkstra();
		List<Grafo> rotasAtual = rotas.getData();
		dijkstra.adicionarRotas(rotasAtual);
		List<String> caminhos = dijkstra.encontrarCaminhosComMaximoParadas(town1, town2, maxStops);
		List<responseRotaDTO> responses = new ArrayList<>();
		for (String caminho : caminhos) {
			RotaDTO rota = new RotaDTO();
			rota.setRoute(caminho);
			rota.setStops(caminho.length() - 1);
			responseRotaDTO response = new responseRotaDTO();
			response.setRotas(new ArrayList<>()); // Inicializa a lista de rotas
			response.getRotas().add(rota);
			responses.add(response);
		}
		return responses;
	}

}
