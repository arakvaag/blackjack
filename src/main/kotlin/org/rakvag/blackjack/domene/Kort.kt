package org.rakvag.blackjack.domene

class Kort {

    constructor(farge: Farge, verdi: Verdi) {
        this.farge = farge
        this.verdi = verdi
    }

    constructor(kortStreng: String) {
        require(kortStreng.length in setOf(2, 3)) {
            "Parameter må være to- eller tre-karakters string med farge og verdi"
        }
        val farge = when (kortStreng.toCharArray()[0]) {
            'K' -> Farge.KLØVER
            'R' -> Farge.RUTER
            'H' -> Farge.HJERTER
            'S' -> Farge.SPAR
            else -> throw RuntimeException("$kortStreng har ikke en gyldig bokstav for kort-farge")
        }
        val verdi = when (kortStreng.substring(1)) {
            "A" -> Verdi.ESS
            "2" -> Verdi.TO
            "3" -> Verdi.TRE
            "4" -> Verdi.FIRE
            "5" -> Verdi.FEM
            "6" -> Verdi.SEKS
            "7" -> Verdi.SJU
            "8" -> Verdi.ÅTTE
            "9" -> Verdi.NI
            "10" -> Verdi.TI
            "J" -> Verdi.KNEKT
            "Q" -> Verdi.DAME
            "K" -> Verdi.KONGE
            else -> throw RuntimeException("$kortStreng har ikke gyldig(e) bokstav(er) for kort-verdi")
        }
        this.farge = farge
        this.verdi = verdi
    }

    private val farge: Farge
    val verdi: Verdi

    override fun toString(): String {
        return farge.kode + verdi.kode
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Kort

        if (farge != other.farge) return false
        if (verdi != other.verdi) return false

        return true
    }

    override fun hashCode(): Int {
        var result = farge.hashCode()
        result = 31 * result + verdi.hashCode()
        return result
    }


    enum class Farge(val kode: Char) {
        SPAR('S'), HJERTER('H'), RUTER('R'), KLØVER('K');
    }

    enum class Verdi {
        TO("2"), TRE("3"), FIRE("4"), FEM("5"), SEKS("6"), SJU("7"),
        ÅTTE("8"), NI("9"), TI("10"), KNEKT("J", 10), DAME("Q",10),
        KONGE("K", 10), ESS("A", 11);

        val kode: String
        val tallVerdi: Int

        constructor(kode: String, tallVerdi: Int) {
            this.kode = kode
            this.tallVerdi = tallVerdi
        }

        constructor(kode: String) {
            this.kode = kode
            tallVerdi = kode.toInt()
        }
    }
}