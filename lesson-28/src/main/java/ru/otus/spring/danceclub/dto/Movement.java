package ru.otus.spring.danceclub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static ru.otus.spring.danceclub.dto.Style.SALSA;
import static ru.otus.spring.danceclub.dto.Style.TANGO;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum Movement {
    GUAPEA(SALSA),
    SACALA(SALSA),
    DILE_QUE_NO(SALSA),
    DILE_QUE_SI(SALSA),
    ENCHUFLA(SALSA),
    VASILALA(SALSA),
    SOMBRERO(SALSA),
    PASEALA(SALSA),
    ENROSQUE(TANGO),
    GANCHO(TANGO),
    CORRIDA(TANGO),
    OCHO(TANGO),
    SALIDA(TANGO),
    CRUZAR_CUNITA(TANGO);

    private Style style;

}
