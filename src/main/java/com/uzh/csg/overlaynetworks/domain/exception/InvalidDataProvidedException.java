package com.uzh.csg.overlaynetworks.domain.exception;

import static org.springframework.http.HttpStatus.FORBIDDEN;

import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(code = FORBIDDEN)
public class InvalidDataProvidedException extends RuntimeException {

}
