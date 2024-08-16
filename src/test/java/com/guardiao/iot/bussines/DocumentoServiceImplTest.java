package com.guardiao.iot.bussines;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import com.guardiao.iot.bussines.service.DocumentoServiceImpl;
import com.guardiao.iot.dto.DocumentoDTO;
import com.guardiao.iot.entity.DocumentoEntity.Documento;
import com.guardiao.iot.infrastructure.irepository.DocumentoRepository;
import com.guardiao.iot.infrastructure.irepository.TipoDocumentalRepository;
import com.guardiao.iot.mappers.DocumentoMapper;
import java.util.Arrays;

import java.util.List;

import java.util.Optional;

class DocumentoServiceImplTest {

    @InjectMocks
    private DocumentoServiceImpl documentoService;

    @Mock
    private DocumentoRepository documentoRepository;

    @Mock
    private TipoDocumentalRepository tipoDocumentalRepository;

    @Mock
    private MultipartFile file;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveDocumento() throws Exception {
        DocumentoDTO documentoDTO = new DocumentoDTO();
        documentoDTO.setNomeDocumento("Documento Teste");
        when(documentoRepository.save(any(Documento.class))).thenReturn(new Documento());

        DocumentoDTO savedDocumento = documentoService.save(documentoDTO, file);

        assertNotNull(savedDocumento);
        verify(documentoRepository, times(1)).save(any(Documento.class));
    }

    @Test
    void testDeleteById() {
        Documento documento = new Documento();
        documento.setId(1L);
        when(documentoRepository.findById(1L)).thenReturn(Optional.of(documento));

        documentoService.deleteById(1L);

        verify(documentoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindById() {
        Documento documento = new Documento();
        documento.setId(1L);
        when(documentoRepository.findById(1L)).thenReturn(Optional.of(documento));

        Optional<DocumentoDTO> found = documentoService.findById(1L);

        assertTrue(found.isPresent());
        verify(documentoRepository, times(1)).findById(1L);
    }

    @Test
    void testFindAll() {
        // Criar documentos mockados
        Documento documento1 = new Documento();
        documento1.setId(1L);
        documento1.setNomeDocumento("Documento 1");

        Documento documento2 = new Documento();
        documento2.setId(2L);
        documento2.setNomeDocumento("Documento 2");

        List<Documento> documentos = Arrays.asList(documento1, documento2);

        // Configurar o comportamento do repositório mockado
        when(documentoRepository.findAll()).thenReturn(documentos);

        // Chamar o método findAll do serviço
        List<DocumentoDTO> resultado = documentoService.findAll();

        // Verificar o tamanho da lista retornada
        assertEquals(2, resultado.size());

        // Verificar se os documentos retornados correspondem aos esperados
        assertEquals("Documento 1", resultado.get(0).getNomeDocumento());
        assertEquals("Documento 2", resultado.get(1).getNomeDocumento());

        // Verificar se o método findAll do repositório foi chamado uma vez
        verify(documentoRepository, times(1)).findAll();
    }
}