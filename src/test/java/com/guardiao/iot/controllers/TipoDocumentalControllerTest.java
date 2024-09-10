/*package com.guardiao.iot.controllers;

import com.guardiao.iot.bussines.iservice.TipoDocumentalService;
import com.guardiao.iot.dto.TipoDocumentalDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.guardiao.iot.controllers.tipodocumental.TipoDocumentalController;


class TipoDocumentalControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private TipoDocumentalController tipoDocumentalController;

    @Mock
    private TipoDocumentalService tipoDocumentalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(tipoDocumentalController).build();
    }

    @Test
    void testListarTodosTiposDocumentais() throws Exception {
        List<TipoDocumentalDTO> tiposDocumentais = Arrays.asList(
                new TipoDocumentalDTO(), // Supondo que você tenha um construtor vazio
                new TipoDocumentalDTO()
        );

        when(tipoDocumentalService.findAll()).thenReturn(tiposDocumentais);

        mockMvc.perform(get("/tiposdocumentais"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(tiposDocumentais.size()));

        verify(tipoDocumentalService, times(1)).findAll();
    }

    @Test
    void testBuscarTipoDocumentalPorId() throws Exception {
        TipoDocumentalDTO tipoDocumentalDTO = new TipoDocumentalDTO();
        tipoDocumentalDTO.setNomeDocumento("Teste Nome");
        when(tipoDocumentalService.findById(anyLong())).thenReturn(Optional.of(tipoDocumentalDTO));

        mockMvc.perform(get("/tiposdocumentais/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeDocumento").exists());

        verify(tipoDocumentalService, times(1)).findById(anyLong());
    }

    @Test
    void testSalvarTipoDocumental() throws Exception {
        TipoDocumentalDTO tipoDocumentalDTO = new TipoDocumentalDTO();
        when(tipoDocumentalService.save(any(TipoDocumentalDTO.class), anyLong())).thenReturn(tipoDocumentalDTO);

        mockMvc.perform(post("/tiposdocumentais/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nomeDocumento\": \"Novo Documento\"}"))
                .andExpect(status().isCreated());

        verify(tipoDocumentalService, times(1)).save(any(TipoDocumentalDTO.class), anyLong());
    }

    @Test
    void testDeletarTipoDocumental() throws Exception {
        doNothing().when(tipoDocumentalService).deleteById(anyLong());

        mockMvc.perform(delete("/tiposdocumentais/1"))
                .andExpect(status().isNoContent());

        verify(tipoDocumentalService, times(1)).deleteById(anyLong());
    }
}*/