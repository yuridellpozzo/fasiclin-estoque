package com.br.fasipe.estoque.pedidofornecedor.services;

import com.br.fasipe.estoque.pedidofornecedor.dto.ProfissionalDto;
import com.br.fasipe.estoque.pedidofornecedor.models.Profissional;
import com.br.fasipe.estoque.pedidofornecedor.repository.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true) // Transação 'readOnly' na classe (Resolve o erro de dupla anotação)
public class ProfissionalService {

    // CORREÇÃO: A declaração do Logger fica fora do método e é static final
    private static final Logger log = LoggerFactory.getLogger(ProfissionalService.class);

    @Autowired
    private ProfissionalRepository profissionalRepository;

    // Removida a anotação @Transactional duplicada aqui, pois já está na classe
    public List<ProfissionalDto> findAdminProfissional() {
        
        // Buscamos o profissional com ID 1
        Optional<Profissional> adminOpt = profissionalRepository.findById(1);

        log.info("Buscando profissional administrador com ID 1");
        
        if (adminOpt.isPresent()) {
            Profissional admin = adminOpt.get();
            ProfissionalDto dto = new ProfissionalDto();
            dto.setId(admin.getId());
            
            // CORRIGIDO: Usa getNome() da PessoaFis. (Assumindo que você ajustou o getter)
            dto.setNomePessoa(admin.getPessoaFis().getNome()); 

            return Collections.singletonList(dto); 
        }

        log.warn("Profissional administrador com ID 1 não encontrado no banco de dados.");
        return Collections.emptyList();
    }
}