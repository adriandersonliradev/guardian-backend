package com.guardiao.iot.controllers.tipodocumental;

import com.guardiao.iot.bussines.iservice.TipoDocumentalService;
import com.guardiao.iot.dto.TipoDocumentalDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tiposdocumentais")
public class TipoDocumentalController {

    @Autowired
    private TipoDocumentalService tipoDocumentalService;

    @GetMapping
    public ResponseEntity<List<TipoDocumentalDTO>> listarTodosTiposDocumentais() {
        List<TipoDocumentalDTO> tiposDocumentais = tipoDocumentalService.findAll();
        return ResponseEntity.ok().body(tiposDocumentais);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoDocumentalDTO> buscarTipoDocumentalPorId(@PathVariable Long id) {
        Optional<TipoDocumentalDTO> tipoDocumental = tipoDocumentalService.findById(id);
        return tipoDocumental.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}")
    public ResponseEntity<TipoDocumentalDTO> salvarTipoDocumental(@PathVariable Long id, @RequestBody TipoDocumentalDTO tipoDocumentalDTO) {
        try {
            TipoDocumentalDTO savedTipoDocumental = tipoDocumentalService.save(tipoDocumentalDTO, id);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTipoDocumental);
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarTipoDocumental(@PathVariable Long id) {
        tipoDocumentalService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}