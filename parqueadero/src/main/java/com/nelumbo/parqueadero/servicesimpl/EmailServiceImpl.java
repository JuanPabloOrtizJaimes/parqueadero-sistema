package com.nelumbo.parqueadero.servicesimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.nelumbo.parqueadero.dtos.MensajeResponse;
import com.nelumbo.parqueadero.exceptions.EmailException;
import com.nelumbo.parqueadero.requests.EmailRequest;
import com.nelumbo.parqueadero.services.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Value("${email.simulado.api.url}")
    private String apiCorreUrl;

    @Override
    public MensajeResponse enviarCorreo(EmailRequest emailRequest) {
        try {
            logger.info("Enviando correo a través de la API: {}", apiCorreUrl);

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<EmailRequest> request = new HttpEntity<>(emailRequest, headers);

            ResponseEntity<MensajeResponse> response = restTemplate.exchange(
                    apiCorreUrl,
                    HttpMethod.POST,
                    request,
                    MensajeResponse.class
            );

            logger.info("Correo enviado exitosamente: {}", emailRequest.getEmail());
            return response.getBody();

        } catch (HttpClientErrorException e) {
            logger.error("Error del cliente al enviar correo: {}", e.getResponseBodyAsString(), e);
            throw new EmailException("Error al procesar la solicitud de correo: " + e.getMessage());
        } catch (HttpServerErrorException e) {
            logger.error("Error del servidor al enviar correo: {}", e.getResponseBodyAsString(), e);
            throw new EmailException("Error en el servidor al enviar correo: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al enviar correo: {}", e.getMessage(), e);
            throw new EmailException("Error inesperado al enviar correo");
        }
    }
}
