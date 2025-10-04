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
@Transactional(readOnly = true)
public class ProfissionalService {

    private static final Logger log = LoggerFactory.getLogger(ProfissionalService.class);

    @Autowired
    private ProfissionalRepository profissionalRepository;
    @Transactional(readOnly = true)
    public List<ProfissionalDto> findAdminProfissional() {
        
        Optional<Profissional> adminOpt = profissionalRepository.findById(1);

        log.info("Buscando profissional administrador com ID 1");
        if (adminOpt.isPresent()) {
            Profissional admin = adminOpt.get();
            ProfissionalDto dto = new ProfissionalDto();
            dto.setId(admin.getId()); // Usa getId() do Profissional
            
            // Usa getPessoaFis() e getNomePessoa() (Corrigido para evitar erro de símbolo)
            dto.setNomePessoa(admin.getPessoaFis().getNome()); 

            return Collections.singletonList(dto); 
        }

        log.warn("Profissional administrador com ID 1 não encontrado no banco de dados.");
        return Collections.emptyList();
    }
}