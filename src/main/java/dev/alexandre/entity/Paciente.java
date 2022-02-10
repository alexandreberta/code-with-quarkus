package dev.alexandre.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.querydsl.core.annotations.QueryProjection;

import dev.alexandre.converters.jackson.LocalDateDeserializer;
import dev.alexandre.converters.jackson.LocalDateSerializer;
import dev.alexandre.interfaces.Registro;	

@Table(name="paciente")
@Entity
public class Paciente implements Registro<Integer> {
	
	@Id
	@TableGenerator(name = "PacienteIdGenerator", table = "id_tabelas", pkColumnName = "tabela", pkColumnValue = "paciente", valueColumnName = "id", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "PacienteIdGenerator")
	@Column(name="id")
	@JsonInclude(value = Include.NON_NULL)
	private Integer id;
		
	@Column(name="nome")
	@JsonInclude(value = Include.NON_NULL)
	private String nome;
	
	@Column(name="sexo")
	@JsonInclude(value = Include.NON_NULL)
	private String sexo;
	
	@Column(name = "dt_nascimento")
    @JsonInclude(value = Include.NON_NULL)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dataNascimento;
	
	@Column(name="ddd_telefone")
	@JsonInclude(value = Include.NON_NULL)
	private Integer dddTelefone;
		
	@Column(name="telefone")
	@JsonInclude(value = Include.NON_NULL)
	private Integer telefone;
	
	@Column(name="cpf")
	@JsonInclude(value = Include.NON_NULL)
	private String cpf;

	public Paciente(){
		
	}
	
	@QueryProjection
	public Paciente(String nome) {
		this.nome = nome;
	}	

	@QueryProjection
	public Paciente(Integer id, String nome, String sexo, LocalDate dataNascimento,
			Integer dddTelefone, Integer telefone, String cpf) {
		this.id = id;
		this.nome = nome;
		this.sexo = sexo;
		this.dataNascimento = dataNascimento;
		this.dddTelefone = dddTelefone;
		this.telefone = telefone;
		this.cpf = cpf;
	}	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Integer getDddTelefone() {
		return dddTelefone;
	}

	public void setDddTelefone(Integer dddTelefone) {
		this.dddTelefone = dddTelefone;
	}

	public Integer getTelefone() {
		return telefone;
	}

	public void setTelefone(Integer telefone) {
		this.telefone = telefone;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}	

}