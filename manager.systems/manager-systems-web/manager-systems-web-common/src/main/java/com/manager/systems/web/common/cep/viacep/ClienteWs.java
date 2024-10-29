
package com.manager.systems.web.common.cep.viacep;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.manager.systems.web.common.cep.utils.Util;

/**
 * Classe para recuperar informações do WS do viacep.com.br
 */
public class ClienteWs 
{
    /**
     * Recupera objeto Endereco pelo CEP
     * @param cep String no formato 00000000
     * @return instancia de br.com.viacep.Endereco
     */
    public static Endereco getEnderecoPorCep(final String cep) 
    {
        final JsonObject jsonObject = getCepResponse(cep);
        Endereco endereco = null;

        JsonValue erro = jsonObject.get("erro");

        if (erro == null) 
        {
            endereco = new Endereco()
                    .setCep(jsonObject.getString("cep"))
                    .setLogradouro(jsonObject.getString("logradouro").toUpperCase())
                    .setComplemento(jsonObject.getString("complemento").toUpperCase())
                    .setBairro(jsonObject.getString("bairro").toUpperCase())
                    .setLocalidade(jsonObject.getString("localidade").toUpperCase())
                    .setUf(jsonObject.getString("uf").toUpperCase())
                    //.setUnidade(jsonObject.getString("unidade").toUpperCase())
                    .setIbge(jsonObject.getString("ibge"))
                    .setGia(jsonObject.getString("gia"));

        }
        
        return endereco;
    }

    /**
     * Recupera Map<String,String> pelo CEP
     * @param cep String no formato 00000000
     * @return instancia de Map<String,String>
     */
    public static Map<String, String> getMapPorCep(final String cep) 
    {
        final JsonObject jsonObject = getCepResponse(cep);
        JsonValue erro = jsonObject.get("erro");

        Map<String, String> mapa = null;
        if (erro == null) {
            mapa = new HashMap<String, String>();

            for (final Iterator<Map.Entry<String,JsonValue>> it = jsonObject.entrySet().iterator(); it.hasNext();) 
            {
                final Map.Entry<String,JsonValue> entry = it.next();
                mapa.put(entry.getKey(), entry.getValue().toString());
            }
        }

        return mapa;
    }

    private static JsonObject getCepResponse(final String cep) 
    {
        JsonObject responseJO = null;

        try 
        {
            if (!Util.validaCep(cep)) 
            {
                throw new RuntimeException("Formato de CEP inválido");
            }

      		final CloseableHttpClient httpclient = HttpClients.createDefault();
            final HttpGet httpGet = new HttpGet("https://viacep.com.br/ws/"+cep+"/json");
            final HttpResponse response = httpclient.execute(httpGet);

            final HttpEntity  entity = response.getEntity();

            responseJO = Json.createReader(entity.getContent()).readObject();
        } 
        catch (final Exception e) 
        {
            throw new RuntimeException(e);
        }

        return responseJO;
    }
}

