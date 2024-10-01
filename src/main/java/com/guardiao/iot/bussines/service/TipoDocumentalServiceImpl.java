package com.guardiao.iot.bussines.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.guardiao.iot.bussines.iservice.TipoDocumentalService;
import com.guardiao.iot.dto.TipoDocumentalDTO;
import com.guardiao.iot.entity.DocumentoEntity.Documento;
import com.guardiao.iot.entity.TipoDocumentoEntity.TipoDocumental;
import com.guardiao.iot.entity.UsuarioEntity.Usuario;
import com.guardiao.iot.infrastructure.irepository.DocumentoRepository;
import com.guardiao.iot.infrastructure.irepository.TipoDocumentalRepository;
import com.guardiao.iot.infrastructure.irepository.UsuarioRepository;
import com.guardiao.iot.mappers.TipoDocumentalMapper;
import java.util.Set;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TipoDocumentalServiceImpl implements TipoDocumentalService {

    private static final Set<String> TIPOS_FIXOS = Set.of(
            "contrato", "relatório financeiro", "ata de reunião", "nota fiscal",
            "proposta comercial", "relatório técnico", "memorando", "laudo pericial",
            "atestado", "contrato de trabalho", "declaração", "declaração e atestado");

    @Autowired
    private TipoDocumentalRepository tipoDocumentalRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DocumentoRepository documentoRepository;

    @Autowired
    private RestTemplate restTemplate;


    private final RabbitTemplate rabbitTemplate;

    private final MessageConverter messageConverter;



    @Override
    @Transactional
    public List<TipoDocumentalDTO> findAll() {
        return tipoDocumentalRepository.findAll().stream()
                .map(TipoDocumentalMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TipoDocumentalDTO> findById(Long id) {
        return tipoDocumentalRepository.findById(id)
                .map(tipoDocumental -> {
                    if (isInativo(tipoDocumental)) {
                        throw new IllegalStateException("Tipo Documental está inativo.");
                    }
                    return TipoDocumentalMapper.INSTANCE.toDTO(tipoDocumental);
                });
    }

    @Override
    @Transactional
    public TipoDocumentalDTO save(TipoDocumentalDTO tipoDocumentalDTO, Long idUsuario) throws IllegalAccessException {
        String nomeDocumentoPadronizado = tipoDocumentalDTO.getNomeDocumento().trim().toLowerCase();

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
        if (usuarioOpt.isPresent() && usuarioOpt.get().getAdmin()) {
            TipoDocumental tipoDocumental;

            if (tipoDocumentalDTO.getId() != null) {
                tipoDocumental = tipoDocumentalRepository.findById(tipoDocumentalDTO.getId())
                        .orElseThrow(() -> new EntityNotFoundException("Tipo Documental não existe"));

                if (TIPOS_FIXOS.contains(tipoDocumental.getNomeDocumento().toLowerCase())) {
                    verificarTipoDocumentalFixo(tipoDocumentalDTO, tipoDocumental);
                } else {
                    atualizarTipoDocumental(tipoDocumentalDTO, tipoDocumental);
                }

            } else {
                if (tipoDocumentalRepository.existsByNomeDocumento(nomeDocumentoPadronizado)) {
                    throw new IllegalArgumentException("Já existe um tipo documental com o nome fornecido.");
                }
                tipoDocumental = TipoDocumentalMapper.INSTANCE.toEntity(tipoDocumentalDTO);
            }

            TipoDocumental savedTipoDocumental = tipoDocumentalRepository.save(tipoDocumental);
            return TipoDocumentalMapper.INSTANCE.toDTO(savedTipoDocumental);
        } else {
            throw new IllegalAccessException("Usuário não tem permissão para criar tipo documental");
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        TipoDocumental tipoDocumental = tipoDocumentalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("tipo documental não existe"));

        if (TIPOS_FIXOS.contains(tipoDocumental.getNomeDocumento().toLowerCase())) {
            throw new IllegalArgumentException("Este tipo documental não pode ser deletado, apenas inativado.");
        }

        desassociarDocumentos(tipoDocumental);
        tipoDocumentalRepository.deleteById(id);
    }

    private void desassociarDocumentos(TipoDocumental tipoDocumental) {
        List<Documento> documentos = documentoRepository.findByTipoDocumentalId(tipoDocumental.getId());
        for (Documento documento : documentos) {
            tipoDocumental.removeDocumento(documento);
        }
        tipoDocumentalRepository.save(tipoDocumental);
    }

    private void verificarTipoDocumentalFixo(TipoDocumentalDTO tipoDocumentalDTO, TipoDocumental tipoDocumental) {
        boolean statusAtual = tipoDocumental.isStatus(); 
        boolean statusNovo = tipoDocumentalDTO.getStatus(); 

        if (statusAtual != statusNovo) {
            tipoDocumental.setStatus(statusNovo);
        } else {
            throw new IllegalArgumentException("Este tipo documental não pode ser alterado, apenas ativado ou inativado.");
        }
    }

    private void atualizarTipoDocumental(TipoDocumentalDTO tipoDocumentalDTO, TipoDocumental tipoDocumental) {
        tipoDocumental.setNomeDocumento(tipoDocumentalDTO.getNomeDocumento());
        tipoDocumental.setLeiRegulamentadora(tipoDocumentalDTO.getLeiRegulamentadora());
        tipoDocumental.setStatus(tipoDocumentalDTO.getStatus());
        tipoDocumental.setTempoRetencao(tipoDocumentalDTO.getTempoRetencao());
    }

    private boolean isAtivo(TipoDocumental tipoDocumental) {
        return tipoDocumental.isStatus();
    }

    private boolean isInativo(TipoDocumental tipoDocumental) {
        return !tipoDocumental.isStatus();
    }



    @Override
    public TipoDocumentalDTO classificarDocumentoEVerificar(MultipartFile file) {
        String tipoDocumentalClassificado = classificarDocumentoNaAPI(file);
    
        Optional<TipoDocumental> tipoDocumentalOptional = tipoDocumentalRepository.findByNomeDocumento(tipoDocumentalClassificado);
    
        if (tipoDocumentalOptional.isPresent()) {
            TipoDocumental tipoDocumental = tipoDocumentalOptional.get();
    
            if (isInativo(tipoDocumental)) {
                throw new IllegalStateException("O Tipo Documental '" + tipoDocumentalClassificado + "' está inativo.");
            }
    
            return TipoDocumentalMapper.INSTANCE.toDTO(tipoDocumental);
        } else {
            throw new EntityNotFoundException("O Tipo Documental '" + tipoDocumentalClassificado + "' não existe no sistema.");
        }
    }

    public TipoDocumentalServiceImpl(RabbitTemplate rabbitTemplate, MessageConverter messageConverter) {
        this.rabbitTemplate = rabbitTemplate;
        this.messageConverter = messageConverter;
    }

    public String classificarDocumentoNaAPI(MultipartFile file){
        try {
            // Converter o arquivo MultipartFile para bytes e criar uma mensagem RabbitMQ
            byte[] fileBytes = file.getBytes();
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentType(MediaType.APPLICATION_PDF_VALUE);
            messageProperties.setHeader("filename", file.getOriginalFilename());// Nome do arquivo
            messageProperties.setReplyTo("documentRequestQueue");

            Message message = new Message(fileBytes, messageProperties);

            // Enviar a mensagem para a fila RabbitMQ e aguardar a resposta (RPC)
            Message responseMessage = rabbitTemplate.sendAndReceive("documentRequestQueue", message);
            System.out.println("Resposta da mensagem: " + responseMessage);

            if (responseMessage != null) {
                String response = new String(responseMessage.getBody());
                System.out.println(response);
                // Aqui você pode converter a resposta para o formato esperado, como JSON
                Map<String, String> responseBody = new ObjectMapper().readValue(response, Map.class);

                return responseBody.get("tipo_documental");
            } else {
                System.out.println("nulo");
                throw new RuntimeException("Erro ao receber resposta do serviço de classificação");
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler o arquivo ou processar resposta", e);
        }
    }



//    public String classificarDocumentoNaAPI(MultipartFile file) {
//        String apiUrl = "http://localhost:5000/classificar";
//
//        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
//        try {
//            multipartBodyBuilder.part("file", new ByteArrayResource(file.getBytes()) {
//                @Override
//                public String getFilename() {
//                    return file.getOriginalFilename();
//                }
//            }).contentType(MediaType.APPLICATION_PDF);
//        } catch (IOException e) {
//            throw new RuntimeException("Erro ao ler o arquivo", e);
//        }
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//        HttpEntity<MultiValueMap<String, HttpEntity<?>>> requestEntity = new HttpEntity<>(multipartBodyBuilder.build(), headers);
//
//        ResponseEntity<Map<String, String>> response = restTemplate.exchange(
//            apiUrl,
//            HttpMethod.POST,
//            requestEntity,
//            new ParameterizedTypeReference<Map<String, String>>() {}
//        );
//
//        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
//            return response.getBody().get("tipo_documental");
//        } else {
//            throw new RuntimeException("Erro ao classificar documento na API Flask: " + response.getStatusCode());
//        }



}