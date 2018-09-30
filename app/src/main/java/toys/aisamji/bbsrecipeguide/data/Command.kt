package toys.aisamji.bbsrecipeguide.data

class Command internal constructor(
        val id: Int,
        val name: String,
        val type: Type,
        val wielders: Array<Wielder>
) {

    enum class Type {
        ATTACK,
        MAGIC,
        ACTION,
        SHOTLOCK
    }
}
