package dev.alexandre.business;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.jpa.impl.JPAQuery;

import dev.alexandre.abstracts.AbstractCadManagerBean;
import dev.alexandre.dto.CampoErroDTO;
import dev.alexandre.dto.RegistroDTO;
import dev.alexandre.entity.Agenda;
import dev.alexandre.entity.QAgenda;
import dev.alexandre.entity.QPaciente;

@RequestScoped
public class AgendaManagerBean extends AbstractCadManagerBean<Agenda> {

	@Inject
	private EntityManager em;

	@Inject
	private PacienteManagerBean pacienteManagerBean;
	
	private QAgenda qAgenda;
	private QPaciente qPaciente;

	private JPAQuery<Agenda> query;

	private ConstructorExpression<Agenda> projecao() {
		return QAgenda.create(qAgenda.id, qAgenda.dataHoraInicio, qAgenda.dataHoraFim, qAgenda.valor,
				qAgenda.idPaciente, QPaciente.create(qPaciente.nome), qAgenda.pagamento);
	}
	
	private ConstructorExpression<Agenda> projecaoSimple() {
		return QAgenda.create(qAgenda.id, qAgenda.dataHoraInicio, qAgenda.dataHoraFim, qAgenda.valor,
				qAgenda.idPaciente, qAgenda.pagamento);
	}

	public List<Agenda> findAll() {
		this.query = new JPAQuery<>(em);
		this.qAgenda = QAgenda.agenda;
		this.qPaciente = QPaciente.paciente;

		return this.query.select(this.projecao()).from(qAgenda)
				.leftJoin(qAgenda.paciente, qPaciente)
				.fetch();
	}

	public List<Agenda> findByFilter(RegistroDTO<Agenda> dto) {
		this.query = new JPAQuery<>(em);
		this.qAgenda = QAgenda.agenda;
		this.qPaciente = QPaciente.paciente;

		this.query.select(this.projecao()).from(qAgenda)
				.leftJoin(qAgenda.paciente, qPaciente);
		if (dto.registro.getDataHoraInicio() != null) {
			this.query.where(qAgenda.dataHoraInicio.eq(dto.registro.getDataHoraInicio()));
		}
		if (dto.registro.getDataHoraFim() != null) {
			this.query.where(qAgenda.dataHoraFim.eq(dto.registro.getDataHoraFim()));
		}
		if (dto.registro.getIdPaciente() != null) {
			this.query.where(qAgenda.idPaciente.eq(dto.registro.getIdPaciente()));
		}
		if (dto.registro.getPagamento() != null) {
			this.query.where(qAgenda.pagamento.eq(dto.registro.getPagamento()));
		}

		return this.query.orderBy(qAgenda.dataHoraInicio.asc()).fetch();
	}
	
	public Agenda find(Integer id) {
		this.query = new JPAQuery<>(em);
		this.qAgenda = QAgenda.agenda;

		return this.query.select(this.projecaoSimple())
				.from(qAgenda)
				.where(qAgenda.id.eq(id))
				.fetchOne();
	}

	public boolean existsAgenda(Agenda registro) {
		this.query = new JPAQuery<>(em);
		this.qAgenda = QAgenda.agenda;

		this.query.select(qAgenda.id)
			.from(qAgenda)
			.where(qAgenda.dataHoraInicio.lt(registro.getDataHoraFim())
					.and(qAgenda.dataHoraFim.gt(registro.getDataHoraInicio())));
			if (registro.getId() != null) {
				this.query.where(qAgenda.id.ne(registro.getId()));
			}

		return null != this.query.fetchFirst();
	}

	public boolean existsAgenda(Integer idPaciente) {
		this.query = new JPAQuery<>(em);
		this.qAgenda = QAgenda.agenda;

		return null != this.query.select(qAgenda.id)
				.from(qAgenda)
				.where(qAgenda.idPaciente.eq(idPaciente))
		 		.fetchFirst();
	}

	@Override
	public List<CampoErroDTO> validar(RegistroDTO<Agenda> dto) {
		List<CampoErroDTO> erros = new ArrayList<>();

		this.validaDataHoraInicio(dto, erros);
		this.validaDataHoraFim(dto, erros);
		this.validaValor(dto, erros);
		this.validaPaciente(dto, erros);
		this.validaHorarioAgenda(dto, erros);

		return erros;
	}

	private void validaDataHoraInicio(RegistroDTO<Agenda> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getDataHoraInicio() == null) {
			erros.add(new CampoErroDTO("dataHoraInicio", "Campo obrigatório"));
		}
	}
	
	private void validaDataHoraFim(RegistroDTO<Agenda> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getDataHoraFim() == null) {
			erros.add(new CampoErroDTO("dataHoraFim", "Campo obrigatório"));
		} else if (dto.registro.getDataHoraFim().isBefore(dto.registro.getDataHoraInicio())) {
			erros.add(new CampoErroDTO("dataHoraFim", "Data final maior que a data de início"));
		}
	}
	
	private void validaValor(RegistroDTO<Agenda> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getValor() == null) {
			erros.add(new CampoErroDTO("valor", "Campo obrigatório"));
		} else if (dto.registro.getValor() <= 0) {
			erros.add(new CampoErroDTO("valor", "Valor inválido"));
		}
	}

	private void validaPaciente(RegistroDTO<Agenda> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getIdPaciente() == null) {
			erros.add(new CampoErroDTO("idPaciente", "Campo obrigatório"));
		} else if (!this.pacienteManagerBean.exists(dto.registro.getIdPaciente())) {
			erros.add(new CampoErroDTO("idPaciente", "Paciente não cadastrado"));
		}
	}
	
	private void validaPagamento(RegistroDTO<Agenda> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getPagamento() == 1) {
			erros.add(new CampoErroDTO("pagamento", "Agenda já está paga"));
		}
	}

	private void validaHorarioAgenda(RegistroDTO<Agenda> dto, List<CampoErroDTO> erros) {
		if (dto.registro.getDataHoraInicio() != null && dto.registro.getDataHoraFim() != null) {
			if (this.existsAgenda(dto.registro)) {
				erros.add(new CampoErroDTO("dataHora", "Já existe agenda nesse horário"));
			}
		}
	}

	@Override
	public List<CampoErroDTO> validaDeletar(List<Agenda> registros) {
		List<CampoErroDTO> erros = new ArrayList<>();
		return erros;
	}
	
	@Transactional
    public List<CampoErroDTO> pagamento(RegistroDTO<Agenda> dto) throws Exception {
		List<CampoErroDTO> erros = new ArrayList<CampoErroDTO>();
		dto.registro = this.find(dto.registro.getId());
		if (dto.registro != null) {
			this.validaPagamento(dto, erros);
			if (erros.isEmpty()) {
				dto.registro.setPagamento(1);
				erros = this.validar(dto);
		        if (erros.isEmpty()) {
		            dto.registro = this.getEm().merge(dto.registro);
		        }
			}
		} else {
			erros.add(new CampoErroDTO("id", "Agenda não encontrada"));
		}

        return erros;
    }
	
	@Override
	public EntityManager getEm() {
		return em;
	}

	@Override
	public Class<Agenda> getTypeParameterClass() {
		return Agenda.class;
	}

}