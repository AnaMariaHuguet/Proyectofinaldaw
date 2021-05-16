package com.proyectofinal.daw.config;

import com.proyectofinal.periodicos.LimpiarReservas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class AppConfig {

    @Scheduled(cron = "0 0 23 * * *")
    public void cadaDiaALa1() {
        limpiarReservas().limpiar();
    }

    @Bean
    public LimpiarReservas limpiarReservas() {
        return new LimpiarReservas();
    }

}
