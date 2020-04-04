package com.piisw.wozniak.lab3.exception;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

@ControllerAdvice
public class GlobalExceptionHandler {
        private static final Logger LOG = Logger.getLogger(GlobalExceptionHandler.class);

        @ExceptionHandler (HttpStatusCodeException.class)
        public final ResponseEntity<String> handleStatusCodeException(HttpStatusCodeException ex) {
                LOG.debug(ex.getMessage());
                return new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getResponseHeaders(), ex.getStatusCode());
        }

        @ExceptionHandler (ResourceAccessException.class)
        public final ResponseEntity<String> handleServerNotAvailableException(ResourceAccessException ex) {
                LOG.debug(ex.getMessage());
                return new ResponseEntity<>("Cannot establish connection", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @ExceptionHandler (Exception.class)
        public final ResponseEntity<String> handleUnknownException(Exception ex) {
                LOG.debug(ex.getMessage());
                return new ResponseEntity<>("UKNOWN ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }

}
