package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.entity.CargoEntity;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.repository.CargoRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CargoServiceTest {
    @InjectMocks
    private CargoService cargoService;
    @Mock
    private CargoRepository cargoRepository;

    @Test
    public void shouldSaveCargoToCompradorWithSuccess() throws RegraDeNegocioException {
        // SETUP
        CargoEntity cargoMock = getCargoMock();
        when(cargoRepository.findByNome(anyString())).thenReturn(
                Optional.of(cargoMock));
        cargoMock.setNome("ROLE_COMPRADOR");

        // ACT
        cargoService.saveCargo(CompradorServiceTest.getCompradorEntity());

        // ASSERT
        verify(cargoRepository, times(1)).save(
                Mockito.any(CargoEntity.class));
    }

    @Test
    public void shouldSaveCargoToCompanhiaWithSuccess() throws RegraDeNegocioException {
        // SETUP
        CargoEntity cargoMock = getCargoMock();
        when(cargoRepository.findByNome(anyString())).thenReturn(
                Optional.of(cargoMock));
        cargoMock.setNome("ROLE_COMPANHIA");

        // ACT
        cargoService.saveCargo(CompanhiaServiceTest.getCompanhiaEntity());

        // ASSERT
        verify(cargoRepository, times(1))
                .save(Mockito.any(CargoEntity.class));
    }

    @Test
    public void shouldSaveCargoToUsuarioWithSuccess() throws RegraDeNegocioException {
        // SETUP
        CargoEntity cargoMock = getCargoMock();
        when(cargoRepository.findByNome(anyString())).thenReturn(
                Optional.of(cargoMock));

        // ACT
        cargoService.saveCargo(UsuarioServiceTest.getUsuarioEntity());

        // ASSERT
        verify(cargoRepository, times(1))
                .save(Mockito.any(CargoEntity.class));
    }

    private static CargoEntity getCargoMock(){
        CargoEntity cargoEntity = new CargoEntity();
        cargoEntity.setIdCargo(1);
        cargoEntity.setNome("ROLE_ADMIN");
        cargoEntity.setUsuarios(new HashSet<>());
        return cargoEntity;
    }
}
