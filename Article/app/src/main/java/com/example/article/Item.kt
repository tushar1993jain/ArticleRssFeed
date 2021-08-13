package com.example.article

import org.simpleframework.xml.Element

data class Item(var title: String? = null,
                var description: String? = null,
                var link: String? = null,
                var encoded: String? = null,
                var creator: String? = null,
                var pubDate: String? = null,
                var category: String? = null,
                var content: String? = null){
}