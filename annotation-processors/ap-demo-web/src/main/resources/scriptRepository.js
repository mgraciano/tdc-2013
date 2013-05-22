
function testPessoaRepository(repository){
    return repository.findByIdEqual(1).getNome();
}