package com.tony.id.test

import com.tony.id.IdGenerator
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest

@SpringBootApplication
class TestIdApp

@SpringBootTest(classes = [TestIdApp::class], webEnvironment = SpringBootTest.WebEnvironment.NONE)
class IdAppTest {

    @Test
    fun test() {
        println(IdGenerator.nextId())
    }

    @Test
    fun genJson() {
    val id = IdGenerator.nextId()
        val json = """{
  "id": "$id",
  "goodsType": 1,
  "goodsName": "普通商品$id",
  "files": [
    {
      "id": "$id",
      "fileId": "1671362709004869633",
      "fileName": "1.jpg",
      "filePath": "images/2023-06-21/1442370475882737665_1687318893793580992.jpg",
      "category": "IMAGE",
      "goodsId": "7077129358711721984"
    }
  ],
  "remark": "%3Cp%3Eq%3C%2Fp%3E",
  "groupId": "",
  "groupName": "",
  "brandId": "",
  "brandName": "",
  "sellingPoint": "",
  "supplier": "",
  "goodsSpecList": [
    {
      "specName": "尺寸",
      "filePath": "+",
      "specValue": "小",
      "valueList": [
        {
          "name": "小",
          "file": "",
          "fileId": "",
          "select": true
        }
      ]
    }
  ],
  "goodsSkuList": [
    {
      "skuSpecsValue": "小",
      "id": "$id",
      "salePrice": 100,
      "costPrice": 100,
      "marketPrice": 100,
      "goodsUnit": "1",
      "weight": 1,
      "inventory": 1,
      "barCode": "",
      "fileId": "",
      "filePath": ""
    }
  ],
  "payType": null,
  "depositCharge": "",
  "depositPay": "",
  "saleRuleType": 1,
  "saleRuleValue": null,
  "unit": "1",
  "stock": 1,
  "goodsNo": "",
  "inventoryDeductionMode": 0,
  "state": 1,
  "onSale": true,
  "onSaleChangeTime": "",
  "isVirtual": false,
  "categoryId": "7077114699027845120",
  "categoryName": "黄瓜",
  "needDelivery": true,
  "needDeposit": false
}"""
        println(json)
    }
}
