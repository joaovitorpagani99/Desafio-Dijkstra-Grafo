package com.api.grafo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.grafo.model.PathData;
import com.api.grafo.model.Rotas;
import com.api.grafo.model.dto.ResponsePayloadCaminhoMinimo;
import com.api.grafo.model.dto.responseRotaDTO;
import com.api.grafo.service.GrafoService;

@RestController
@RequestMapping("/graph")
public class GrafoController {

	@Autowired
	private GrafoService grafoService;

	@PostMapping
	public ResponseEntity<Rotas> salvarGrafo(@RequestBody() Rotas rotas) {
		Rotas grafoSalvo = this.grafoService.salvar(rotas);
		return ResponseEntity.status(201).body(grafoSalvo);
	}

	@GetMapping("/{graphId}")
	public ResponseEntity<Rotas> recuperarGrafo(@PathVariable("graphId") Long graphId) {
		try {
			return ResponseEntity.ok(this.grafoService.buscar(graphId));
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/routes/from/{town1}/to/{town2}")
	public ResponseEntity<List<responseRotaDTO>> getRoutes(
			@PathVariable("town1") String town1,
			@PathVariable("town2") String town2,
			@RequestParam(required = false) Integer maxStops,
			@RequestBody() Rotas rotas) {

		List<responseRotaDTO> menorCaminho = this.grafoService.listarRotas(town1.toUpperCase(), town2.toUpperCase(),
				maxStops, rotas);
		return ResponseEntity.ok(menorCaminho);
	}

	@PostMapping("/routes/{graphId}/from/{town1}/to/{town2}")
	public ResponseEntity<List<responseRotaDTO>> encontrarRotasComGrafoSalvo(
			@PathVariable("graphId") Long graphId,
			@PathVariable("town1") String town1,
			@PathVariable("town2") String town2,
			@RequestParam(required = false) Integer maxStops,
			@RequestBody() Rotas rotas) {
		try {
			List<responseRotaDTO> menorCaminho = this.grafoService.buscarGrafoEListarRotas(graphId, town1.toUpperCase(),
					town2.toUpperCase(),
					maxStops);
			return ResponseEntity.ok(menorCaminho);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}

	}

	@PostMapping("/distance")
	public ResponseEntity<Integer> calcularDistancia(@RequestBody PathData pathData) {
		Integer totalDistance = this.grafoService.calcularDistancia(pathData);
		return ResponseEntity.ok(totalDistance);
	}

	@PostMapping("/distance/{graphId}")
	public ResponseEntity<Integer> calcularDistanciaComGrafoSalvo(@PathVariable("graphId") Long graphId,
			@RequestBody PathData pathData) {
		try {
			Integer totalDistance = this.grafoService.calcularDistanciaComGrafoSalvo(graphId, pathData);
			return ResponseEntity.ok(totalDistance);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/distance/from/{town1}/to/{town2}")
	public ResponseEntity<?> determinarADistanciaMinima(
			@PathVariable("town1") String town1,
			@PathVariable("town2") String town2,
			@RequestBody() Rotas rotas) {
		if (town1.equals(town2))
			return new ResponseEntity<>(0, HttpStatus.OK);

		ResponsePayloadCaminhoMinimo caminhoMinimo = this.grafoService.calcularDistanciaMinimaEntreBairros(
				town1.toUpperCase(), town2.toUpperCase(),
				rotas);
		return ResponseEntity.ok(caminhoMinimo);
	}

	@PostMapping("/distance/{graphId}/from/{town1}/to/{town2}")
	public ResponseEntity<?> getDeterminarADistanciaMinima(
			@PathVariable("graphId") Long graphId,
			@PathVariable("town1") String town1,
			@PathVariable("town2") String town2) {
		try {
			if (town1.equals(town2))
				return new ResponseEntity<>(0, HttpStatus.OK);
			Rotas rotas = this.grafoService.buscar(graphId);

			ResponsePayloadCaminhoMinimo totalDistance = this.grafoService.calcularDistanciaMinimaEntreBairros(town1,
					town2,
					rotas);
			return ResponseEntity.ok(totalDistance);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

}
