/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.tony.test.id

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
