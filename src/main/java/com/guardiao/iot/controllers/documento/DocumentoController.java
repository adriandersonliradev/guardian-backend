package com.guardiao.iot.controllers.documento;

//import java.net.http.HttpHeaders;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guardiao.iot.bussines.iservice.DocumentoService;
import com.guardiao.iot.dto.DocumentoDTO;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders; // Spring Framework
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/documentos")
@CrossOrigin(origins = "http://localhost:5173")
public class DocumentoController {

    @Autowired
    private DocumentoService documentoService;

    @GetMapping
    public ResponseEntity<List<DocumentoDTO>> listarTodosDocumentos() {
        List<DocumentoDTO> documentos = documentoService.findAll();
        return ResponseEntity.ok().body(documentos);
    }

    @GetMapping("/expirados")
    public ResponseEntity<List<DocumentoDTO>> listarTodosDocumentosExpirados() {
        List<DocumentoDTO> documentos = documentoService.findDocumentosExpirados();
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
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"documento_" + id + ".pdf\"")
                .body(pdfContent);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> salvarDocumento(
            @RequestParam("file") MultipartFile file,
            @RequestParam("documentoDTO") String documentoDTOJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            DocumentoDTO documentoDTO = objectMapper.readValue(documentoDTOJson, DocumentoDTO.class);

            DocumentoDTO savedDocumento = documentoService.save(documentoDTO, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDocumento);
        }catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarDocumento(@PathVariable Long id) {
        try {
            documentoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Retorna 500 Internal Server Error
        }
    }

    @DeleteMapping("/excluirdocumentos")
    public ResponseEntity<String> excluirDocumentos(@RequestBody List<Long> ids) {
        try {
            documentoService.excluirDocumentosPorIds(ids);  // Pode usar o método excluirDocumentosPorIds também
            return ResponseEntity.ok("Documentos excluídos com sucesso.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
}
