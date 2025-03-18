package ar.edu.unsam.algo2.obraSocial

import java.time.LocalDate

abstract class Afiliado {
    var nombre = "Juan Carlos"
    var edad = 21
    var atenciones = mutableListOf<Atencion>()

    // el monto de copago es cuánto debe pagar un afiliado por atenderse
    fun montoCopago() = 100 + this.montoEspecialCopago()
    abstract fun montoEspecialCopago(): Int

    fun atenderseCon(doctor: Doctor) {
        if (!doctor.puedeAtender(this)) {
            throw RuntimeException("El doctor no lo puede atender")
        }
        atenciones.add(Atencion(doctor, LocalDate.now(), this.montoCopago()))
     }
}

data class Atencion(var doctor: Doctor, var fecha: LocalDate, var montoCopago: Int)


class AfiliadoComun : Afiliado() {
    override fun montoEspecialCopago() = if (edad > 21) 10 else 20
}

class AfiliadoEspecial : Afiliado() {
    override fun montoEspecialCopago() = 5
}

class Doctor {
    lateinit var criterioAtencion: CriterioAtencion

    fun puedeAtender(afiliado: Afiliado) = criterioAtencion.puedeAtender(afiliado)
}

interface CriterioAtencion {
    fun puedeAtender(afiliado: Afiliado): Boolean
}

class EligeGenteGrande : CriterioAtencion {
    override fun puedeAtender(afiliado: Afiliado) = afiliado.edad > 50
}

class EligeGenteConNombreLargo : CriterioAtencion {
    override fun puedeAtender(afiliado: Afiliado) = afiliado.nombre.length > 40
}

class Combinado(var criteriosAtencion: MutableList<CriterioAtencion> = mutableListOf()) : CriterioAtencion {
    override fun puedeAtender(afiliado: Afiliado) = criteriosAtencion.all { it.puedeAtender(afiliado) }
}
