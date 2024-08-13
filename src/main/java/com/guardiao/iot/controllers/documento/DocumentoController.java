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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guardiao.iot.bussines.iservice.DocumentoService;
import com.guardiao.iot.dto.DocumentoDTO;
import com.guardiao.iot.entity.DocumentoEntity.Documento;
import javax.validation.Valid;
import org.springframework.http.MediaType;



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

    @GetMapping("/{id}/arquivo")
    public ResponseEntity<byte[]> getDocumentoArquivo(@PathVariable Long id) {
        byte[] pdfContent = documentoService.getDocumentoArquivo(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfContent);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentoDTO> salvarDocumento(
            @RequestParam("file") MultipartFile file,
            @RequestParam("documentoDTO") String documentoDTOJson) {
        try {
            // Deserializando o DTO de JSON
            ObjectMapper objectMapper = new ObjectMapper();
            DocumentoDTO documentoDTO = objectMapper.readValue(documentoDTOJson, DocumentoDTO.class);

            // Chamando o serviço para salvar o documento
            DocumentoDTO savedDocumento = documentoService.save(documentoDTO, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDocumento);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarDocumento(@PathVariable Long id) {
        documentoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
