
def calculaSalarioMensal(salarioMensal, diasTrabalhados){
    return (salarioMensal / 30) * diasTrabalhados;
}

def calcula13(salarioMensal){
    return salarioMensal * 1.0012;
}
    
def calculaHoraExtra(salarioMensal, horasExtras){
    return ((salarioMensal / 30) / 8) * horasExtras;
}

def calculaSalario(repository, diasTrabalhados){
    return (repository.findByIdEqual(1).getSalario() / 30) * diasTrabalhados;
}