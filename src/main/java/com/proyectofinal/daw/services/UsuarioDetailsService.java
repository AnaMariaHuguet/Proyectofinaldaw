package com.proyectofinal.daw.services;

import java.util.HashSet;
import java.util.Set;
import java.util.List;

import com.proyectofinal.daw.entities.Funciones;
import com.proyectofinal.daw.entities.Usuario;
import com.proyectofinal.daw.repositories.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("usuarioDetailsService")
public class UsuarioDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario usuario = usuarioService.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        List<Funciones> funciones = usuario.getFunciones();
        for (Funciones funcion : funciones) {
            authorities.add(new SimpleGrantedAuthority(funcion.getNombre()));
        }
        return new User(email, usuario.getContrasenya(), authorities);

    }

}
