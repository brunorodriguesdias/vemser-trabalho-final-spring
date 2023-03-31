package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.in.AviaoCreateDTO;
import br.com.dbc.javamosdecolar.dto.in.PassagemCreateDTO;
import br.com.dbc.javamosdecolar.dto.in.VooCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.AviaoDTO;
import br.com.dbc.javamosdecolar.dto.outs.PassagemDTO;
import br.com.dbc.javamosdecolar.dto.outs.VooDTO;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AviaoService aviaoService;
    private final PassagemService passagemService;
    private final VooService vooService;

    public AviaoDTO createAviao(Integer idCompanhia, AviaoCreateDTO aviaoCreateDTO) throws RegraDeNegocioException {
        aviaoCreateDTO.setIdCompanhia(idCompanhia);
        return aviaoService.create(aviaoCreateDTO);
    }

    public PassagemDTO createPassagem(Integer idCompanhia, PassagemCreateDTO passagemCreateDTO) throws RegraDeNegocioException {
        passagemCreateDTO.setIdCompanhia(idCompanhia);
        return passagemService.create(passagemCreateDTO);
    }

    public VooDTO createVoo(Integer idCompanhia, VooCreateDTO vooCreateDTO) {
        vooCreateDTO.setIdCompanhia(idCompanhia);
        return vooService.create(vooCreateDTO);
    }

}
