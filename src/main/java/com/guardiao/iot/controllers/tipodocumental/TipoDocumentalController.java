package com.guardiao.iot.controllers.tipodocumental;

import com.guardiao.iot.bussines.iservice.TipoDocumentalService;
import com.guardiao.iot.dto.TipoDocumentalDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tiposdocumentais")
@CrossOrigin(origins = "http://localhost:5173")
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
        return tipoDocumental.map(value -> ResponseEntity.ok().body(value))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}")
    public ResponseEntity<TipoDocumentalDTO> salvarTipoDocumental(@PathVariable Long id,
            @RequestBody TipoDocumentalDTO tipoDocumentalDTO) {
        try {
            TipoDocumentalDTO savedTipoDocumental = tipoDocumentalService.save(tipoDocumentalDTO, id);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTipoDocumental);
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarTipoDocumental(@PathVariable Long id) {
        try {
            tipoDocumentalService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @PostMapping("/verificartipoarquivopdf")
    public ResponseEntity<?> postMethodName(@RequestParam("file") MultipartFile file) {
        try {
            TipoDocumentalDTO tipoDocumental = tipoDocumentalService.classificarDocumentoEVerificar(file);
            return ResponseEntity.ok(tipoDocumental);

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao processar o documento: " + e.getMessage());
        }
    }
}
