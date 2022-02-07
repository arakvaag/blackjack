package org.rakvag.blackjack.domene

data class Kort(
    val farge: Farge,
    val verdi: Verdi
) {

    override fun toString(): String {
        return farge.kode + verdi.kode
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