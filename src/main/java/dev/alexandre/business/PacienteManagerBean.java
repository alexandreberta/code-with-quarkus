package dev.alexandre.business;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.jpa.impl.JPAQuery;

import dev.alexandre.abstracts.AbstractCadManagerBean;
import dev.alexandre.dto.CampoErroDTO;
import dev.alexandre.dto.RegistroDTO;
import dev.alexandre.entity.Paciente;
import dev.alexandre.entity.QPaciente;

@RequestScoped
public class PacienteManagerBean extends AbstractCadManagerBean<Paciente> {

	@Inject
	private EntityManager em;

	@Inject
	private AgendaManagerBean agendaManagerBean;

	private QPaciente qPaciente;

	private JPAQuery<Paciente> query;

	private ConstructorExpression<Paciente> projecao() {
		return QPaciente.create(qPaciente.id, qPaciente.nome, qPaciente.sexo, qPaciente.dataNascimento,
				qPaciente.dddTelefone, qPaciente.telefone, qPaciente.cpf);
	}

	public List<Paciente> findAll() {
		this.query = new JPAQuery<>(em);
		this.qPaciente = QPaciente.paciente;

		return this.query.select(this.projecao()).from(qPaciente).fetch();
	}
	
	public List<Paciente> findByNome(RegistroDTO<Paciente> dto) {
		this.query = new JPAQuery<>(em);
		this.qPaciente = QPaciente.paciente;
				
		this.query.select(this.projecao()).from(qPaciente);
		if (dto.registro.getNome() != null) {
			this.query.where(qPaciente.nome.eq(dto.registro.getNome()));
		}
			
		return this.query.fetch();
	}

	public boolean exists(Integer id) {
		this.query = new JPAQuery<>(em);
		this.qPaciente = QPaciente.paciente;

		return null != this.query.select(qPaciente.id)
				.from(qPaciente)
				.where(qPaciente.id.eq(id))
				.fetchOne();
	}

	public boolean existsCpf(Paciente paciente) {
		this.query = new JPAQuery<>(em);
		this.qPaciente = QPaciente.paciente;

		this.query.select(qPaciente.id)
				.from(qPaciente)
				.where(qPaciente.cpf.eq(paciente.getCpf()));
		if (paciente.getId() != null) {
			this.query.where(qPaciente.id.ne(paciente.getId()));
		}

		return null != this.query.fetchOne();
	}

	@Override
	public List<CampoErroDTO> validar(RegistroDTO<Paciente> dto) {
		List<CampoErroDTO> erros = new ArrayList<>();

		this.validaNome(dto, erros);
		this.validaUnique(dto, erros);

		return erros;
	}

	private void validaNome(RegistroDTO<Paciente> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getNome() == null || dto.registro.getNome().isEmpty()) {
			erros.add(new CampoErroDTO("nome", "Campo obrigatório"));
		}
	}

	private void validaUnique(RegistroDTO<Paciente> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getCpf() != null || !dto.registro.getCpf().equals("")) {
			if (this.existsCpf(dto.registro)) {
				erros.add(new CampoErroDTO("cpf", "CPF já cadastrado"));
			}
		}
	}

	@Override
	public List<CampoErroDTO> validaDeletar(List<Paciente> registros) {
		List<CampoErroDTO> erros = new ArrayList<>();

		for (Paciente registro : registros) {
			this.validaAgenda(registro, erros);
		}

		return erros;
	}

	private void validaAgenda(Paciente paciente, List<CampoErroDTO> erros) {
		if (this.agendaManagerBean.existsAgenda(paciente.getId())) {
			erros.add(new CampoErroDTO("id", "Paciente possui agenda"));
		}
	}

	@Override
	public EntityManager getEm() {
		return em;
	}

	@Override
	public Class<Paciente> getTypeParameterClass() {
		return Paciente.class;
	}

}