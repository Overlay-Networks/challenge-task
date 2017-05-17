package com.uzh.csg.overlaynetworks.domain.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by yurybelevskiy on 17.05.17.
 */
@ResponseStatus(code = BAD_REQUEST)
public class MessageSendFailureException extends Exception {}
