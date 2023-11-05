package com.example.portfolio.offering.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Could be more verbose to specify what entity wasn't found.
// Sometimes it may not be obvious.

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "No such entity found")
public class EntityNotFoundException extends RuntimeException {}
