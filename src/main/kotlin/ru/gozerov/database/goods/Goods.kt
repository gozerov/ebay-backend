package ru.gozerov.database.goods

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

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
}