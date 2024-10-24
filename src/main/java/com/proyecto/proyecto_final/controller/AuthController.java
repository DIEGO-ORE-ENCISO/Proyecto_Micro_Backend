package com.proyecto.proyecto_final.controller;

import com.proyecto.proyecto_final.modelos.Integrante;
import com.proyecto.proyecto_final.modelos.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("/api")
public class AuthController {

    private static final String FILE_PATH = "integrantes.txt"; // Archivo en resources

    /**
     * Método para autenticar un usuario.
     * @param usuario objeto que contiene el código y password del usuario.
     * @return ResponseEntity con el resultado de la autenticación.
     */
    @PostMapping("/autenticar")
    public ResponseEntity<String> autenticar(@RequestBody Usuario usuario) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(FILE_PATH)))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 2 && parts[0].equals(usuario.getCodigo()) && parts[1].equals(usuario.getPassword())) {
                    return ResponseEntity.ok("Autenticación exitosa");
                }
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error al leer el archivo");
        }
        return ResponseEntity.status(401).body("Problemas en la autenticación");
    }

    /**
     * Método para obtener la lista de integrantes.
     * @return ResponseEntity con la lista de integrantes.
     */
    @GetMapping("/get-integrantes")
    public ResponseEntity<List<Integrante>> getIntegrantes() {
        List<Integrante> integrantes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(FILE_PATH)))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 3) {
                    integrantes.add(new Integrante(parts[2], parts[3])); // Suponiendo que el formato es código;nombre;apellido
                }
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
        return ResponseEntity.ok(integrantes);
    }
}