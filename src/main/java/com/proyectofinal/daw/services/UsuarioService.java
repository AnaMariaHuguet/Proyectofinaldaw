package com.proyectofinal.daw.services;

import java.util.Map;

import com.proyectofinal.daw.entities.Usuario;
import com.proyectofinal.daw.repositories.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository repoUsuario;

    public Page<Usuario> findAll(Map<String, String> params) {
        int currentPage = params.get("page") != null ? Integer.valueOf(params.get("page").toString()) - 1 : 0;
        int pageSize = params.get("size") != null ? Integer.valueOf(params.get("size").toString()) : 6;
        String sortBy = params.get("sortby") != null ? params.get("sortby").toString() : "id";
        String order = params.get("order") != null ? params.get("order").toString() : "asc";
        PageRequest pageRequest = PageRequest.of(currentPage, pageSize,
                order.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());

        return repoUsuario.findAll(pageRequest);
    }
}
