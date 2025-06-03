package com.lifesimulator.lifesimulator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

@Configuration
public class ScannerConfig {
    @Bean(destroyMethod = "close")
    public Scanner scanner() {
        return new Scanner(System.in);
    }
}
