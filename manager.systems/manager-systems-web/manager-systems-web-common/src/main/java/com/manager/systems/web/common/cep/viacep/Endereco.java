package com.manager.systems.web.common.cep.viacep;

import com.manager.systems.common.utils.StringUtils;

/**
 * Entidade baseada nos dados do WS do viacep.com
 */
public class Endereco {

    private String cep;
    private String logradouro;
    private String complemento;
    private String bairro;
    private String localidade;
    private String uf;
    private String estadoIbge;
    private String unidade;
    private String ibge;
    private String gia;

    public String getCep() 
    {
        return this.cep;
    }

    public String getLogradouro()
    {
        return this.logradouro;
    }

    public String getComplemento()
    {
        return this.complemento;
    }

    public String getBairro() 
    {
        return this.bairro;
    }

    public String getLocalidade() 
    {
        return this.localidade;
    }

    public String getUf()
    {
        return this.uf;
    }

    public String getUnidade() 
    {
        return this.unidade;
    }

    public String getIbge() 
    {
        return this.ibge;
    }

    public String getGia() 
    {
        return this.gia;
    }

    public Endereco setCep(final String cep)
    {
        this.cep = cep;
        return this;
    }

    public Endereco setLogradouro(final String logradouro) 
    {
        this.logradouro = logradouro;
        return this;
    }

    public Endereco setComplemento(final String complemento)
    {
        this.complemento = complemento;
        if(StringUtils.isNull(this.complemento))
        {
        	this.complemento = "A";
        }
        return this;
    }

    public Endereco setBairro(final String bairro) 
    {
        this.bairro = bairro;
        return this;
    }

    public Endereco setLocalidade(final String localidade)
    {
        this.localidade = localidade;
        return this;
    }

    public Endereco setUf(final String uf) 
    {
        this.uf = uf;
        return this;
    }

    public Endereco setUnidade(final String unidade)
    {
        this.unidade = unidade;
        return this;
    }

    public Endereco setIbge(final String ibge) 
    {
        this.ibge = ibge;
        return this;
    }

    public Endereco setGia(final String gia)
    {
        this.gia = gia;
        return this;
    }

    public final String getEstadoIbge() 
    {
		return this.estadoIbge;
	}

	public final void setEstadoIbge(final String estadoIbge) 
	{
		this.estadoIbge = estadoIbge;
	}

	@Override
    public String toString() 
    {
        return "Endereco{" +
                "cep='" + cep + '\'' +
                ", logradouro='" + logradouro + '\'' +
                ", complemento='" + complemento + '\'' +
                ", bairro='" + bairro + '\'' +
                ", localidade='" + localidade + '\'' +
                ", uf='" + uf + '\'' +
                ", estadoIbge='" + estadoIbge + '\'' +
                ", unidade='" + unidade + '\'' +
                ", ibge='" + ibge + '\'' +
                ", gia='" + gia + '\'' +
                '}';
    }
}
