package com.pettycash.service;

import com.pettycash.dto.LoginDTO;
import com.pettycash.dto.TokenDTO;
import javassist.NotFoundException;

public interface LoginService {
   TokenDTO login(LoginDTO loginDTO) throws NotFoundException;
}
