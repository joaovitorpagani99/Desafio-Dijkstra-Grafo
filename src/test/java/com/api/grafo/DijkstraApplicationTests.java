package com.api.grafo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class DijkstraApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testDistanciaRotaABC() throws Exception {
		mockMvc.perform(post("/distance")
				.contentType("application/json")
				.content("{\"path\": [\"A\", \"B\", \"C\"]}"))
				.andExpect(status().isOk())
				.andExpect(content().string("9"));
	}

	@Test
	void testRecuperarGrafo() throws Exception {
		mockMvc.perform(get("/graph/{graphId}", 1L))
				.andExpect(status().isOk());
	}

}
