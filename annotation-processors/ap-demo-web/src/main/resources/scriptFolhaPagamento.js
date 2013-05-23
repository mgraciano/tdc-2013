

function calculaSalarioMensal(salarioMensal, diasTrabalhados){
    return (salarioMensal / 30) * diasTrabalhados;
}

function calcula13(salarioMensal){
    return salarioMensal * 1.0012;
}
    
function calculaHoraExtra(salarioMensal, horasExtras){
    return ((salarioMensal / 30) / 8) * horasExtras;
}