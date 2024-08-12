package com.guardiao.iot.controllers.documento;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guardiao.iot.bussines.iservice.DocumentoService;
import com.guardiao.iot.dto.DocumentoDTO;
import com.guardiao.iot.entity.DocumentoEntity.Documento;
import javax.validation.Valid;

@RestController
@RequestMapping("/documentos")
public class DocumentoController {

    @Autowired
    private DocumentoService documentoService;

    @GetMapping
    public ResponseEntity<List<DocumentoDTO>> listarTodosDocumentos() {
        List<DocumentoDTO> documentos = documentoService.findAll();
        return ResponseEntity.ok().body(documentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentoDTO> buscarDocumentoPorId(@PathVariable Long id) {
        Optional<DocumentoDTO> documento = documentoService.findById(id);
        return documento.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DocumentoDTO> salvarDocumento(@RequestBody @Valid DocumentoDTO documento) {
        DocumentoDTO savedDocumento = documentoService.save(documento);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDocumento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarDocumento(@PathVariable Long id) {
        documentoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
