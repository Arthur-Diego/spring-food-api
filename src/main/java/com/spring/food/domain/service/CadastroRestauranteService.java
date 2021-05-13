package com.spring.food.domain.service;

import com.spring.food.domain.exception.EntidadeNaoEncontradaException;
import com.spring.food.domain.model.Cozinha;
import com.spring.food.domain.model.Estado;
import com.spring.food.domain.model.Restaurante;
import com.spring.food.domain.repository.CozinhaRepository;
import com.spring.food.domain.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CadastroRestauranteService {

	public static final String MSG_RESTAURANTE_NAO_ENCONTRADO = "Não existe cadastro de cozinha com código %d";
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	public Restaurante salvar(Restaurante restaurante) {
		Long cozinhaId = restaurante.getCozinha().getId();
		
		Cozinha cozinha = cozinhaRepository.findById(cozinhaId)
			.orElseThrow(() -> new EntidadeNaoEncontradaException(
					String.format(MSG_RESTAURANTE_NAO_ENCONTRADO, cozinhaId)));
		
		restaurante.setCozinha(cozinha);
		
		return restauranteRepository.save(restaurante);
	}

	public Restaurante buscarOuFalhar(Long restauranteId) {
		return restauranteRepository.findById(restauranteId)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(
						String.format(MSG_RESTAURANTE_NAO_ENCONTRADO, restauranteId)));
	}
	
}
