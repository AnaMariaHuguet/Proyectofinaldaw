package com.proyectofinal.daw.services;

import java.util.Map;

import com.proyectofinal.daw.entities.Prestamo;
import com.proyectofinal.daw.entities.Usuario;
import com.proyectofinal.daw.repositories.PrestamoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PrestamoService {

    @Autowired
    PrestamoRepository repoPrestamo;

    public Page<Prestamo> findAll(Map<String, String> params, Usuario usuario) {
        int currentPage = params.get("pageprest") != null ? Integer.valueOf(params.get("pageprest").toString()) - 1 : 0;
        int pageSize = params.get("sizeprest") != null ? Integer.valueOf(params.get("sizeprest").toString()) : 6;
        String sortBy = params.get("sortbyprest") != null ? params.get("sortbyprest").toString() : "id";
        String order = params.get("orderprest") != null ? params.get("orderprest").toString() : "asc";
        PageRequest pageRequest = PageRequest.of(currentPage, pageSize,
                order.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());

        return repoPrestamo.findAllByUsuario(usuario, pageRequest);
    }
}
