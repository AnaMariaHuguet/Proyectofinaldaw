package com.proyectofinal.daw.services;

import java.util.Map;

import com.proyectofinal.daw.entities.HistoricoPrestamos;
import com.proyectofinal.daw.entities.Usuario;
import com.proyectofinal.daw.repositories.HistoricoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class HistoricoService {

    @Autowired
    HistoricoRepository repoHistorico;

    public Page<HistoricoPrestamos> findAll(Map<String, String> params, Usuario usuario) {
        int currentPage = params.get("pagehist") != null ? Integer.valueOf(params.get("pagehist").toString()) - 1 : 0;
        int pageSize = params.get("sizehist") != null ? Integer.valueOf(params.get("sizehist").toString()) : 6;
        String sortBy = params.get("sortbyhist") != null ? params.get("sortbyhist").toString() : "id";
        String order = params.get("orderhist") != null ? params.get("orderhist").toString() : "asc";

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize,
                order.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());

        return repoHistorico.findAllByUsuario(usuario, pageRequest);
    }
}
