package toys.aisamji.bbsrecipeguide.data

class Recipe internal constructor(
        val id: Int,
        val typeId: Int,
        val successRate: Double,
        val resultId: Int,
        val ingredientIds: Array<Int>,
        val melders: Array<Wielder>
) {

    class Type internal constructor(
            val id: Int,
            val shimmeringResultId: Int,
            val fleetingResultId: Int,
            val pulsingResultd: Int,
            val wellspringResultId: Int,
            val soothingResultId: Int,
            val hungryResultId: Int,
            val aboundingResultId: Int
    ) { }
}
