package com.dreampany.tools.util

import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.util.DataUtil
import com.dreampany.tools.data.model.Word

/**
 * Created by Roman-372 on 8/26/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Util {
    companion object {

        fun concat(type: Type, subtype: Subtype): String {
            return type.name + subtype.name
        }

        fun getText(word: Word): String {
            val builder = StringBuilder()
            if (word.hasDefinitions()) {
                for (item in word.definitions!!) {
                    DataUtil.joinString(builder, item.text)
                }
            }

            if (word.hasExamples()) {
                for (item in word.examples!!) {
                    DataUtil.joinString(builder, item)
                }
            }

            if (word.hasSynonyms()) {
                for (item in word.synonyms!!) {
                    DataUtil.joinString(builder, item)
                }
            }

            if (word.hasAntonyms()) {
                for (item in word.antonyms!!) {
                    DataUtil.joinString(builder, item)
                }

            }

            return builder.toString()
        }
    }
}