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

import dev.alexandre.business.PacienteManagerBean;
import dev.alexandre.dto.CampoErroDTO;
import dev.alexandre.dto.ListRegistroDTO;
import dev.alexandre.dto.LoadResponseDTO;
import dev.alexandre.dto.RegistroDTO;
import dev.alexandre.dto.ResponseDTO;
import dev.alexandre.entity.Paciente;
import io.quarkus.logging.Log;

@Path("paciente")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PacienteResource {

    private static final Logger LOG = LoggerFactory.getLogger(PacienteResource.class);

    @Inject
    private PacienteManagerBean managerBean;


    public PacienteResource() {
    	
    }

    public PacienteResource(PacienteManagerBean managerBean) {
        this.managerBean = managerBean;
    }
        
    @GET
	@Path("/load")
	public Response loadAll() {
		LoadResponseDTO<Paciente> dto = new LoadResponseDTO<Paciente>();
		try {
			List<Paciente> registros = this.managerBean.findAll();
			dto.registros = registros;
		} catch (Exception e) {
			dto.success = false;
			dto.msg = "Falha ao buscar dados dos pacientes";
			Log.error("Falha ao buscar dados dos pacientes", e);
		}
		return Response.status(dto.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
				.entity(dto).type(MediaType.APPLICATION_JSON).build();
	}
    
    @POST
    @Path("/load/filter")	
    public Response loadFilter(RegistroDTO<Paciente> dto) {
        LoadResponseDTO<Paciente> response = new LoadResponseDTO<Paciente>();
		try {
			List<Paciente> registros = this.managerBean.findByNome(dto);
			response.registros = registros;
		} catch (Exception e) {
			response.success = false;
			response.msg = "Falha ao buscar dados dos pacientes";
			Log.error("Falha ao buscar dados dos pacientes", e);
		}
		return Response.status(response.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
				.entity(response).type(MediaType.APPLICATION_JSON).build();

    }
    
    @POST
    @Path("/save")	
    public Response save(RegistroDTO<Paciente> dto) {
        ResponseDTO<Paciente> response = new ResponseDTO<>();

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
            response.msg = "Falha ao salvar dados do paciente";
            LOG.error("Falha ao salvar dados do paciente", e);
        }

        return Response.status(response.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
                .entity(response).type(MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/update")
    public Response update(RegistroDTO<Paciente> dto) {
        ResponseDTO<Paciente> response = new ResponseDTO<>();

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
            response.msg = "Falha ao atualizar dados do paciente";
            LOG.error("Falha ao atualizar dados do paciente", e);
        }

        return Response.status(response.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
                .entity(response).type(MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/delete")
    public Response delete(ListRegistroDTO<Paciente> dto) {
        ResponseDTO<Paciente> response = new ResponseDTO<>();
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
            response.msg = "Falha ao excluir dados do paciente";
            LOG.error("Falha ao excluir dados do paciente", e);
        }

        return Response.status(response.success ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
                .entity(response).type(MediaType.APPLICATION_JSON).build();
    }

}
