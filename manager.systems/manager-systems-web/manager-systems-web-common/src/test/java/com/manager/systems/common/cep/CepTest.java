package com.manager.systems.common.cep;

import java.io.IOException;

public class CepTest 
{
    public static void main(final String[] args) throws IOException 
    {
        System.out.println(com.manager.systems.web.common.cep.correios.ClienteWs.getEnderecoPorCep("70002900"));
        System.out.println(com.manager.systems.web.common.cep.correios.ClienteWs.getMapPorCep("70002900"));

        System.out.println(com.manager.systems.web.common.cep.viacep.ClienteWs.getEnderecoPorCep("70002900"));
        System.out.println(com.manager.systems.web.common.cep.viacep.ClienteWs.getMapPorCep("70002900"));

        System.out.println(com.manager.systems.web.common.cep.postmon.ClienteWs.getEnderecoPorCep("69046380"));
        System.out.println(com.manager.systems.web.common.cep.postmon.ClienteWs.getMapPorCep("69046380"));
    }
}