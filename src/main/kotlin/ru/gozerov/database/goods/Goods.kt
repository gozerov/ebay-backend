package ru.gozerov.database.goods

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.gozerov.features.goods.GoodRemote

object Goods : Table("goods") {

    private val vendorCode = Goods.integer("vendor_code").autoIncrement()
    private val name = Goods.varchar("name", 50)
    private val description = Goods.varchar("description", 250)
    private val price = Goods.integer("price")
    private val images = Goods.varchar("images", 1024).nullable()

    fun insert(goodDTO: GoodDTO) {
        transaction {
            Goods.insert {
                it[name] = goodDTO.name
                it[description] = goodDTO.description
                it[price] = goodDTO.price
                it[images] = Json.encodeToString(value = goodDTO.images)
            }
        }
    }

    fun getGoods(): List<GoodDTO>? = transaction{
        val goods = Goods.selectAll().toList()
        return@transaction if (goods.isEmpty()) null else
            goods.map { good ->
                GoodDTO(
                    vendorCode = good[vendorCode],
                    name = good[name],
                    description = good[description],
                    price = good[price],
                    images = good[images]?.run { Json.decodeFromString(this) }
                )
            }
    }
    fun getGoodsSize(): Int? = transaction {
        val query = GoodsSizeQuery(Goods).execute(this)
        query?.next()
        return@transaction query?.getInt("count")

    }

    fun getPartOfGoods(index: Int): List<GoodRemote>? = transaction {
        if (index < 0) return@transaction null
        val query = PartOfGoodsQuery(index, Goods).execute(this)
        val goods = mutableListOf<GoodRemote>()
        if (query == null) throw Exception("Query is null")
        while (query.next()) {
            goods.add(
                GoodRemote(
                    vendorCode = query.getInt(vendorCode.name),
                    name = query.getString(name.name),
                    description = query.getString(description.name),
                    price = query.getInt(price.name),
                    images = query.getString(images.name)?.run { Json.decodeFromString(this) }
                )
            )
        }
        if (goods.isEmpty())
            return@transaction null
        else
            return@transaction goods

    }

    fun getGoodById(id: Int) : GoodRemote? = transaction {
        val good = Goods.select { vendorCode.eq(id) }.firstOrNull()
        return@transaction good?.let {
            GoodRemote(
                vendorCode = good[vendorCode],
                name = good[name],
                description = good[description],
                price = good[price],
                images = good[images]?.run { Json.decodeFromString(this) }
            )
        }
    }

}

data class PartOfGoodsQuery(
    private val index: Int,
    private val table: Table
    ) : Query(table, null) {
    override fun prepareSQL(builder: QueryBuilder): String {
        println(index)
        builder {
            append("SELECT * FROM ${table.tableName} " +
                    "OFFSET ${index * ROW_PART_COUNT} " +
                    "FETCH NEXT ${(index + 1) * ROW_PART_COUNT} ROWS ONLY")
        }
        return builder.toString()
    }

    companion object {
        const val ROW_PART_COUNT = 10
    }

}
data class GoodsSizeQuery(private val table: Table) : Query(table, null) {
    override fun prepareSQL(builder: QueryBuilder): String {
        builder {
            append("SELECT count(\"vendor_code\") FROM ${table.tableName}")
        }
        return builder.toString()
    }

}