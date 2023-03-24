package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.entity.CargoEntity;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.repository.CargoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CargoService {

    private CargoRepository cargoRepository;
    protected CargoEntity findByNome (String nome) throws RegraDeNegocioException {
        return cargoRepository.findByNome(nome)
                .orElseThrow(() -> new RegraDeNegocioException("Cargo não encontrado!"));
    }
}
