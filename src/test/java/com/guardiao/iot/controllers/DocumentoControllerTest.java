package com.guardiao.iot.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guardiao.iot.dto.DocumentoDTO;
import com.guardiao.iot.bussines.iservice.DocumentoService;
import com.guardiao.iot.controllers.documento.DocumentoController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;
import java.util.Arrays;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

class DocumentoControllerTest {

    @InjectMocks
    private DocumentoController documentoController;

    @Mock
    private DocumentoService documentoService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(documentoController).build();
    }

    @Test
    void testSalvarDocumento() throws Exception {
        DocumentoDTO documentoDTO = new DocumentoDTO();
        documentoDTO.setNomeDocumento("Documento Teste");

        MockMultipartFile file = new MockMultipartFile("file", "documento.pdf", "application/pdf",
                "PDF Content".getBytes());
        MockMultipartFile jsonFile = new MockMultipartFile("documentoDTO", "", "application/json",
                new ObjectMapper().writeValueAsBytes(documentoDTO));

        when(documentoService.save(any(DocumentoDTO.class), any(MultipartFile.class))).thenReturn(documentoDTO);

        mockMvc.perform(multipart("/documentos")
                .file(file)
                .param("documentoDTO", new ObjectMapper().writeValueAsString(documentoDTO))
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated());

        verify(documentoService, times(1)).save(any(DocumentoDTO.class), any(MultipartFile.class));
    }

    @Test
    void testDeletarDocumento() throws Exception {
        doNothing().when(documentoService).deleteById(1L);

        mockMvc.perform(delete("/documentos/1"))
                .andExpect(status().isNoContent());

        verify(documentoService, times(1)).deleteById(1L);
    }

    @Test
    void testBuscarDocumentoPorId() throws Exception {
        DocumentoDTO documentoDTO = new DocumentoDTO();
        documentoDTO.setId(1L);
        when(documentoService.findById(1L)).thenReturn(Optional.of(documentoDTO));

        mockMvc.perform(get("/documentos/1"))
                .andExpect(status().isOk());

        verify(documentoService, times(1)).findById(1L);
    }

    @Test
    void testListarTodosDocumentos() throws Exception {

        List<DocumentoDTO> documentoDTOs = Arrays.asList(
            new DocumentoDTO() {{
                setId(1L);
                setNomeDocumento("Documento 1");
            }},
            new DocumentoDTO() {{
                setId(2L);
                setNomeDocumento("Documento 2");
            }}
        );

        // Simula o comportamento do serviço
        when(documentoService.findAll()).thenReturn(documentoDTOs);

        mockMvc.perform(get("/documentos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$.length()").value(documentoDTOs.size())) 
                .andExpect(jsonPath("$[0].id").value(1L)) 
                .andExpect(jsonPath("$[0].nomeDocumento").value("Documento 1")) 
                                                                                
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].nomeDocumento").value("Documento 2"));
                                                                                 

        verify(documentoService, times(1)).findAll();
    }
}