package dev.alexandre.abstracts;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import dev.alexandre.dto.CampoErroDTO;
import dev.alexandre.dto.RegistroDTO;
import dev.alexandre.interfaces.Registro;

public abstract class AbstractCadManagerBean<T extends Registro<?>> {
    	
    @Transactional
    public List<CampoErroDTO> salvar(final RegistroDTO<T> dto) throws Exception {
        List<CampoErroDTO> erros = this.validar(dto);
        if (erros.isEmpty()) {
            this.getEm().persist(dto.registro);
        }

        return erros;
    }

    @Transactional
    public List<CampoErroDTO> atualizar(final RegistroDTO<T> dto) throws Exception {
        List<CampoErroDTO> erros = this.validar(dto);
        if (erros.isEmpty()) {
            dto.registro = this.getEm().merge(dto.registro);
        }
        return erros;
    }

    @Transactional
    public List<CampoErroDTO> deletar(final List<T> registros) throws Exception {
        final List<CampoErroDTO> erros = this.validaDeletar(registros);
        if (erros.isEmpty()) {
            for (final T registro : registros) {
                this.getEm().remove(this.getEm().find(this.getTypeParameterClass(), registro.getId()));
            }
        }
        return erros;
    }

    public abstract List<CampoErroDTO> validar(RegistroDTO<T> registro);

    public abstract List<CampoErroDTO> validaDeletar(List<T> registros);

    public abstract EntityManager getEm();

    public abstract Class<T> getTypeParameterClass();

}