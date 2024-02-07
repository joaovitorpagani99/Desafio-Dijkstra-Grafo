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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.api.grafo.model.Rotas;
import com.api.grafo.model.dto.responseRotaDTO;
import com.api.grafo.service.GrafoService;

@RestController
@RequestMapping("/graph")
public class GrafoController {

	@Autowired
	private GrafoService grafoService;

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public Rotas salvarGrafo(@RequestBody() Rotas rotas) {
		return this.grafoService.salvar(rotas);
	}

	@GetMapping("/{graphId}")
	public Rotas recuperarGrafo(@PathVariable("graphId") Long graphId) {
		return this.grafoService.buscar(graphId);
	}

	@PostMapping("/routes/from/{town1}/to/{town2}")
	public ResponseEntity<List<responseRotaDTO>> getRoutes(
			@PathVariable("town1") String town1,
			@PathVariable("town2") String town2,
			@RequestParam(required = false) Integer maxStops,
			@RequestBody() Rotas rotas) {

		List<responseRotaDTO> menorCaminho = this.grafoService.listarRotas(town1, town2, maxStops, rotas);
		return ResponseEntity.ok(menorCaminho);
	}

}
