package com.manager.systems.web.common.cep.utils;

public class Util 
{
    public static boolean validaCep(String cep) 
    {
    	boolean result = true;
        if (!cep.matches("\\d{8}")) 
        {
            result = false;
        }
        return result;
    }
}