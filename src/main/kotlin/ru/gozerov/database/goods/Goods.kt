package ru.gozerov.database.goods

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.gozerov.database.goods.Goods.SMALL_PART_GOODS
import ru.gozerov.database.reviews.Reviews
import ru.gozerov.database.reviews.toReview
import ru.gozerov.features.goods.GoodRemote

object Goods : Table("goods") {

    private val vendorCode = Goods.integer("vendor_code")
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

    fun getGoods(): List<GoodRemote>? = transaction{
        val goods = Goods.selectAll().toList()
        return@transaction if (goods.isEmpty()) null else {
            goods.map { good ->
                val reviews = Reviews.getReviewsByGoodId(goodId = good[vendorCode]).map { it.toReview() }
                GoodRemote(
                    vendorCode = good[vendorCode],
                    name = good[name],
                    description = good[description],
                    price = good[price],
                    images = good[images]?.run { Json.decodeFromString(this) },
                    reviews = reviews.ifEmpty { null }
                )
            }
        }
    }

    fun getGoodsPack(): Map<String, List<GoodRemote>?> = transaction {
        val titles = listOf("Featured Product", "Best Sellers", "New Arrivals", "Top Rated Product", "Special Offers")
        return@transaction titles.associateWith {
            val query = SmallPartOfGoodsQuery(Goods).execute(this) ?: throw Exception("Query is null")
            val goods = mutableListOf<GoodRemote>()
            repeat(SMALL_PART_GOODS) {
                query.next()
                val vendorCode = query.getInt(vendorCode.name)
                val reviews = Reviews.getReviewsByGoodId(goodId = vendorCode).map { it.toReview() }
                val good = GoodRemote(
                    vendorCode = vendorCode,
                    name = query.getString(name.name),
                    description = query.getString(description.name),
                    price = query.getInt(price.name),
                    images = query.getString(images.name)?.run { Json.decodeFromString(this) },
                    reviews = reviews
                )
                goods.add(good)
            }
            goods
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
            val vendorCode = query.getInt(vendorCode.name)
            val reviews = Reviews.getReviewsByGoodId(goodId = vendorCode).map { it.toReview() }
            goods.add(
                GoodRemote(
                    vendorCode = vendorCode,
                    name = query.getString(name.name),
                    description = query.getString(description.name),
                    price = query.getInt(price.name),
                    images = query.getString(images.name)?.run { Json.decodeFromString(this) },
                    reviews = reviews
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
            val reviews = Reviews.getReviewsByGoodId(goodId = good[vendorCode]).map { it.toReview() }
            GoodRemote(
                vendorCode = good[vendorCode],
                name = good[name],
                description = good[description],
                price = good[price],
                images = good[images]?.run { Json.decodeFromString(this) },
                reviews = reviews
            )
        }
    }

    fun getCategories() : List<Category> = CATEGORIES

    fun getGoodsByCategory(name: String) : Pair<Category, List<GoodRemote>?>? = transaction {
        val goods = Goods.selectAll().toList()
        val category = CATEGORIES.firstOrNull { it.name.lowercase() == name.lowercase() }
        return@transaction if (goods.isEmpty() || category == null) null else {
            category to goods.map { good ->
                val reviews = Reviews.getReviewsByGoodId(goodId = good[vendorCode]).map { it.toReview() }
                GoodRemote(
                    vendorCode = good[vendorCode],
                    name = good[this@Goods.name],
                    description = good[description],
                    price = good[price],
                    images = good[images]?.run { Json.decodeFromString(this) },
                    reviews = reviews.ifEmpty { null }
                )
            }
        }
    }

    const val SMALL_PART_GOODS = 5

    private val CATEGORIES = listOf(
        Category(0, "Food"),
        Category(1, "Gift"),
        Category(2, "Fashion"),
        Category(3, "Gadget"),
        Category(4, "Computer"),
        Category(5, "Souvenir")
    )

}

data class PartOfGoodsQuery(
    private val index: Int,
    private val table: Table
    ) : Query(table, null) {
    override fun prepareSQL(builder: QueryBuilder): String {
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

data class SmallPartOfGoodsQuery(
    private val table: Table
) : Query(table, null) {

    override fun prepareSQL(builder: QueryBuilder): String {
        val start = kotlin.random.Random.nextInt(2)
        builder {
            append("SELECT * FROM ${table.tableName} " +
                    "OFFSET $start " +
                    "FETCH NEXT ${start + SMALL_PART_GOODS} ROWS ONLY")
        }
        return builder.toString()
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