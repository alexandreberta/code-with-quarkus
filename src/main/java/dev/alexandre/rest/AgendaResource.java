package dev.alexandre.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.alexandre.business.AgendaManagerBean;
import dev.alexandre.dto.CampoErroDTO;
import dev.alexandre.dto.ListRegistroDTO;
import dev.alexandre.dto.LoadResponseDTO;
import dev.alexandre.dto.RegistroDTO;
import dev.alexandre.dto.ResponseDTO;
import dev.alexandre.entity.Agenda;
import io.quarkus.logging.Log;

@Path("agenda")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AgendaResource {

    private static final Logger LOG = LoggerFactory.getLogger(AgendaResource.class);

    @Inject
    private AgendaManagerBean managerBean;


    public AgendaResource() {
    	
    }

    public AgendaResource(AgendaManagerBean managerBean) {
        this.managerBean = managerBean;
    }
        
    @GET
	@Path("/load")
	public Response loadAll() {
		LoadResponseDTO<Agenda> dto = new LoadResponseDTO<Agenda>();
		try {
			List<Agenda> registros = this.managerBean.findAll();
			dto.registros = registros;
		} catch (Exception e) {
			dto.success = false;
			dto.msg = "Falha ao buscar dados das agendas";
			Log.error("Falha ao buscar dados das agendas", e);
		}
		return Response.status(dto.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
				.entity(dto).type(MediaType.APPLICATION_JSON).build();

	}

    @POST
    @Path("/load/filter")
    public Response loadFilter(RegistroDTO<Agenda> dto) {
        LoadResponseDTO<Agenda> response = new LoadResponseDTO<Agenda>();
        try {
            List<Agenda> registros = this.managerBean.findByFilter(dto);
            response.registros = registros;
        } catch (Exception e) {
            response.success = false;
            response.msg = "Falha ao buscar dados das agenda";
            Log.error("Falha ao buscar dados das agendas", e);
        }
        return Response.status(response.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
                .entity(response).type(MediaType.APPLICATION_JSON).build();

    }

    @POST
    @Path("/save")	
    public Response save(RegistroDTO<Agenda> dto) {
        ResponseDTO<Agenda> response = new ResponseDTO<>();

        try {
            List<CampoErroDTO> errors = this.managerBean.salvar(dto);
            if (errors.isEmpty()) {
                response.registro = dto.registro;
            } else {
                response.success = false;
                response.errors = errors;
            }

        } catch (Exception e) {
            response.success = false;
            response.msg = "Falha ao salvar dados da agenda";
            LOG.error("Falha ao salvar dados da agenda", e);
        }

        return Response.status(response.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
                .entity(response).type(MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/update")
    public Response update(RegistroDTO<Agenda> dto) {
        ResponseDTO<Agenda> response = new ResponseDTO<>();

        try {
            List<CampoErroDTO> errors = this.managerBean.atualizar(dto);
            if (errors.isEmpty()) {
                response.registro = dto.registro;
            } else {
                response.success = false;
                response.errors = errors;
            }
        } catch (Exception e) {
            response.success = false;
            response.msg = "Falha ao atualizar dados da agenda";
            LOG.error("Falha ao atualizar dados da agenda", e);
        }

        return Response.status(response.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
                .entity(response).type(MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/delete")
    public Response delete(ListRegistroDTO<Agenda> dto) {
        ResponseDTO<Agenda> response = new ResponseDTO<>();
        try {            
            List<CampoErroDTO> errors = this.managerBean.deletar(dto.registros);
            if (errors.isEmpty()) {
                response.success = true;
            } else {
                response.success = false;
                response.errors = errors;
            }
        } catch (Exception e) {            
            response.success = false;
            response.msg = "Falha ao excluir dados da agenda";
            LOG.error("Falha ao excluir dados da agenda", e);
        }

        return Response.status(response.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
                .entity(response).type(MediaType.APPLICATION_JSON).build();
    }
    
    @POST
    @Path("/pay")
    public Response pagamento(RegistroDTO<Agenda> dto) {
        ResponseDTO<Agenda> response = new ResponseDTO<>();

        try {
            List<CampoErroDTO> errors = this.managerBean.pagamento(dto);
            if (errors.isEmpty()) {
                response.registro = dto.registro;
            } else {
                response.success = false;
                response.errors = errors;
            }
        } catch (Exception e) {
            response.success = false;
            response.msg = "Falha ao atualizar pagamento da agenda";
            LOG.error("Falha ao atualizar pagamento da agenda", e);
        }

        return Response.status(response.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
                .entity(response).type(MediaType.APPLICATION_JSON).build();
    }

}
