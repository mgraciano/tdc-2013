package tdc2013.hibernate.model;

import java.util.Collection;

import tdc2013.repository.Repository;

/**
 * 
 * @author klaus.boeing
 */
@Repository
public interface PessoaRepository {

	public Pessoa findByIdEqual(Long id);

	public Pessoa findByNomeLike(String nome);

	public Pessoa findBySexoEqual(Sexo sexo);

	public Collection<Pessoa> findBySexoEqualAndNameLike(Sexo sexo, String name);

}
