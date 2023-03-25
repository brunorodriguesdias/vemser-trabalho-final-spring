package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.entity.CargoEntity;
import br.com.dbc.javamosdecolar.entity.CompanhiaEntity;
import br.com.dbc.javamosdecolar.entity.CompradorEntity;
import br.com.dbc.javamosdecolar.entity.UsuarioEntity;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.repository.CargoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CargoService {

    private final CargoRepository cargoRepository;

    protected void saveCargo(CompradorEntity compradorEntity) throws RegraDeNegocioException {
        CargoEntity cargoEntity = findByNome("ROLE_COMPRADOR");
        cargoEntity.getUsuarios().add(compradorEntity);
        cargoRepository.save(cargoEntity);
    }

    protected void saveCargo(CompanhiaEntity companhiaEntity) throws RegraDeNegocioException {
        CargoEntity cargoEntity = findByNome("ROLE_COMPANHIA");
        cargoEntity.getUsuarios().add(companhiaEntity);
        cargoRepository.save(cargoEntity);
    }

    protected void saveCargo(UsuarioEntity usuarioEntity) throws RegraDeNegocioException {
        CargoEntity cargoEntity = findByNome("ROLE_ADMIN");
        cargoEntity.getUsuarios().add(usuarioEntity);
        cargoRepository.save(cargoEntity);
    }

    protected CargoEntity findByNome (String nome) throws RegraDeNegocioException {
        return cargoRepository.findByNome(nome)
                .orElseThrow(() -> new RegraDeNegocioException("Cargo não encontrado!"));
    }
}
