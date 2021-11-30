package abdul.learningjournal.spark.example

import org.scalatest.{BeforeAndAfterAll, FunSuite}
import org.apache.spark.sql.{DataFrame, SparkSession}
import abdul.learningjournal.spark.example.HelloSpark.{countByCountry, loadSurveyDF}

import scala.collection.mutable

class HelloSparkTest extends FunSuite with BeforeAndAfterAll {

  @transient var spark: SparkSession = _

  override def beforeAll() = {
    spark = SparkSession.builder()
      .appName("HelloSparkTest")
      .master("local[3]")
      .getOrCreate()
  }

  override def afterAll() = {
    spark.stop()
  }

  test("Data File Loading") {
    val sampleDF = loadSurveyDF(spark, "data/sample.csv")
    val rCount = sampleDF.count()
    assert(rCount == 9, " record should be 9")
  }

  test("Count by Country"){
    val sampleDF = loadSurveyDF(spark, "data/sample.csv")
    val countDF = countByCountry(sampleDF)
    val countryMap = new mutable.HashMap[String, Long]
    countDF.collect().foreach(r => countryMap.put(r.getString(0), r.getLong(1)))

    assert(countryMap("United States") == 4, ": Count for United States should be 4")
    assert(countryMap("Canada") == 2, ": Count for Canada should be 2")
    assert(countryMap("United Kingdom") == 1, ": Count for United Kingdom should be 1")
  }

}
