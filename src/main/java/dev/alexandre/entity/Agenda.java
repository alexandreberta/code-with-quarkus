package dev.alexandre.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.querydsl.core.annotations.QueryProjection;

import dev.alexandre.converters.jackson.LocalDateTimeDeserializer;
import dev.alexandre.converters.jackson.LocalDateTimeSerializer;
import dev.alexandre.interfaces.Registro;	

@Table(name="agenda")
@Entity
public class Agenda implements Registro<Integer> {
	
	@Id
	@TableGenerator(name = "AgendaIdGenerator", table = "id_tabelas", pkColumnName = "tabela", pkColumnValue = "agenda", valueColumnName = "id", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "AgendaIdGenerator")
	@Column(name="id")
	private Integer id;
		
	@Column(name = "dt_inicio")
	@JsonInclude(value = Include.NON_NULL)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime dataHoraInicio;
	
	@Column(name = "dt_fim")
	@JsonInclude(value = Include.NON_NULL)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime dataHoraFim;
	
	@Column(name="valor")
	@JsonInclude(value = Include.NON_NULL)
	private Double valor;
	
	@Column(name="id_paciente")
	@JsonInclude(value = Include.NON_NULL)
	private Integer idPaciente;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "id_paciente", referencedColumnName = "id", insertable = false, updatable = false)
    })
    @JsonInclude(value = Include.NON_NULL)
    private Paciente paciente;
	
	@Column(name="pagamento")
	@JsonInclude(value = Include.NON_NULL)
	private Integer pagamento;

	public Agenda(){
		
	}

	@QueryProjection
	public Agenda(Integer id, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim, Double valor,
			Integer idPaciente, Paciente paciente, Integer pagamento) {
		this.id = id;
		this.dataHoraInicio = dataHoraInicio;
		this.dataHoraFim = dataHoraFim;
		this.valor = valor;
		this.idPaciente = idPaciente;
		this.paciente = paciente;
		this.pagamento = pagamento;
	}
	
	@QueryProjection
	public Agenda(Integer id, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim, Double valor,
			Integer idPaciente, Integer pagamento) {
		this.id = id;
		this.dataHoraInicio = dataHoraInicio;
		this.dataHoraFim = dataHoraFim;
		this.valor = valor;
		this.idPaciente = idPaciente;
		this.pagamento = pagamento;
	}	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}	

	public LocalDateTime getDataHoraInicio() {
		return dataHoraInicio;
	}

	public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
		this.dataHoraInicio = dataHoraInicio;
	}

	public LocalDateTime getDataHoraFim() {
		return dataHoraFim;
	}

	public void setDataHoraFim(LocalDateTime dataHoraFim) {
		this.dataHoraFim = dataHoraFim;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Integer getIdPaciente() {
		return idPaciente;
	}

	public void setIdPaciente(Integer idPaciente) {
		this.idPaciente = idPaciente;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public Integer getPagamento() {
		return pagamento;
	}

	public void setPagamento(Integer pagamento) {
		this.pagamento = pagamento;
	}

}